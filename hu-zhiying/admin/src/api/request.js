const RUNTIME_KEY = 'hzy-admin-runtime';
const SESSION_KEY = 'hzy-admin-session';

const defaultRuntime = {
  baseUrl: 'http://localhost:8080',
};

const defaultSession = {
  token: '',
  refreshToken: '',
  accessExpiresAt: '',
  refreshExpiresAt: '',
  profile: null,
  menus: [],
  permissions: [],
};

function getStorage(target = 'local') {
  if (typeof window === 'undefined') {
    return null;
  }
  return target === 'session' ? window.sessionStorage : window.localStorage;
}

function readStorage(key, fallback, target = 'local') {
  const storage = getStorage(target);
  if (!storage) {
    return fallback;
  }

  try {
    const raw = storage.getItem(key);
    return raw ? { ...fallback, ...JSON.parse(raw) } : fallback;
  } catch (error) {
    return fallback;
  }
}

function writeStorage(key, value, target = 'local') {
  const storage = getStorage(target);
  if (!storage) {
    return;
  }
  storage.setItem(key, JSON.stringify(value));
}

function normalizeAmount(value) {
  return typeof value === 'number' ? value : Number(value || 0);
}

function normalizeSession(payload = {}) {
  return {
    ...defaultSession,
    ...payload,
    profile: payload.profile || null,
    menus: Array.isArray(payload.menus) ? payload.menus : [],
    permissions: Array.isArray(payload.permissions) ? payload.permissions : [],
  };
}

export function getRuntimeConfig() {
  return readStorage(RUNTIME_KEY, defaultRuntime, 'local');
}

export function setRuntimeConfig(payload) {
  const next = {
    ...getRuntimeConfig(),
    ...payload,
  };
  writeStorage(RUNTIME_KEY, next, 'local');
  return next;
}

export function buildAdminWsUrl(path) {
  const runtime = getRuntimeConfig();
  return `${runtime.baseUrl.replace(/^http/, 'ws')}${path}`;
}

export function getAdminSession() {
  return normalizeSession(readStorage(SESSION_KEY, defaultSession, 'session'));
}

export function saveAdminSession(session) {
  writeStorage(SESSION_KEY, normalizeSession(session), 'session');
}

export function clearAdminSession() {
  const storage = getStorage('session');
  if (storage) {
    storage.removeItem(SESSION_KEY);
  }
}

async function refreshAccessToken(refreshToken) {
  const runtime = getRuntimeConfig();
  const response = await fetch(`${runtime.baseUrl}/api/auth/refresh`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({
      refreshToken,
      clientType: 'admin-web',
    }),
  });
  const payload = await response.json().catch(() => null);
  if (!response.ok || !payload?.success) {
    clearAdminSession();
    throw new Error(payload?.message || '登录已过期');
  }
  saveAdminSession(payload.data);
}

async function rawRequest({ url, method = 'GET', data, auth = true, retry = true }) {
  const runtime = getRuntimeConfig();
  const session = getAdminSession();
  const headers = {
    'Content-Type': 'application/json',
  };

  if (auth && session.token) {
    headers.Authorization = `Bearer ${session.token}`;
  }

  const response = await fetch(`${runtime.baseUrl}${url}`, {
    method,
    headers,
    body: data ? JSON.stringify(data) : undefined,
  });

  if (response.status === 401 && retry && session.refreshToken) {
    await refreshAccessToken(session.refreshToken);
    return rawRequest({ url, method, data, auth, retry: false });
  }

  const payload = await response.json().catch(() => null);
  if (!response.ok || !payload?.success) {
    throw new Error(payload?.message || `请求失败: ${response.status}`);
  }

  return payload;
}

export async function request(options) {
  return rawRequest(options);
}

export async function loginAsAdmin(credentials) {
  const payload = await rawRequest({
    url: '/api/auth/admin-login',
    method: 'POST',
    data: credentials,
    auth: false,
  });
  return normalizeSession(payload.data);
}

export async function fetchAdminSession() {
  return normalizeSession((await rawRequest({ url: '/api/auth/session' })).data);
}

export async function fetchAdminDashboard() {
  return (await rawRequest({ url: '/api/admin/dashboard' })).data;
}

export async function fetchAdminDispatch() {
  return (await rawRequest({ url: '/api/admin/dispatch' })).data.map((item) => ({
    ...item,
    amount: normalizeAmount(item.amount),
  }));
}

export async function forceAssignDispatch(taskId, masterName) {
  return (
    await rawRequest({
      url: `/api/admin/dispatch/${taskId}/assign`,
      method: 'POST',
      data: { masterName },
    })
  ).data;
}

export async function fetchAdminOrders() {
  return (await rawRequest({ url: '/api/admin/orders' })).data.map((item) => ({
    ...item,
    amount: normalizeAmount(item.amount),
  }));
}

export async function fetchAdminMasters() {
  return (await rawRequest({ url: '/api/admin/masters' })).data.map((item) => ({
    ...item,
    name: item.realName,
    skills: item.skillTags,
    score: item.creditScore,
    status: item.online ? '在线' : '休息',
    credit: item.creditScore >= 95 ? 'A' : 'A-',
    area: item.serviceArea,
    deposit: normalizeAmount(item.deposit),
  }));
}

