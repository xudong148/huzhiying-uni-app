import { request } from '../utils/request';

const statusTextMap = {
  PENDING_PAYMENT: '待支付预付款，支付后开始派单',
  PENDING_DISPATCH: '平台正在匹配可服务师傅',
  PENDING_ACCEPT: '等待师傅确认上门',
  ON_THE_WAY: '师傅已出发，正在赶来',
  ARRIVED: '师傅已到场，等待开始施工',
  WAITING_SUPPLEMENT_PAYMENT: '师傅已提交增项报价，等待确认补款',
  IN_SERVICE: '订单正在施工中',
  COMPLETED: '服务已完成，欢迎评价',
  REFUNDING: '退款处理中',
  CANCELLED: '订单已取消',
};

function normalizeTimeline(timeline = []) {
  return timeline.map((item) => ({
    key: item.key,
    label: item.label,
    desc: item.desc,
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
    totalAmount: Number(quotation.totalAmount || 0),
    items: (quotation.items || []).map((item, index) => ({
      id: `${quotation.id}-${index + 1}`,
      name: item.name,
      amount: Number(item.amount || 0),
    })),
  };
}

function normalizeServiceOrder(item) {
  const tags = item.status === 'PENDING_PAYMENT'
    ? ['待支付', '支付后派单']
    : item.status === 'WAITING_SUPPLEMENT_PAYMENT'
      ? ['待补款', '平台担保']
      : item.paymentStatus === 'PARTIAL_PAID'
        ? ['预付上门费', '平台担保']
        : ['已支付'];

  return {
    ...item,
    type: 'service',
    statusText: statusTextMap[item.status] || '订单处理中',
    address: item.address?.detail || item.address || '待补充地址',
    price: Number(item.amount || 0),
    eta: item.eta || '待确认',
    masterMobile: item.masterName && item.masterName !== '待接单' ? '170****8899' : '',
    tags,
    timeline: normalizeTimeline(item.timeline),
    quotation: normalizeQuotation(item.quotation),
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
  };
}

/**
 * 查询订单列表。
 */
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

/**
 * 查询订单详情。
 * @param {string} orderId
 */
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

/**
 * 查询订单轨迹。
 * @param {string} orderId
 */
export function getOrderTracking(orderId) {
  return request({ url: `/api/orders/${orderId}/tracking` });
}

/**
 * 查询预约时段。
 */
export async function getTimeSlots() {
  const response = await request({ url: '/api/service-orders/time-slots' });
  return {
    data: (response.data || []).map((item) => item.value),
  };
}

/**
 * 创建服务订单。
 * @param {object} payload
 */
export function createServiceOrder(payload) {
  return request({
    url: '/api/service-orders',
    method: 'POST',
    data: payload,
  });
}

/**
 * 创建商品订单。
 * @param {object} payload
 */
export function createProductOrder(payload) {
  return request({
    url: '/api/product-orders',
    method: 'POST',
    data: payload,
  });
}

/**
 * 创建微信预支付单。
 * @param {string} orderId
 */
export function requestWechatPrepay(orderId) {
  return request({
    url: '/api/payments/wechat/prepay',
    method: 'POST',
    data: { orderId },
  });
}

/**
 * 确认增项报价。
 * @param {string} quotationId
 */
export function confirmQuotation(quotationId) {
  return request({
    url: `/api/quotations/${quotationId}/confirm`,
    method: 'POST',
  });
}

/**
 * 更新服务订单状态。
 * @param {string} orderId
 * @param {string} status
 */
export function updateServiceOrderStatus(orderId, status) {
  return request({
    url: `/api/service-orders/${orderId}/status`,
    method: 'POST',
    data: { status },
  });
}

/**
 * 创建增项报价。
 * @param {string} orderId
 * @param {string} remark
 */
export function createQuotation(orderId, remark) {
  return request({
    url: '/api/quotations',
    method: 'POST',
    data: { orderId, remark },
  });
}

/**
 * 发起退款。
 * @param {string} orderId
 */
export function refundOrder(orderId) {
  return request({
    url: '/api/payments/wechat/refund',
    method: 'POST',
    data: { orderId },
  });
}

/**
 * 取消订单。
 * @param {string} orderId
 * @param {string} reason
 */
export function cancelOrder(orderId, reason) {
  return request({
    url: `/api/orders/${orderId}/cancel`,
    method: 'POST',
    data: { reason },
  });
}

/**
 * 催单。
 * @param {string} orderId
 * @param {string} remark
 */
export function urgeOrder(orderId, remark) {
  return request({
    url: `/api/orders/${orderId}/urge`,
    method: 'POST',
    data: { remark },
  });
}
