import { getRequestErrorMessage } from './request';

function wasToastShown(error) {
  return Boolean(error && typeof error === 'object' && error.toastShown);
}

export function safeAsync(handler, label = '异步任务') {
  return async (...args) => {
    try {
      return await handler(...args);
    } catch (error) {
      console.warn(`${label}失败`, error);
      if (!wasToastShown(error)) {
        uni.showToast({
          title: getRequestErrorMessage(error, `${label}失败`),
          icon: 'none',
        });
        if (error && typeof error === 'object') {
          error.toastShown = true;
        }
      }
      return null;
    }
  };
}

export function showActionError(error, fallback = '操作失败') {
  if (wasToastShown(error)) {
    return;
  }
  uni.showToast({
    title: getRequestErrorMessage(error, fallback),
    icon: 'none',
  });
  if (error && typeof error === 'object') {
    error.toastShown = true;
  }
}
