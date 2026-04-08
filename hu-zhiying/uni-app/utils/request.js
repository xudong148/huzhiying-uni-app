import { useUserStore } from '../stores/user';

const RUNTIME_KEY = 'hzy-runtime-config';
const DEFAULT_RUNTIME = {
  baseUrl: 'http://localhost:8080',
  useMock: false,
};

export function getRuntimeConfig() {
  try {
    return {
      ...DEFAULT_RUNTIME,
      ...(uni.getStorageSync(RUNTIME_KEY) || {}),
    };
  } catch (error) {
    return { ...DEFAULT_RUNTIME };
  }
}

export function setRuntimeConfig(payload) {
  const next = {
    ...getRuntimeConfig(),
    ...(payload || {}),
  };
  uni.setStorageSync(RUNTIME_KEY, next);
  return next;
}

export function shouldUseMock() {
  return Boolean(getRuntimeConfig().useMock);
}

function buildUrl(url) {
  const runtime = getRuntimeConfig();
  return `${runtime.baseUrl}${url}`;
}

export function buildWsUrl(url) {
  const runtime = getRuntimeConfig();
  const wsBase = runtime.baseUrl.replace(/^http/, 'ws');
  return `${wsBase}${url}`;
}

function redirectToLogin() {
  const pages = getCurrentPages();
  const current = pages[pages.length - 1];
  const redirect = current?.route ? `/${current.route}` : '/pages/index/index';
  uni.navigateTo({
    url: `/pages/auth/login?redirect=${encodeURIComponent(redirect)}`,
  });
}

async function refreshSession(userStore) {
  const [error, response] = await uni.request({
    url: buildUrl('/api/auth/refresh'),
    method: 'POST',
    data: {
      refreshToken: userStore.refreshToken,
    },
  });

  if (error || response?.statusCode >= 400 || !response?.data?.data?.token) {
    userStore.logout();
    redirectToLogin();
    throw error || response;
  }

  userStore.login({
    token: response.data.data.token,
    refreshToken: response.data.data.refreshToken,
    role: userStore.role,
    profile: userStore.profile,
  });
}

export async function request(options) {
  const userStore = useUserStore();
  const requestOptions = {
    method: 'GET',
    ...options,
  };

  const run = async () => {
    const [error, response] = await uni.request({
      ...requestOptions,
      url: buildUrl(requestOptions.url),
      header: {
        'Content-Type': 'application/json',
        ...(requestOptions.header || {}),
        Authorization: userStore.token ? `Bearer ${userStore.token}` : '',
      },
    });

    if (error) {
      uni.showToast({
        title: '网络请求失败',
        icon: 'none',
      });
      throw error;
    }

    if (response.statusCode === 401 && userStore.refreshToken) {
      await refreshSession(userStore);
      return run();
    }

    if (response.statusCode >= 400 || response.data?.success === false) {
      uni.showToast({
        title: response.data?.message || '请求失败',
        icon: 'none',
      });
      throw response;
    }

    return response.data;
  };

  return run();
}
