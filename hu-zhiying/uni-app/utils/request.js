import { useUserStore } from '../stores/user';

const RUNTIME_KEY = 'hzy-runtime-config';
const DEFAULT_RUNTIME = {
  baseUrl: 'http://localhost:8080',
};

/**
 * 读取移动端运行配置。
 */
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

/**
 * 更新移动端运行配置。
 */
export function setRuntimeConfig(payload) {
  const next = {
    ...getRuntimeConfig(),
    ...(payload || {}),
  };
  uni.setStorageSync(RUNTIME_KEY, next);
  return next;
}

/**
 * 拼接完整接口地址。
 */
export function buildAbsoluteUrl(url) {
  const runtime = getRuntimeConfig();
  return `${runtime.baseUrl}${url}`;
}

/**
 * 构造移动端 WebSocket 地址。
 */
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
  const [, response] = await uni.request({
    url: buildAbsoluteUrl('/api/auth/refresh'),
    method: 'POST',
    data: {
      refreshToken: userStore.refreshToken,
    },
  });

  if (response?.statusCode >= 400 || !response?.data?.data?.token) {
    userStore.logout();
    redirectToLogin();
    throw response;
  }

  userStore.login({
    token: response.data.data.token,
    refreshToken: response.data.data.refreshToken,
    role: userStore.role,
    profile: userStore.profile,
  });
}

/**
 * 统一请求封装。
 * 1. 运行时只访问真实后端接口。
 * 2. 遇到 401 会尝试自动刷新一次登录态。
 */
export async function request(options) {
  const userStore = useUserStore();
  const requestOptions = {
    method: 'GET',
    ...options,
  };

  const run = async () => {
    const [, response] = await uni.request({
      ...requestOptions,
      url: buildAbsoluteUrl(requestOptions.url),
      header: {
        'Content-Type': 'application/json',
        ...(requestOptions.header || {}),
        Authorization: userStore.token ? `Bearer ${userStore.token}` : '',
      },
    });

    if (!response) {
      uni.showToast({ title: '网络请求失败', icon: 'none' });
      throw new Error('network error');
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

/**
 * 上传文件。
 */
export function uploadFile(filePath, formData = {}) {
  const userStore = useUserStore();
  return new Promise((resolve, reject) => {
    uni.uploadFile({
      url: buildAbsoluteUrl('/api/files/upload'),
      filePath,
      name: 'file',
      formData,
      header: {
        Authorization: userStore.token ? `Bearer ${userStore.token}` : '',
      },
      success(response) {
        const payload = typeof response.data === 'string' ? JSON.parse(response.data) : response.data;
        if (response.statusCode >= 400 || payload?.success === false) {
          uni.showToast({
            title: payload?.message || '上传失败',
            icon: 'none',
          });
          reject(payload);
          return;
        }
        resolve(payload);
      },
      fail(error) {
        uni.showToast({ title: '上传失败', icon: 'none' });
        reject(error);
      },
    });
  });
}