export async function fetchAdminFinance() {
  return (await rawRequest({ url: '/api/admin/finance' })).data.map((item) => ({
    ...item,
    amount: normalizeAmount(item.amount),
  }));
}

export async function fetchAdminNotifications() {
  const data = (await rawRequest({ url: '/api/admin/notifications' })).data;
  return Array.isArray(data?.items) ? data.items : [];
}

export async function dispatchAdminNotifications(limit = 20) {
  return (await rawRequest({
    url: `/api/admin/notifications/dispatch?limit=${limit}`,
    method: 'POST',
  })).data;
}

export async function fetchAdminArbitrations() {
  return (await rawRequest({ url: '/api/admin/arbitrations' })).data;
}

export async function fetchAdminPricing() {
  return fetchCrudList('/api/admin/pricing/rules');
}

export async function fetchCrudList(resource) {
  return (await rawRequest({ url: resource })).data;
}

export async function fetchCrudDetail(resource, id) {
  return (await rawRequest({ url: `${resource}/${id}` })).data;
}

export async function createCrudItem(resource, payload) {
  return (await rawRequest({ url: resource, method: 'POST', data: payload })).data;
}

export async function updateCrudItem(resource, id, payload) {
  return (await rawRequest({ url: `${resource}/${id}`, method: 'PUT', data: payload })).data;
}

export async function deleteCrudItem(resource, id) {
  return (await rawRequest({ url: `${resource}/${id}`, method: 'DELETE' })).data;
}

export async function fetchRoleGrant(roleId) {
  return (await rawRequest({ url: `/api/admin/system/roles/${roleId}/grants` })).data;
}

export async function saveRoleGrant(roleId, payload) {
  return (await rawRequest({
    url: `/api/admin/system/roles/${roleId}/grants`,
    method: 'PUT',
    data: payload,
  })).data;
}

export async function fetchDispatchDetail(taskId) {
  return (await rawRequest({ url: `/api/admin/dispatch/${taskId}` })).data;
}

export async function assignDispatchTask(taskId, payload) {
  return (await rawRequest({ url: `/api/admin/dispatch/${taskId}/assign`, method: 'POST', data: payload })).data;
}

export async function cancelDispatchOrder(taskId, reason) {
  return (await rawRequest({
    url: `/api/admin/dispatch/${taskId}/cancel-order`,
    method: 'POST',
    data: { reason },
  })).data;
}

export async function fetchAdminOrderDetail(orderId) {
  return (await rawRequest({ url: `/api/admin/orders/${orderId}` })).data;
}

export async function cancelAdminOrder(orderId, reason) {
  return (await rawRequest({
    url: `/api/admin/orders/${orderId}/cancel`,
    method: 'POST',
    data: { reason },
  })).data;
}

export async function refundAdminOrder(orderId, reason) {
  return (await rawRequest({
    url: `/api/admin/orders/${orderId}/refund`,
    method: 'POST',
    data: { reason },
  })).data;
}

export async function grantAdminCoupon(orderId, payload) {
  return (await rawRequest({
    url: `/api/admin/orders/${orderId}/grant-coupon`,
    method: 'POST',
    data: payload,
  })).data;
}

export async function updateAdminOrderAppointment(orderId, appointment) {
  return (await rawRequest({
    url: `/api/admin/orders/${orderId}/appointment`,
    method: 'PUT',
    data: { appointment },
  })).data;
}

export async function fetchAdminMasterDetail(userId) {
  return (await rawRequest({ url: `/api/admin/masters/${userId}` })).data;
}

export async function updateAdminMaster(userId, payload) {
  return (await rawRequest({ url: `/api/admin/masters/${userId}`, method: 'PUT', data: payload })).data;
}

export async function enableAdminMaster(userId) {
  return (await rawRequest({ url: `/api/admin/masters/${userId}/enable`, method: 'POST' })).data;
}

export async function disableAdminMaster(userId) {
  return (await rawRequest({ url: `/api/admin/masters/${userId}/disable`, method: 'POST' })).data;
}

export async function updateAdminMasterCredit(userId, creditScore) {
  return (await rawRequest({
    url: `/api/admin/masters/${userId}/credit-score`,
    method: 'POST',
    data: { creditScore },
  })).data;
}

export async function fetchFinanceDetail(billNo) {
  return (await rawRequest({ url: `/api/admin/finance/${billNo}` })).data;
}

export async function approveFinanceBill(billNo, remark) {
  return (await rawRequest({
    url: `/api/admin/finance/${billNo}/approve`,
    method: 'POST',
    data: { remark },
  })).data;
}

export async function fetchArbitrationDetail(id) {
  return (await rawRequest({ url: `/api/admin/arbitrations/${id}` })).data;
}

export async function resolveArbitrationCase(id, payload) {
  return (await rawRequest({
    url: `/api/admin/arbitrations/${id}/resolve`,
    method: 'POST',
    data: payload,
  })).data;
}
