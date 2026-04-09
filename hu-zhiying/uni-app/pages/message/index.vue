<template>
  <view class="page-shell message-center">
    <view class="card message-center__hero">
      <view>
        <view class="message-center__title">消息中心</view>
        <view class="message-center__subtitle">订单沟通、施工进度和客服回复都会在这里同步</view>
      </view>
      <view class="message-center__status" :class="`message-center__status--${socketState}`">
        {{ socketStateText }}
      </view>
    </view>

    <view class="section-title message-center__section">
      <text class="section-title__text">会话列表</text>
      <text class="section-title__desc" @tap="safeLoadSessions">刷新</text>
    </view>

    <view v-if="!loading && !sessions.length" class="card message-center__empty">
      <view class="message-center__empty-title">暂时没有消息</view>
      <view class="message-center__empty-desc">有新的订单沟通后，这里会自动出现会话。</view>
    </view>

    <view
      v-for="item in sessions"
      :key="item.sessionId"
      class="card message-center__item"
      @tap="openSession(item)"
    >
      <view class="message-center__item-head">
        <view class="message-center__item-main">
          <text class="message-center__item-title">{{ item.title }}</text>
          <text class="message-center__item-participant">{{ item.participant }}</text>
        </view>
        <view class="message-center__item-side">
          <text class="message-center__item-time">{{ item.latestTime || '刚刚' }}</text>
          <view v-if="item.unreadCount" class="message-center__item-badge">
            {{ item.unreadCount > 99 ? '99+' : item.unreadCount }}
          </view>
        </view>
      </view>
      <view class="message-center__item-preview">{{ item.latestMessage || '点击进入会话' }}</view>
      <view class="message-center__item-foot">
        <text>共 {{ item.messageCount }} 条消息</text>
        <text v-if="item.orderId">订单 {{ item.orderId }}</text>
      </view>
    </view>
  </view>
</template>

<script setup>
import { computed, ref } from 'vue';
import { onHide, onShow, onUnload } from '@dcloudio/uni-app';
import { getMessageSessions } from '../../api/user';
import { safeAsync } from '../../utils/page-task';
import { buildWsUrl } from '../../utils/request';
import {
  consumePendingChatAutoOpen,
  setPendingChatTarget,
  syncMessageBadgeBySessions,
} from '../../utils/message-center';

const sessions = ref([]);
const loading = ref(false);
const pageVisible = ref(false);
const socketState = ref('offline');

let socketTask = null;
let reconnectTimer = null;
let refreshTimer = null;

const socketStateText = computed(() => {
  if (socketState.value === 'online') {
    return '实时同步中';
  }
  if (socketState.value === 'connecting') {
    return '正在重连';
  }
  return '离线兜底';
});

async function loadSessions() {
  loading.value = true;
  try {
    const res = await getMessageSessions();
    sessions.value = res.data || [];
    syncMessageBadgeBySessions(sessions.value);
  } finally {
    loading.value = false;
  }
}

const safeLoadSessions = safeAsync(loadSessions, '加载消息列表');

function openSession(item) {
  setPendingChatTarget({
    sessionId: item.sessionId,
    orderId: item.orderId,
    autoOpen: false,
  });
  uni.navigateTo({
    url: `/pages/message/chat?sessionId=${encodeURIComponent(item.sessionId)}&orderId=${encodeURIComponent(item.orderId || '')}`,
  });
}

function clearTimers() {
  if (reconnectTimer) {
    clearTimeout(reconnectTimer);
    reconnectTimer = null;
  }
  if (refreshTimer) {
    clearTimeout(refreshTimer);
    refreshTimer = null;
  }
}

function scheduleRefresh() {
  if (refreshTimer) {
    return;
  }
  refreshTimer = setTimeout(async () => {
    refreshTimer = null;
    await safeLoadSessions();
  }, 180);
}

function scheduleReconnect() {
  if (!pageVisible.value || socketTask) {
    return;
  }
  socketState.value = 'connecting';
  if (reconnectTimer) {
    return;
  }
  reconnectTimer = setTimeout(() => {
    reconnectTimer = null;
    connectSocket();
  }, 1200);
}

