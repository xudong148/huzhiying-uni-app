import { masterOrders, walletData } from '../mock/data';
import { request, shouldUseMock } from '../utils/request';

const clone = (payload) => JSON.parse(JSON.stringify(payload));

const statusTextMap = {
  PENDING_DISPATCH: '平台正在匹配可服务师傅',
  PENDING_ACCEPT: '等待师傅确认上门',
  ON_THE_WAY: '已出发，正在前往服务地点',
  ARRIVED: '已到达，等待开始施工',
  WAITING_SUPPLEMENT_PAYMENT: '已提交增项报价',
  IN_SERVICE: '施工中',
  COMPLETED: '已完工',
  REFUNDING: '退款处理中',
  CANCELLED: '已取消',
};

function normalizeOrder(item) {
  return {
    ...item,
    address: item.address?.detail || item.address || '待补充地址',
    price: Number(item.amount || 0),
    statusText: statusTextMap[item.status] || '订单处理中',
    timeline: item.timeline || [],
    quotation: item.quotation || null,
  };
}

export async function getDispatchOrders() {
  if (shouldUseMock()) {
    return { data: clone(masterOrders) };
  }
  const response = await request({ url: '/api/dispatch/tasks' });
  return {
    data: (response.data || []).map((item) => ({
      id: item.id,
      orderId: item.orderId,
      title: item.title,
      income: Number(item.income || 0),
      distance: item.distance,
      address: item.area,
      currentMaster: item.currentMaster,
      tags: item.tags || [],
    })),
  };
}

export async function claimDispatchOrder(taskId, masterName = '张师傅') {
  if (shouldUseMock()) {
    return { data: { taskId, masterName } };
  }
  return request({
    url: `/api/dispatch/tasks/${taskId}/claim`,
    method: 'POST',
    data: { masterName },
  });
}

export async function getMasterDashboard() {
  if (shouldUseMock()) {
    return {
      data: {
        dispatchCount: 2,
        wallet: walletData,
        orders: [],
      },
    };
  }
  const response = await request({ url: '/api/master/dashboard' });
  return {
    data: {
      ...response.data,
      orders: (response.data.orders || []).map(normalizeOrder),
    },
  };
}

export async function getWalletData() {
  if (shouldUseMock()) {
    return { data: clone(walletData) };
  }
  const response = await request({ url: '/api/master/wallet' });
  return {
    data: {
      balance: Number(response.data.account.available || 0),
      frozen: Number(response.data.account.frozen || 0),
      todayIncome: Number(response.data.account.todayIncome || 0),
      transactions: response.data.transactions || [],
    },
  };
}

export async function getMasterSettings() {
  if (shouldUseMock()) {
    return {
      data: {
        listening: true,
        maxDistance: '20km',
        privacyNumber: true,
      },
    };
  }
  return request({ url: '/api/master/settings' });
}

export async function saveMasterSettings(payload) {
  if (shouldUseMock()) {
    return { data: payload };
  }
  return request({
    url: '/api/master/settings',
    method: 'POST',
    data: payload,
  });
}

export async function applyMaster(payload) {
  if (shouldUseMock()) {
    return { data: payload };
  }
  return request({
    url: '/api/master/apply',
    method: 'POST',
    data: payload,
  });
}
