<template>
  <view class="page-container chat-page">
    <scroll-view scroll-y class="chat-page__scroll" :show-scrollbar="false">
      <view class="page-shell">
        <view v-for="item in messages" :key="item.id" class="chat-page__row" :class="`chat-page__row--${item.type}`">
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
        <button class="primary-btn chat-page__send" @tap="send">发送</button>
      </view>
    </view>
  </view>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import { getChatMessages } from '../../api/user';

const draft = ref('');
const messages = ref([]);

function send() {
  if (!draft.value) {
    return;
  }

  messages.value.push({
    id: Date.now(),
    type: 'user',
    content: draft.value,
    time: '刚刚',
  });
  draft.value = '';
}

onMounted(async () => {
  const res = await getChatMessages();
  messages.value = res.data;
});
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
