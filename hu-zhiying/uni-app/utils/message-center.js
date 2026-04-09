const CHAT_SESSION_KEY = 'hzy-chat-session';
const CHAT_SESSION_TYPE_KEY = 'hzy-chat-session-type';
const CHAT_ORDER_KEY = 'hzy-chat-order-id';
const CHAT_AUTO_OPEN_KEY = 'hzy-chat-autojump';
const MESSAGE_TAB_INDEX = 3;

export function getPendingChatTarget() {
  return {
    sessionId: uni.getStorageSync(CHAT_SESSION_KEY) || '',
    sessionType: uni.getStorageSync(CHAT_SESSION_TYPE_KEY) || 'order',
    orderId: uni.getStorageSync(CHAT_ORDER_KEY) || '',
    autoOpen: uni.getStorageSync(CHAT_AUTO_OPEN_KEY) === '1',
  };
}

export function setPendingChatTarget({ sessionId = '', sessionType = 'order', orderId = '', autoOpen = false } = {}) {
  if (sessionId) {
    uni.setStorageSync(CHAT_SESSION_KEY, sessionId);
  } else {
    uni.removeStorageSync(CHAT_SESSION_KEY);
  }

  if (sessionId) {
    uni.setStorageSync(CHAT_SESSION_TYPE_KEY, sessionType || 'order');
  } else {
    uni.removeStorageSync(CHAT_SESSION_TYPE_KEY);
  }

  if (orderId) {
    uni.setStorageSync(CHAT_ORDER_KEY, orderId);
  } else {
    uni.removeStorageSync(CHAT_ORDER_KEY);
  }

  if (autoOpen) {
    uni.setStorageSync(CHAT_AUTO_OPEN_KEY, '1');
  } else {
    uni.removeStorageSync(CHAT_AUTO_OPEN_KEY);
  }
}

export function consumePendingChatAutoOpen() {
  const target = getPendingChatTarget();
  if (!target.autoOpen || !target.sessionId) {
    return null;
  }
  uni.removeStorageSync(CHAT_AUTO_OPEN_KEY);
  return target;
}

export function syncMessageBadge(unreadCount = 0) {
  const count = Number(unreadCount || 0);
  try {
    if (count > 0) {
      uni.setTabBarBadge({
        index: MESSAGE_TAB_INDEX,
        text: count > 99 ? '99+' : String(count),
      });
      return;
    }
    uni.removeTabBarBadge({ index: MESSAGE_TAB_INDEX });
  } catch (error) {
    console.log('消息角标同步失败', error);
  }
}

export function syncMessageBadgeBySessions(sessions = []) {
  const unreadCount = sessions.reduce((sum, item) => sum + Number(item.unreadCount || 0), 0);
  syncMessageBadge(unreadCount);
  return unreadCount;
}
