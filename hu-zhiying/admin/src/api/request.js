/**
 * 管理后台请求层。
 * 说明：
 * 1. 运行时只访问真实后端接口，不再保留演示分支。
 * 2. 统一处理鉴权注入、401 刷新和通用 CRUD 请求。
 */

const RUNTIME_KEY = 'hzy-admin-runtime';
const SESSION_KEY = 'hzy-admin-session';

const defaultRuntime = {
  baseUrl: 'http://localhost:8080',
};

function readStorage(key, fallback) {
  if (typeof window === 'undefined') {
    return fallback;
  }

  try {
    const raw = window.localStorage.getItem(key);
    return raw ? { ...fallback, ...JSON.parse(raw) } : fallback;
  } catch (error) {
    return fallback;
  }
}

function writeStorage(key, value) {
  if (typeof window === 'undefined') {
    return;
  }
  window.localStorage.setItem(key, JSON.stringify(value));
}

function normalizeAmount(value) {
  return typeof value === 'number' ? value : Number(value || 0);
}

/**
 * 读取后台运行环境配置。
 */
export function getRuntimeConfig() {
  return readStorage(RUNTIME_KEY, defaultRuntime);
}

/**
 * 更新后台运行环境配置。
 */
export function setRuntimeConfig(payload) {
  const next = {
    ...getRuntimeConfig(),
    ...payload,
  };
  writeStorage(RUNTIME_KEY, next);
  return next;
}

/**
 * 构造后台 WebSocket 地址。
 */
export function buildAdminWsUrl(path) {
  const runtime = getRuntimeConfig();
  return `${runtime.baseUrl.replace(/^http/, 'ws')}${path}`;
}

/**
 * 获取后台登录会话。
 */
export function getAdminSession() {
  return readStorage(SESSION_KEY, {
    token: '',
    refreshToken: '',
    profile: null,
  });
}

/**
 * 保存后台登录会话。
 */
export function saveAdminSession(session) {
  writeStorage(SESSION_KEY, session);
}

/**
 * 清除后台登录会话。
 */
export function clearAdminSession() {
  if (typeof window !== 'undefined') {
    window.localStorage.removeItem(SESSION_KEY);
  }
}

async function refreshAccessToken(refreshToken) {
  const runtime = getRuntimeConfig();
  const response = await fetch(`${runtime.baseUrl}/api/auth/refresh`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({ refreshToken }),
  });
  const payload = await response.json().catch(() => null);
  if (!response.ok || !payload?.success) {
    clearAdminSession();
    throw new Error(payload?.message || '登录已过期');
  }

  const session = getAdminSession();
  saveAdminSession({
    ...session,
    token: payload.data.token,
    refreshToken: payload.data.refreshToken,
  });
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
    throw new Error(payload?.message || `请求失败：${response.status}`);
  }

  return payload;
}

/**
 * 对外暴露统一请求方法。
 */
export async function request(options) {
  return rawRequest(options);
}

/**
 * 后台登录。
 */
export async function loginAsAdmin() {
  const payload = await rawRequest({
    url: '/api/auth/sms-login',
    method: 'POST',
    data: { role: 'admin' },
    auth: false,
  });
  return payload.data;
}

/**
 * 获取后台仪表盘数据。
 */
export async function fetchAdminDashboard() {
  return (await rawRequest({ url: '/api/admin/dashboard' })).data;
}

/**
 * 获取调度中心列表。
 */
export async function fetchAdminDispatch() {
  return (await rawRequest({ url: '/api/admin/dispatch' })).data.map((item) => ({
    ...item,
    amount: normalizeAmount(item.amount),
  }));
}

/**
 * 强制派单。
 */
export async function forceAssignDispatch(taskId, masterName = '张师傅') {
  return (
    await rawRequest({
      url: `/api/dispatch/tasks/${taskId}/force-assign`,
      method: 'POST',
      data: { masterName },
    })
  ).data;
}

/**
 * 获取订单管理列表。
 */
export async function fetchAdminOrders() {
  return (await rawRequest({ url: '/api/admin/orders' })).data.map((item) => ({
    ...item,
    amount: normalizeAmount(item.amount),
  }));
}

/**
 * 获取师傅管理列表。
 */
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

/**
 * 获取财务视图。
 */
export async function fetchAdminFinance() {
  return (await rawRequest({ url: '/api/admin/finance' })).data.map((item) => ({
    ...item,
    amount: normalizeAmount(item.amount),
  }));
}

/**
 * 获取仲裁列表。
 */
export async function fetchAdminArbitrations() {
  return (await rawRequest({ url: '/api/admin/arbitrations' })).data;
}

/**
 * 获取后台定价规则视图数据。
 */
export async function fetchAdminPricing() {
  return fetchCrudList('/api/admin/pricing/rules');
}

/**
 * 查询任意配置类资源列表。
 */
export async function fetchCrudList(resource) {
  return (await rawRequest({ url: resource })).data;
}

/**
 * 查询任意配置类资源详情。
 */
export async function fetchCrudDetail(resource, id) {
  return (await rawRequest({ url: `${resource}/${id}` })).data;
}

/**
 * 新增任意配置类资源。
 */
export async function createCrudItem(resource, payload) {
  return (await rawRequest({ url: resource, method: 'POST', data: payload })).data;
}

/**
 * 更新任意配置类资源。
 */
export async function updateCrudItem(resource, id, payload) {
  return (await rawRequest({ url: `${resource}/${id}`, method: 'PUT', data: payload })).data;
}

/**
 * 删除任意配置类资源。
 */
export async function deleteCrudItem(resource, id) {
  return (await rawRequest({ url: `${resource}/${id}`, method: 'DELETE' })).data;
}
