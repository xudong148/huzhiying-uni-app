import {
  arbitrationRows,
  contentRows,
  dashboard,
  dispatchRows,
  financeRows,
  marketingRows,
  masterRows,
  orderRows,
  pricingRows,
  systemRows,
} from '../mock/data';

const RUNTIME_KEY = 'hzy-admin-runtime';
const SESSION_KEY = 'hzy-admin-session';

const defaultRuntime = {
  baseUrl: 'http://localhost:8080',
  useMock: false,
};

const dataset = {
  dashboard,
  dispatchRows,
  orderRows,
  masterRows,
  pricingRows,
  financeRows,
  arbitrationRows,
  marketingRows,
  contentRows,
  systemRows,
};

const clone = (payload) => JSON.parse(JSON.stringify(payload));

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

export function getAdminMock(name) {
  return Promise.resolve(clone(dataset[name]));
}

export function getRuntimeConfig() {
  return readStorage(RUNTIME_KEY, defaultRuntime);
}

export function buildAdminWsUrl(path) {
  const runtime = getRuntimeConfig();
  return `${runtime.baseUrl.replace(/^http/, 'ws')}${path}`;
}

export function setRuntimeConfig(payload) {
  const next = {
    ...getRuntimeConfig(),
    ...payload,
  };
  writeStorage(RUNTIME_KEY, next);
  return next;
}

export function getAdminSession() {
  return readStorage(SESSION_KEY, {
    token: '',
    refreshToken: '',
    profile: null,
  });
}

export function saveAdminSession(session) {
  writeStorage(SESSION_KEY, session);
}

export function clearAdminSession() {
  if (typeof window !== 'undefined') {
    window.localStorage.removeItem(SESSION_KEY);
  }
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

export async function request(options) {
  return rawRequest(options);
}

export async function loginAsAdmin() {
  if (getRuntimeConfig().useMock) {
    return {
      token: `admin-token-${Date.now()}`,
      refreshToken: `admin-refresh-${Date.now()}`,
      role: 'admin',
    };
  }

  const payload = await rawRequest({
    url: '/api/auth/sms-login',
    method: 'POST',
    data: { role: 'admin' },
    auth: false,
  });
  return payload.data;
}

export async function fetchAdminDashboard() {
  if (getRuntimeConfig().useMock) {
    return clone(dashboard);
  }
  return (await rawRequest({ url: '/api/admin/dashboard' })).data;
}

export async function fetchAdminDispatch() {
  if (getRuntimeConfig().useMock) {
    return clone(dispatchRows);
  }
  return (await rawRequest({ url: '/api/admin/dispatch' })).data.map((item) => ({
    ...item,
    amount: normalizeAmount(item.amount),
  }));
}

export async function forceAssignDispatch(taskId, masterName = '张师傅') {
  if (getRuntimeConfig().useMock) {
    return { taskId, masterName };
  }
  return (
    await rawRequest({
      url: `/api/dispatch/tasks/${taskId}/force-assign`,
      method: 'POST',
      data: { masterName },
    })
  ).data;
}

export async function fetchAdminOrders() {
  if (getRuntimeConfig().useMock) {
    return clone(orderRows);
  }
  return (await rawRequest({ url: '/api/admin/orders' })).data.map((item) => ({
    ...item,
    amount: normalizeAmount(item.amount),
  }));
}

export async function fetchAdminMasters() {
  if (getRuntimeConfig().useMock) {
    return clone(masterRows);
  }
  return (await rawRequest({ url: '/api/admin/masters' })).data.map((item) => ({
    name: item.realName,
    skills: item.skillTags,
    score: item.creditScore,
    status: item.online ? '在线' : '休息',
    credit: item.creditScore >= 95 ? 'A' : 'A-',
    area: item.serviceArea,
    deposit: normalizeAmount(item.deposit),
  }));
}

export async function fetchAdminPricing() {
  if (getRuntimeConfig().useMock) {
    return clone(pricingRows);
  }
  return (await rawRequest({ url: '/api/admin/pricing' })).data.map((item) => ({
    ...item,
    basePrice: normalizeAmount(item.basePrice),
  }));
}

export async function fetchAdminFinance() {
  if (getRuntimeConfig().useMock) {
    return clone(financeRows);
  }
  return (await rawRequest({ url: '/api/admin/finance' })).data.map((item) => ({
    ...item,
    amount: normalizeAmount(item.amount),
  }));
}

export async function fetchAdminArbitrations() {
  if (getRuntimeConfig().useMock) {
    return clone(arbitrationRows);
  }
  return (await rawRequest({ url: '/api/admin/arbitrations' })).data;
}
