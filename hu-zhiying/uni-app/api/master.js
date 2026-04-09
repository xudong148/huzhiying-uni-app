import { request } from '../utils/request';

const statusTextMap = {
  PENDING_DISPATCH: '平台正在匹配可服务师傅',
  PENDING_ACCEPT: '等待师傅确认上门',
  ON_THE_WAY: '师傅已出发，正在前往服务地址',
  ARRIVED: '师傅已到场，等待开始施工',
  WAITING_SUPPLEMENT_PAYMENT: '已提交增项报价，等待补款',
  IN_SERVICE: '施工中',
  COMPLETED: '已完工',
  REFUNDING: '退款处理中',
  CANCELLED: '已取消',
};

function normalizeOrder(item) {
  return {
    ...item,
    title: item.title || item.serviceName || '待确认服务工单',
    address: item.address?.detail || item.address || '待补充地址',
    appointment: item.appointment || item.scheduleTime || '以订单详情为准',
    price: Number(item.amount || 0),
    statusText: item.statusText || statusTextMap[item.status] || '订单处理中',
    timeline: item.timeline || [],
    quotation: item.quotation || null,
  };
}

function normalizeTransaction(item = {}) {
  return {
    ...item,
    title: item.title || item.type || '钱包流水',
    time: item.time || item.createdAt || '',
    amount: Number(item.amount || 0),
  };
}

export async function getDispatchOrders() {
  const response = await request({ url: '/api/dispatch/tasks' });
  return {
    data: (response.data || []).map((item) => ({
      id: item.id,
      orderId: item.orderId,
      title: item.title,
      income: Number(item.income || 0),
      distance: item.distance || '',
      address: item.area || '',
      currentMaster: item.currentMaster || '',
      tags: item.tags || [],
    })),
  };
}

export function claimDispatchOrder(taskId, payload = {}) {
  return request({
    url: `/api/dispatch/tasks/${taskId}/claim`,
    method: 'POST',
    data: payload,
  });
}

export async function getMasterDashboard() {
  const response = await request({ url: '/api/master/dashboard' });
  return {
    data: {
      ...response.data,
      currentOrder: response.data.currentOrder ? normalizeOrder(response.data.currentOrder) : null,
      orders: (response.data.orders || []).map(normalizeOrder),
    },
  };
}

export async function getWalletData() {
  const response = await request({ url: '/api/master/wallet' });
  return {
    data: {
      balance: Number(response.data.account.available || 0),
      frozen: Number(response.data.account.frozen || 0),
      todayIncome: Number(response.data.account.todayIncome || 0),
      transactions: (response.data.transactions || []).map(normalizeTransaction),
    },
  };
}

export async function getMasterSettings() {
  const response = await request({ url: '/api/master/settings' });
  return {
    data: {
      listening: response.data?.listening !== false,
      maxDistance: response.data?.maxDistance || '20km',
      privacyNumber: response.data?.privacyNumber !== false,
    },
  };
}

export async function saveMasterSettings(payload) {
  const response = await request({
    url: '/api/master/settings',
    method: 'POST',
    data: payload,
  });
  return {
    data: {
      listening: response.data?.listening !== false,
      maxDistance: response.data?.maxDistance || payload.maxDistance || '20km',
      privacyNumber: response.data?.privacyNumber !== false,
    },
  };
}

export function applyMaster(payload) {
  return request({
    url: '/api/master/apply',
    method: 'POST',
    data: payload,
  });
}

export function checkInOrder(orderId, payload) {
  return request({
    url: `/api/master/orders/${orderId}/check-in`,
    method: 'POST',
    data: payload,
  });
}

export function uploadBeforeWorkMedia(orderId, payload) {
  return request({
    url: `/api/master/orders/${orderId}/before-work-media`,
    method: 'POST',
    data: payload,
  });
}

export function uploadAfterWorkMedia(orderId, payload) {
  return request({
    url: `/api/master/orders/${orderId}/after-work-media`,
    method: 'POST',
    data: payload,
  });
}
