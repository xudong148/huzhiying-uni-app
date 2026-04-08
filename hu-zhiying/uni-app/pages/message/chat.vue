<template>
  <view class="page-container chat-page">
    <scroll-view scroll-y class="chat-page__scroll" :show-scrollbar="false" :scroll-into-view="scrollIntoView">
      <view class="page-shell">
        <view v-for="item in messages" :key="item.id" :id="`message-${item.id}`" class="chat-page__row" :class="`chat-page__row--${item.type}`">
          <view class="chat-page__bubble">{{ item.content }}</view>
          <view class="chat-page__time">{{ item.time }}</view>
        </view>
      </view>
      <view class="safe-bottom"></view>
    </scroll-view>

    <view class="chat-page__input">
      <view class="chat-page__toolbar">
        <view class="chip">图片</view>
        <view class="chip">语音</view>
        <view class="chip">催单</view>
      </view>
      <view class="chat-page__composer">
        <input v-model="draft" placeholder="请输入消息内容" />
        <button class="primary-btn chat-page__send" :loading="sending" @tap="send">发送</button>
      </view>
    </view>
  </view>
</template>

<script setup>
import { nextTick, ref } from 'vue';
import { onHide, onLoad, onShow, onUnload } from '@dcloudio/uni-app';
import { sendChatMessage, getChatMessages } from '../../api/user';
import { useUserStore } from '../../stores/user';
import { buildWsUrl } from '../../utils/request';

const store = useUserStore();
const draft = ref('');
const sending = ref(false);
const scrollIntoView = ref('');
const sessionId = ref('MS-001');
const messages = ref([]);
let socketTask = null;

function normalizeSenderType() {
  return store.role === 'master' ? 'master' : 'user';
}

async function scrollToBottom() {
  await nextTick();
  const last = messages.value[messages.value.length - 1];
  if (last) {
    scrollIntoView.value = `message-${last.id}`;
  }
}

async function loadMessages() {
  const res = await getChatMessages(sessionId.value);
  messages.value = res.data;
  scrollToBottom();
}

function closeSocket() {
  if (socketTask) {
    socketTask.close({});
    socketTask = null;
  }
}

function connectSocket() {
  if (socketTask) {
    return;
  }
  socketTask = uni.connectSocket({
    url: buildWsUrl('/ws/chat'),
  });

  socketTask.onOpen(() => {
    socketTask?.send({ data: 'ping' });
  });

  socketTask.onMessage((event) => {
    try {
      const payload = JSON.parse(event.data || '{}');
      if (payload.event !== 'chat_message' || payload.payload?.sessionId !== sessionId.value) {
        return;
      }
      const message = payload.payload.message;
      if (!message || messages.value.some((item) => item.id === message.id)) {
        return;
      }
      messages.value.push({
        id: message.id,
        type: message.sender || 'system',
        content: message.content,
        time: message.time || '刚刚',
      });
      scrollToBottom();
    } catch (error) {
      // Ignore malformed frames.
    }
  });

  socketTask.onClose(() => {
    socketTask = null;
  });
}

async function send() {
  if (!draft.value.trim()) {
    return;
  }
  sending.value = true;
  try {
    const res = await sendChatMessage(sessionId.value, {
      senderCode: normalizeSenderType(),
      content: draft.value,
    });
    messages.value.push(res.data);
    draft.value = '';
    scrollToBottom();
  } finally {
    sending.value = false;
  }
}

onLoad((options) => {
  sessionId.value = options.sessionId || uni.getStorageSync('hzy-chat-session') || 'MS-001';
});

onShow(loadMessages);
onShow(connectSocket);
onHide(closeSocket);
onUnload(closeSocket);
</script>

<style scoped>
.chat-page {
  background: #f4f6f9;
}

.chat-page__scroll {
  height: calc(100vh - 178rpx);
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

.chat-page__row--master .chat-page__bubble {
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
  background: rgba(255, 255, 255, 0.94);
}

.chat-page__toolbar {
  display: flex;
  gap: 10rpx;
  margin-bottom: 12rpx;
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
