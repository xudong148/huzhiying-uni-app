import { orderList, quotationDetail, timeSlots } from '../mock/data';
import { request, shouldUseMock } from '../utils/request';

const clone = (payload) => JSON.parse(JSON.stringify(payload));

const statusTextMap = {
  PENDING_DISPATCH: '平台正在匹配可服务师傅',
  PENDING_ACCEPT: '附近师傅正在抢单',
  ON_THE_WAY: '师傅已出发，正在赶来',
  ARRIVED: '师傅已到达，等待开始施工',
  WAITING_SUPPLEMENT_PAYMENT: '师傅已提交增项报价，待确认补款',
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
  return {
    ...item,
    type: 'service',
    statusText: statusTextMap[item.status] || '订单处理中',
    address: item.address?.detail || item.address || '待补充地址',
    price: Number(item.amount || 0),
    eta: item.eta || '待确认',
    masterMobile: item.masterName && item.masterName !== '待接单' ? '170****8899' : '',
    tags: item.paymentStatus === 'PARTIAL_PAID' ? ['预付上门费', '平台担保'] : ['已支付'],
    timeline: normalizeTimeline(item.timeline),
    quotation: normalizeQuotation(item.quotation),
  };
}

function normalizeProductOrder(item) {
  const statusText = item.status === 'PENDING_SHIPMENT'
    ? '仓库已打单，等待出库'
    : item.status === 'PENDING_PAYMENT'
      ? '待完成支付确认'
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

export async function getOrderList() {
  if (shouldUseMock()) {
    return { data: clone(orderList) };
  }

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
  if (shouldUseMock()) {
    const order = orderList.find((item) => item.id === orderId) || orderList[0];
    return {
      data: {
        ...clone(order),
        quotation: clone(quotationDetail),
      },
    };
  }

  const response = orderId.startsWith('PO')
    ? await request({ url: `/api/product-orders/${orderId}` })
    : await request({ url: `/api/service-orders/${orderId}` });

  return {
    data: orderId.startsWith('PO')
      ? normalizeProductOrder(response.data)
      : normalizeServiceOrder(response.data),
  };
}

export async function getTimeSlots() {
  return { data: clone(timeSlots) };
}

export async function createServiceOrder(payload) {
  if (shouldUseMock()) {
    return {
      data: {
        id: `SO${Date.now()}`,
      },
    };
  }

  return request({
    url: '/api/service-orders',
    method: 'POST',
    data: payload,
  });
}

export async function createProductOrder(payload) {
  if (shouldUseMock()) {
    return {
      data: {
        id: `PO${Date.now()}`,
        ...payload,
      },
    };
  }

  return request({
    url: '/api/product-orders',
    method: 'POST',
    data: payload,
  });
}

export async function requestWechatPrepay(orderId) {
  if (shouldUseMock()) {
    return {
      data: {
        orderId,
        sandbox: true,
      },
    };
  }
  return request({
    url: '/api/payments/wechat/prepay',
    method: 'POST',
    data: { orderId },
  });
}

export async function confirmWechatPayment(orderId) {
  if (shouldUseMock()) {
    return {
      data: {
        orderId,
        status: 'SUCCESS',
      },
    };
  }
  return request({
    url: `/api/payments/wechat/${orderId}/callback`,
    method: 'POST',
  });
}

export async function confirmQuotation(quotationId) {
  if (shouldUseMock()) {
    return { data: { quotationId } };
  }
  return request({
    url: `/api/quotations/${quotationId}/confirm`,
    method: 'POST',
  });
}

export async function updateServiceOrderStatus(orderId, status) {
  if (shouldUseMock()) {
    return { data: { orderId, status } };
  }
  return request({
    url: `/api/service-orders/${orderId}/status`,
    method: 'POST',
    data: { status },
  });
}

export async function createQuotation(orderId, remark) {
  if (shouldUseMock()) {
    return { data: { orderId, remark } };
  }
  return request({
    url: '/api/quotations',
    method: 'POST',
    data: { orderId, remark },
  });
}

export async function refundOrder(orderId) {
  if (shouldUseMock()) {
    return { data: { orderId } };
  }
  return request({
    url: '/api/payments/wechat/refund',
    method: 'POST',
    data: { orderId },
  });
}