function closeSocket() {
  clearTimers();
  socketState.value = 'offline';
  if (socketTask) {
    socketTask.close({});
    socketTask = null;
  }
}

function connectSocket() {
  if (socketTask || !pageVisible.value) {
    return;
  }
  socketState.value = 'connecting';
  socketTask = uni.connectSocket({
    url: buildWsUrl('/ws/chat'),
  });

  socketTask.onOpen(() => {
    socketState.value = 'online';
    socketTask?.send({ data: 'ping' });
  });

  socketTask.onMessage((event) => {
    try {
      const payload = JSON.parse(event.data || '{}');
      if (payload.event === 'chat_message') {
        scheduleRefresh();
      }
    } catch (error) {
      console.log('消息中心事件解析失败', error);
    }
  });

  socketTask.onClose(() => {
    socketTask = null;
    scheduleReconnect();
  });

  socketTask.onError(() => {
    socketTask = null;
    scheduleReconnect();
  });
}

async function maybeAutoOpenChat() {
  const target = consumePendingChatAutoOpen();
  if (!target?.sessionId) {
    return;
  }
  openSession(target);
}

onShow(safeAsync(async () => {
  pageVisible.value = true;
  await safeLoadSessions();
  await maybeAutoOpenChat();
  connectSocket();
}, '初始化消息中心'));

onHide(() => {
  pageVisible.value = false;
  closeSocket();
});

onUnload(() => {
  pageVisible.value = false;
  closeSocket();
});
</script>

<style scoped>
.message-center {
  padding-bottom: calc(36rpx + env(safe-area-inset-bottom));
}

.message-center__hero {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 24rpx;
  padding: 28rpx;
  background: linear-gradient(135deg, #1c1e26 0%, #2b5cff 100%);
  color: #fff;
}

.message-center__title {
  font-size: 34rpx;
  font-weight: 800;
}

.message-center__subtitle {
  margin-top: 10rpx;
  font-size: 22rpx;
  line-height: 1.6;
  color: rgba(255, 255, 255, 0.78);
}

.message-center__status {
  flex-shrink: 0;
  padding: 10rpx 18rpx;
  border-radius: 999rpx;
  font-size: 22rpx;
  background: rgba(255, 255, 255, 0.14);
}

.message-center__status--online {
  background: rgba(0, 181, 120, 0.18);
}

.message-center__status--connecting {
  background: rgba(255, 125, 0, 0.18);
}

.message-center__section {
  margin-top: 28rpx;
}

.message-center__empty {
  padding: 42rpx 28rpx;
  text-align: center;
}

.message-center__empty-title {
  font-size: 30rpx;
  font-weight: 700;
  color: #1c1e26;
}

.message-center__empty-desc {
  margin-top: 12rpx;
  font-size: 24rpx;
  line-height: 1.6;
  color: #8b90a0;
}

.message-center__item {
  margin-top: 18rpx;
  padding: 24rpx 28rpx;
}

.message-center__item-head {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 20rpx;
}

.message-center__item-main {
  display: flex;
  flex-direction: column;
  gap: 10rpx;
  min-width: 0;
}

.message-center__item-title {
  font-size: 30rpx;
  font-weight: 700;
  color: #1c1e26;
}

.message-center__item-participant {
  font-size: 22rpx;
  color: #8b90a0;
}

.message-center__item-side {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  gap: 10rpx;
  flex-shrink: 0;
}

.message-center__item-time {
  font-size: 22rpx;
  color: #8b90a0;
}

.message-center__item-badge {
  min-width: 36rpx;
  padding: 4rpx 10rpx;
  border-radius: 999rpx;
  background: #ff4a4a;
  font-size: 20rpx;
  text-align: center;
  color: #fff;
}

.message-center__item-preview {
  margin-top: 18rpx;
  font-size: 26rpx;
  line-height: 1.7;
  color: #475467;
}

.message-center__item-foot {
  display: flex;
  justify-content: space-between;
  margin-top: 18rpx;
  font-size: 22rpx;
  color: #8b90a0;
}
</style>
