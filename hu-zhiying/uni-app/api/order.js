import { request } from '../utils/request';

const statusTextMap = {
  PENDING_PAYMENT: '待支付预付款，支付后开始派单',
  PENDING_DISPATCH: '平台正在匹配可服务师傅',
  PENDING_ACCEPT: '等待师傅确认上门',
  ON_THE_WAY: '师傅已出发，正在赶来',
  ARRIVED: '师傅已到场，等待开始施工',
  WAITING_SUPPLEMENT_PAYMENT: '师傅已提交增项报价，等待补款',
  IN_SERVICE: '订单正在施工中',
  COMPLETED: '服务已完成，欢迎评价',
  REFUNDING: '退款处理中',
  CANCELLED: '订单已取消',
};

function normalizeTimeline(timeline = []) {
  return timeline.map((item) => ({
    key: item.key || item.stepKey,
    label: item.label,
    desc: item.desc || item.description,
    done: item.done,
    time: item.time,
  }));
}

function normalizeQuotation(quotation) {
  if (!quotation) {
    return null;
  }
  return {
    ...quotation,
    quotationId: quotation.quotationId || quotation.id,
    totalAmount: Number(quotation.totalAmount || 0),
    items: (quotation.items || []).map((item, index) => ({
      id: `${quotation.id || quotation.quotationId}-${index + 1}`,
      name: item.name,
      amount: Number(item.amount || 0),
    })),
  };
}

function normalizeServiceOrder(item) {
  return {
    ...item,
    type: 'service',
    statusText: item.statusText || statusTextMap[item.status] || '订单处理中',
    address: item.address || '待补充地址',
    price: Number(item.amount || 0),
    eta: item.eta || '待确认',
    masterMobile: item.masterMobile || '',
    tags: [],
    timeline: normalizeTimeline(item.timeline),
    quotation: normalizeQuotation(item.quotation),
    mediaFiles: item.mediaFiles || [],
    messageSummary: item.messageSummary || null,
  };
}

function normalizeProductOrder(item) {
  const statusText = item.status === 'PENDING_SHIPMENT'
    ? '仓库已打包，等待出库'
    : item.status === 'PENDING_PAYMENT'
      ? '等待完成支付确认'
      : '商品订单处理中';

  return {
    ...item,
    type: 'product',
    statusText,
    appointment: item.status === 'PENDING_SHIPMENT' ? '预计 1-2 天内送达' : '支付成功后开始备货',
    address: item.address?.detail || item.address || '待补充地址',
    price: Number(item.amount || 0),
    tags: item.createInstallOrder ? ['商城订单', '自动生成安装工单'] : ['商城订单'],
    timeline: [],
    quotation: null,
    mediaFiles: [],
    messageSummary: null,
  };
}

export async function getOrderList() {
  const [serviceRes, productRes] = await Promise.all([
    request({ url: '/api/service-orders' }),
    request({ url: '/api/product-orders' }),
  ]);

  return {
    data: [
      ...(serviceRes.data || []).map(normalizeServiceOrder),
      ...(productRes.data || []).map(normalizeProductOrder),
    ],
  };
}

export async function getOrderDetail(orderId) {
  const response = orderId.startsWith('PO')
    ? await request({ url: `/api/product-orders/${orderId}` })
    : await request({ url: `/api/service-orders/${orderId}` });

  return {
    data: orderId.startsWith('PO')
      ? normalizeProductOrder(response.data)
      : normalizeServiceOrder(response.data),
  };
}

export function getOrderTracking(orderId) {
  return request({ url: `/api/orders/${orderId}/tracking` });
}

export async function getTimeSlots() {
  const response = await request({ url: '/api/service-orders/time-slots' });
  return {
    data: (response.data || []).map((item) => item.value),
  };
}

export function createServiceOrder(payload) {
  return request({
    url: '/api/service-orders',
    method: 'POST',
    data: payload,
  });
}

export function createProductOrder(payload) {
  return request({
    url: '/api/product-orders',
    method: 'POST',
    data: payload,
  });
}

export function requestWechatPrepay(orderId) {
  return request({
    url: '/api/payments/wechat/prepay',
    method: 'POST',
    data: { orderId },
  });
}

export function confirmQuotation(quotationId) {
  return request({
    url: `/api/quotations/${quotationId}/confirm`,
    method: 'POST',
  });
}

export function updateServiceOrderStatus(orderId, status) {
  return request({
    url: `/api/service-orders/${orderId}/status`,
    method: 'POST',
    data: { status },
  });
}

export function createQuotation(orderId, remark) {
  return request({
    url: '/api/quotations',
    method: 'POST',
    data: { orderId, remark },
  });
}

export function refundOrder(orderId, payload = {}) {
  return request({
    url: '/api/payments/wechat/refund',
    method: 'POST',
    data: {
      orderId,
      ...payload,
    },
  });
}

export function cancelOrder(orderId, reason) {
  return request({
    url: `/api/orders/${orderId}/cancel`,
    method: 'POST',
    data: { reason },
  });
}

export function urgeOrder(orderId, remark) {
  return request({
    url: `/api/orders/${orderId}/urge`,
    method: 'POST',
    data: { remark },
  });
}
