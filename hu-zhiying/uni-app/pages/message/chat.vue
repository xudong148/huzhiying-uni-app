<template>
  <view class="page-container chat-page">
    <view class="chat-page__network" :class="`chat-page__network--${socketState}`">
      {{ socketStateText }}
    </view>

    <scroll-view
      scroll-y
      class="chat-page__scroll"
      :show-scrollbar="false"
      :scroll-into-view="scrollIntoView"
    >
      <view class="page-shell">
        <view
          v-for="item in messages"
          :key="item.id"
          :id="`message-${item.id}`"
          class="chat-page__row"
          :class="messageRowClass(item)"
        >
          <template v-if="item.type === 'image'">
            <image class="chat-page__image" :src="item.content" mode="widthFix" @tap="previewImage(item.content)" />
          </template>
          <template v-else-if="item.type === 'audio'">
            <view class="chat-page__bubble chat-page__bubble--audio">
              <audio :src="item.content" controls></audio>
            </view>
          </template>
          <template v-else>
            <view class="chat-page__bubble">{{ item.content }}</view>
          </template>
          <view class="chat-page__time">{{ item.time }}</view>
        </view>
      </view>
      <view class="safe-bottom"></view>
    </scroll-view>

    <view class="chat-page__input">
      <view class="chat-page__toolbar">
        <view class="chip" :class="{ 'chat-page__chip--loading': uploadingImage }" @tap="safeSendImage">图片</view>
        <view class="chip" :class="{ 'chat-page__chip--loading': uploadingAudio }" @tap="safeSendAudio">语音</view>
        <view class="chip" @tap="safeUrgeByChat">催单</view>
      </view>
      <view class="chat-page__composer">
        <input v-model="draft" placeholder="请输入消息内容" />
        <button class="primary-btn chat-page__send" :loading="sending" @tap="safeSendText">发送</button>
      </view>
    </view>
  </view>
</template>

<script setup>
import { computed, nextTick, ref } from 'vue';
import { onHide, onLoad, onShow, onUnload } from '@dcloudio/uni-app';
import { uploadMedia } from '../../api/file';
import { urgeOrder } from '../../api/order';
import {
  getChatMessages,
  getMessageSessions,
  markChatSessionRead,
  sendChatMessage,
} from '../../api/user';
import { useUserStore } from '../../stores/user';
import { safeAsync } from '../../utils/page-task';
import { setPendingChatTarget, syncMessageBadgeBySessions } from '../../utils/message-center';
import { buildWsUrl } from '../../utils/request';

const store = useUserStore();
const draft = ref('');
const sending = ref(false);
const uploadingImage = ref(false);
const uploadingAudio = ref(false);
const scrollIntoView = ref('');
const sessionId = ref('MS-001');
const orderId = ref('');
const messages = ref([]);
const pageVisible = ref(false);
const socketState = ref('offline');

let socketTask = null;
let reconnectTimer = null;
let sessionRefreshTimer = null;

const socketStateText = computed(() => {
  if (socketState.value === 'online') {
    return '实时消息已连接';
  }
  if (socketState.value === 'connecting') {
    return '连接中断，正在重连';
  }
  return '当前处于离线兜底模式，消息会在刷新后同步';
});

function normalizeSenderCode() {
  return store.role === 'master' ? 'master' : 'user';
}

function messageRowClass(item) {
  return `chat-page__row--${item.sender || 'system'}`;
}

async function scrollToBottom() {
  await nextTick();
  const last = messages.value[messages.value.length - 1];
  if (last) {
    scrollIntoView.value = `message-${last.id}`;
  }
}

function previewImage(url) {
  uni.previewImage({
    urls: messages.value.filter((item) => item.type === 'image').map((item) => item.content),
    current: url,
  });
}

async function syncSessionBadge() {
  const res = await getMessageSessions();
  syncMessageBadgeBySessions(res.data || []);
}

async function markCurrentSessionRead() {
  if (!sessionId.value) {
    return;
  }
  await markChatSessionRead(sessionId.value);
  await syncSessionBadge();
}

async function loadMessages() {
  const res = await getChatMessages(sessionId.value);
  messages.value = res.data || [];
  await scrollToBottom();
  await markCurrentSessionRead();
}

const safeLoadMessages = safeAsync(loadMessages, '加载聊天记录');

function clearTimers() {
  if (reconnectTimer) {
    clearTimeout(reconnectTimer);
    reconnectTimer = null;
  }
  if (sessionRefreshTimer) {
    clearTimeout(sessionRefreshTimer);
    sessionRefreshTimer = null;
  }
}

