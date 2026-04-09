import { useUserStore } from '../stores/user';

const RUNTIME_KEY = 'hzy-runtime-config';
const TRACE_ID_KEY = 'hzy-last-trace-id';
const RETRYABLE_METHODS = new Set(['GET', 'HEAD', 'OPTIONS']);
const RETRYABLE_STATUS = new Set([408, 429, 500, 502, 503, 504]);

let refreshSessionPromise = null;
let loginRedirecting = false;

function normalizeBaseUrl(baseUrl) {
  const raw = String(baseUrl || '').trim().replace(/\/+$/, '');
  if (!raw) {
    return '';
  }
  return /^https?:\/\//.test(raw) ? raw : `http://${raw}`;
}

function resolveEnvBaseUrl() {
  if (typeof process === 'undefined' || !process?.env) {
    return '';
  }
  return normalizeBaseUrl(
    process.env.UNI_APP_API_BASE_URL
      || process.env.VITE_API_BASE_URL
      || process.env.HZY_API_BASE_URL
      || '',
  );
}

function isNativeLikeRuntime() {
  // #ifdef H5
  return false;
  // #endif
  return true;
}

function getHostName(baseUrl = '') {
  return normalizeBaseUrl(baseUrl)
    .replace(/^https?:\/\//, '')
    .split('/')[0]
    .split(':')[0]
    .trim()
    .toLowerCase();
}

function isLocalhostBaseUrl(baseUrl = '') {
  return ['localhost', '127.0.0.1', '::1'].includes(getHostName(baseUrl));
}

const DEFAULT_RUNTIME = {
  baseUrl: resolveEnvBaseUrl() || 'http://127.0.0.1:8080',
  requestTimeout: 15000,
};

function resolveRuntimeConfig(payload = {}) {
  return {
    ...DEFAULT_RUNTIME,
    ...(payload || {}),
    baseUrl: normalizeBaseUrl(payload.baseUrl || DEFAULT_RUNTIME.baseUrl),
    requestTimeout: Number(payload.requestTimeout || DEFAULT_RUNTIME.requestTimeout),
  };
}

export function getRuntimeDiagnostics(baseUrl) {
  const resolvedBaseUrl = normalizeBaseUrl(baseUrl) || getRuntimeConfig().baseUrl;
  const localhost = isLocalhostBaseUrl(resolvedBaseUrl);
  const needsLanIp = isNativeLikeRuntime() && localhost;

  return {
    baseUrl: resolvedBaseUrl,
    isLocalhost: localhost,
    needsLanIp,
    hint: needsLanIp
      ? `当前地址是 ${resolvedBaseUrl}。真机或模拟器不能访问 localhost，请改成电脑局域网 IP，例如 http://192.168.1.10:8080`
      : `当前后端地址：${resolvedBaseUrl}`,
  };
}

function getNetworkErrorMessage() {
  const diagnostics = getRuntimeDiagnostics();
  if (diagnostics.needsLanIp) {
    return `当前后端地址是 ${diagnostics.baseUrl}，真机或模拟器请改成电脑局域网 IP`;
  }
  return '网络请求失败';
}

function normalizeRequestMethod(method = 'GET') {
  return String(method || 'GET').toUpperCase();
}

function sleep(duration = 0) {
  return new Promise((resolve) => {
    setTimeout(resolve, duration);
  });
}

function readHeaderValue(headers = {}, headerName = '') {
  if (!headers || !headerName) {
    return '';
  }
  const lowerName = headerName.toLowerCase();
  const matchedKey = Object.keys(headers).find((key) => key.toLowerCase() === lowerName);
  return matchedKey ? headers[matchedKey] : '';
}

function resolveTraceId(payload, headers = {}) {
  if (typeof payload?.traceId === 'string' && payload.traceId) {
    return payload.traceId;
  }
  if (typeof payload?.data?.traceId === 'string' && payload.data.traceId) {
    return payload.data.traceId;
  }
  return readHeaderValue(headers, 'x-trace-id') || '';
}

function setLastTraceId(traceId = '') {
  if (!traceId) {
    return '';
  }
  try {
    uni.setStorageSync(TRACE_ID_KEY, traceId);
  } catch (error) {
    return traceId;
  }
  return traceId;
}

export function getLastTraceId() {
  try {
    return uni.getStorageSync(TRACE_ID_KEY) || '';
  } catch (error) {
    return '';
  }
}

function attachTraceMeta(target, traceId) {
  const resolvedTraceId = traceId || getLastTraceId();
  if (!resolvedTraceId || !target || typeof target !== 'object') {
    return target;
  }
  if (!target.traceId) {
    target.traceId = resolvedTraceId;
  }
  return target;
}

function markToastShown(target) {
  if (target && typeof target === 'object') {
    target.toastShown = true;
  }
  return target;
}

function resolveRetryCount(method, retry) {
  if (typeof retry === 'number') {
    return Math.max(0, retry);
  }
  return RETRYABLE_METHODS.has(method) ? 1 : 0;
}

function shouldRetryRequest({ method, attempt, retryCount, requestError, statusCode }) {
  if (attempt >= retryCount) {
    return false;
  }
  if (requestError) {
    return true;
  }
  return RETRYABLE_METHODS.has(method) && RETRYABLE_STATUS.has(Number(statusCode || 0));
}

function ensureErrorObject(error, fallback) {
  if (error && typeof error === 'object') {
    return error;
  }
  return new Error(getRequestErrorMessage(error, fallback));
}

export function getRequestErrorMessage(error, fallback = '请求失败') {
  if (!error) {
    return fallback;
  }
  if (typeof error === 'string') {
    return error;
  }
  if (typeof error.errorMessage === 'string' && error.errorMessage) {
    return error.errorMessage;
  }
  if (typeof error.data?.message === 'string' && error.data.message) {
    return error.data.message;
  }
  if (typeof error.message === 'string' && error.message) {
    return error.message;
  }
  if (typeof error.errMsg === 'string' && error.errMsg) {
    return error.errMsg;
  }
  return fallback;
}

export function getRuntimeConfig() {
  try {
    return resolveRuntimeConfig(uni.getStorageSync(RUNTIME_KEY) || {});
  } catch (error) {
    return resolveRuntimeConfig();
  }
}

export function setRuntimeConfig(payload) {
  const next = resolveRuntimeConfig({
    ...getRuntimeConfig(),
    ...(payload || {}),
  });
  uni.setStorageSync(RUNTIME_KEY, next);
  return next;
}

export function buildAbsoluteUrl(url) {
  if (!url) {
    return getRuntimeConfig().baseUrl;
  }
  if (/^https?:\/\//.test(url)) {
    return url;
  }
  const runtime = getRuntimeConfig();
  return `${runtime.baseUrl}${url}`;
}

export function buildWsUrl(url) {
  if (/^wss?:\/\//.test(url)) {
    return url;
  }
  const runtime = getRuntimeConfig();
  const wsBase = runtime.baseUrl.replace(/^http/, 'ws');
  return `${wsBase}${url}`;
}

function redirectToLogin() {
  const pages = getCurrentPages();
  const current = pages[pages.length - 1];
  if (loginRedirecting || current?.route === 'pages/auth/login') {
    return;
  }

  loginRedirecting = true;
  const redirect = current?.route ? `/${current.route}` : '/pages/index/index';
  uni.navigateTo({
    url: `/pages/auth/login?redirect=${encodeURIComponent(redirect)}`,
    complete() {
      setTimeout(() => {
        loginRedirecting = false;
      }, 300);
    },
  });
}

async function refreshSession(userStore) {
  if (refreshSessionPromise) {
    return refreshSessionPromise;
  }

  refreshSessionPromise = (async () => {
    const [requestError, response] = await uni.request({
      url: buildAbsoluteUrl('/api/auth/refresh'),
      method: 'POST',
      data: {
        refreshToken: userStore.refreshToken,
      },
    });

    const traceId = setLastTraceId(resolveTraceId(response?.data, response?.header));

    if (requestError || !response) {
      const error = attachTraceMeta(ensureErrorObject(requestError, getNetworkErrorMessage()), traceId);
      error.errorMessage = getRequestErrorMessage(error, getNetworkErrorMessage());
      userStore.logout();
      redirectToLogin();
      throw error;
    }

    if (response.statusCode >= 400 || !response?.data?.data?.token) {
      attachTraceMeta(response, traceId);
      response.errorMessage = response.data?.message || '登录刷新失败';
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
  })().finally(() => {
    refreshSessionPromise = null;
  });

  return refreshSessionPromise;
}

export async function request(options) {
  const userStore = useUserStore();
  const runtime = getRuntimeConfig();
  const requestOptions = {
    method: 'GET',
    ...(options || {}),
  };
  const method = normalizeRequestMethod(requestOptions.method);
  const retryCount = resolveRetryCount(method, requestOptions.retry);
  const retryDelay = Number(requestOptions.retryDelay || 600);
  const showErrorToast = requestOptions.showErrorToast !== false;
  const skipAuthRefresh = requestOptions.skipAuthRefresh === true;

  delete requestOptions.retry;
  delete requestOptions.retryDelay;
  delete requestOptions.showErrorToast;
  delete requestOptions.skipAuthRefresh;

  const run = async (attempt = 0) => {
    const [requestError, response] = await uni.request({
      ...requestOptions,
      method,
      url: buildAbsoluteUrl(requestOptions.url),
      timeout: requestOptions.timeout || runtime.requestTimeout,
      header: {
        'Content-Type': 'application/json',
        ...(requestOptions.header || {}),
        Authorization: userStore.token ? `Bearer ${userStore.token}` : '',
      },
    });

    const traceId = setLastTraceId(resolveTraceId(response?.data, response?.header));

    if (requestError || !response) {
      const error = attachTraceMeta(ensureErrorObject(requestError, getNetworkErrorMessage()), traceId);
      error.errorMessage = getRequestErrorMessage(error, getNetworkErrorMessage());
      if (shouldRetryRequest({ method, attempt, retryCount, requestError: error })) {
        await sleep(retryDelay * (attempt + 1));
        return run(attempt + 1);
      }
      if (showErrorToast && !error.toastShown) {
        uni.showToast({ title: error.errorMessage, icon: 'none' });
        markToastShown(error);
      }
      throw error;
    }

    attachTraceMeta(response, traceId);
    attachTraceMeta(response.data, traceId);

    if (response.statusCode === 401 && userStore.refreshToken && !skipAuthRefresh) {
      await refreshSession(userStore);
      return run(attempt);
    }

    if (response.statusCode === 401) {
      userStore.logout();
      redirectToLogin();
      response.errorMessage = response.data?.message || '登录状态已失效，请重新登录';
      if (showErrorToast && !response.toastShown) {
        uni.showToast({ title: response.errorMessage, icon: 'none' });
        markToastShown(response);
      }
      throw response;
    }

    if (response.statusCode >= 400 || response.data?.success === false) {
      response.errorMessage = response.data?.message || '请求失败';
      if (shouldRetryRequest({ method, attempt, retryCount, statusCode: response.statusCode })) {
        await sleep(retryDelay * (attempt + 1));
        return run(attempt + 1);
      }
      if (showErrorToast && !response.toastShown) {
        uni.showToast({
          title: response.errorMessage,
          icon: 'none',
        });
        markToastShown(response);
      }
      throw response;
    }

    return attachTraceMeta(response.data, traceId);
  };

  return run();
}

export function uploadFile(filePath, formData = {}, options = {}) {
  const userStore = useUserStore();
  const runtime = getRuntimeConfig();
  const retryCount = typeof options.retry === 'number' ? Math.max(0, options.retry) : 1;
  const retryDelay = Number(options.retryDelay || 800);
  const showErrorToast = options.showErrorToast !== false;

  return new Promise((resolve, reject) => {
    const run = (attempt = 0) => {
      uni.uploadFile({
        url: buildAbsoluteUrl('/api/files/upload'),
        timeout: runtime.requestTimeout,
        filePath,
        name: 'file',
        formData,
        header: {
          Authorization: userStore.token ? `Bearer ${userStore.token}` : '',
        },
        success(response) {
          let payload = response.data;
          if (typeof response.data === 'string') {
            try {
              payload = JSON.parse(response.data);
            } catch (error) {
              payload = {
                success: false,
                message: '上传返回格式异常',
              };
            }
          }

          const traceId = setLastTraceId(resolveTraceId(payload, response?.header));
          attachTraceMeta(payload, traceId);

          if (response.statusCode >= 400 || payload?.success === false) {
            payload.errorMessage = payload?.message || '上传失败';
            if (attempt < retryCount && RETRYABLE_STATUS.has(Number(response.statusCode || 0))) {
              setTimeout(() => run(attempt + 1), retryDelay * (attempt + 1));
              return;
            }
            if (showErrorToast && !payload.toastShown) {
              uni.showToast({
                title: payload.errorMessage,
                icon: 'none',
              });
              markToastShown(payload);
            }
            reject(payload);
            return;
          }

          resolve(payload);
        },
        fail(error) {
          const nextError = attachTraceMeta(ensureErrorObject(error, getNetworkErrorMessage()));
          nextError.errorMessage = getRequestErrorMessage(nextError, getNetworkErrorMessage());
          if (attempt < retryCount) {
            setTimeout(() => run(attempt + 1), retryDelay * (attempt + 1));
            return;
          }
          if (showErrorToast && !nextError.toastShown) {
            uni.showToast({ title: nextError.errorMessage, icon: 'none' });
            markToastShown(nextError);
          }
          reject(nextError);
        },
      });
    };

    run();
  });
}