function queueSessionBadgeRefresh() {
  if (sessionRefreshTimer) {
    return;
  }
  sessionRefreshTimer = setTimeout(async () => {
    sessionRefreshTimer = null;
    await safeAsync(syncSessionBadge, '同步消息角标')();
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
      if (payload.event !== 'chat_message') {
        return;
      }

      const incomingSessionId = payload.payload?.sessionId;
      if (!incomingSessionId) {
        return;
      }

      if (incomingSessionId !== sessionId.value) {
        queueSessionBadgeRefresh();
        return;
      }

      const message = payload.payload.message;
      if (!message || messages.value.some((item) => item.id === message.id)) {
        queueSessionBadgeRefresh();
        return;
      }

      messages.value.push({
        id: message.id,
        sender: message.sender || 'system',
        type: message.type || 'text',
        content: message.content,
        time: message.time || '刚刚',
      });
      scrollToBottom();
      safeAsync(markCurrentSessionRead, '同步会话已读')();
    } catch (error) {
      console.log('聊天消息解析失败', error);
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

async function submitMessage(payload) {
  const res = await sendChatMessage(sessionId.value, payload);
  if (res.data && !messages.value.some((item) => item.id === res.data.id)) {
    messages.value.push(res.data);
  }
  await scrollToBottom();
  await markCurrentSessionRead();
}

async function sendText() {
  if (!draft.value.trim()) {
    return;
  }
  sending.value = true;
  try {
    await submitMessage({
      senderCode: normalizeSenderCode(),
      messageType: 'text',
      content: draft.value.trim(),
    });
    draft.value = '';
  } finally {
    sending.value = false;
  }
}

async function sendImage() {
  const chooseRes = await uni.chooseMedia({
    count: 1,
    mediaType: ['image'],
    sourceType: ['album', 'camera'],
  });
  const target = chooseRes.tempFiles?.[0];
  if (!target) {
    return;
  }

  uploadingImage.value = true;
  try {
    const fileRes = await uploadMedia(target.tempFilePath, 'chat_message', sessionId.value);
    await submitMessage({
      senderCode: normalizeSenderCode(),
      messageType: 'image',
      content: fileRes.data.url,
    });
  } finally {
    uploadingImage.value = false;
  }
}

async function sendAudio() {
  const chooseRes = await uni.chooseMessageFile({
    count: 1,
    type: 'file',
  });
  const target = chooseRes.tempFiles?.[0];
  const filePath = target?.path || target?.tempFilePath;
  if (!filePath) {
    return;
  }

  uploadingAudio.value = true;
  try {
    const fileRes = await uploadMedia(filePath, 'chat_message', sessionId.value);
    await submitMessage({
      senderCode: normalizeSenderCode(),
      messageType: 'audio',
      content: fileRes.data.url,
    });
  } finally {
    uploadingAudio.value = false;
  }
}

async function urgeByChat() {
  if (!orderId.value) {
    uni.showToast({ title: '当前会话未关联订单', icon: 'none' });
    return;
  }

  await urgeOrder(orderId.value, '用户通过聊天窗口发起催单');
  uni.showToast({ title: '已提醒平台处理', icon: 'none' });
}

const safeSendText = safeAsync(sendText, '发送文本消息');
const safeSendImage = safeAsync(sendImage, '发送图片消息');
const safeSendAudio = safeAsync(sendAudio, '发送语音消息');
const safeUrgeByChat = safeAsync(urgeByChat, '聊天催单');

onLoad((options) => {
  sessionId.value = options.sessionId || uni.getStorageSync('hzy-chat-session') || 'MS-001';
  orderId.value = options.orderId || uni.getStorageSync('hzy-chat-order-id') || '';
  setPendingChatTarget({
    sessionId: sessionId.value,
    orderId: orderId.value,
    autoOpen: false,
  });
});

onShow(safeAsync(async () => {
  pageVisible.value = true;
  await safeLoadMessages();
  connectSocket();
}, '初始化聊天页'));

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
.chat-page {
  background: #f4f6f9;
}

.chat-page__network {
  padding: 14rpx 24rpx;
  font-size: 22rpx;
  color: #8b90a0;
  background: rgba(255, 255, 255, 0.9);
}

.chat-page__network--online {
  color: #00b578;
}

.chat-page__network--connecting {
  color: #ff7d00;
}

.chat-page__scroll {
  height: calc(100vh - 228rpx);
}

.chat-page__row {
  margin-bottom: 18rpx;
}

.chat-page__row--system {
  text-align: center;
}

.chat-page__row--system .chat-page__bubble {
  display: inline-block;
  background: #eef2ff;
}

.chat-page__row--master .chat-page__bubble,
.chat-page__row--master .chat-page__image {
  background: #ffffff;
}

.chat-page__row--user {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
}

.chat-page__row--user .chat-page__bubble {
  background: #2b5cff;
  color: #ffffff;
}

.chat-page__bubble {
  max-width: 82%;
  padding: 20rpx 22rpx;
  border-radius: 24rpx;
  font-size: 26rpx;
  line-height: 1.7;
  background: #ffffff;
}

.chat-page__bubble--audio {
  width: 360rpx;
}

.chat-page__image {
  max-width: 360rpx;
  border-radius: 24rpx;
}

.chat-page__time {
  margin-top: 8rpx;
  font-size: 20rpx;
  color: #8b90a0;
}

.chat-page__input {
  position: fixed;
  left: 0;
  right: 0;
  bottom: 0;
  padding: 16rpx 24rpx calc(16rpx + env(safe-area-inset-bottom));
  background: rgba(255, 255, 255, 0.96);
}

.chat-page__toolbar {
  display: flex;
  gap: 10rpx;
  margin-bottom: 12rpx;
}

.chat-page__chip--loading {
  opacity: 0.6;
}

.chat-page__composer {
  display: flex;
  align-items: center;
  gap: 12rpx;
}

.chat-page__composer input {
  flex: 1;
  height: 84rpx;
  padding: 0 24rpx;
  border-radius: 24rpx;
  background: #f4f6f9;
  font-size: 26rpx;
}

.chat-page__send {
  width: 180rpx;
}
</style>
