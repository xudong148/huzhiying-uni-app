<template>
  <view class="express-card pressable" @tap="$emit('tap')">
    <view class="express-card__main">
      <view>
        <view class="express-card__title">{{ title }}</view>
        <view class="express-card__desc">{{ desc }}</view>
      </view>
      <view class="express-card__action">
        <view class="express-card__dot"></view>
        <text>一键呼叫</text>
      </view>
    </view>
  </view>
</template>

<script setup>
defineProps({
  title: {
    type: String,
    default: '极速报修',
  },
  desc: {
    type: String,
    default: '最快 30 分钟上门，支持语音、图片和快捷派单',
  },
});

defineEmits(['tap']);
</script>

<style scoped>
.express-card {
  position: relative;
  overflow: hidden;
  padding: 32rpx;
  border-radius: 28rpx;
  background: linear-gradient(135deg, #2b5cff 0%, #143bc9 100%);
  box-shadow: 0 18rpx 40rpx rgba(43, 92, 255, 0.28);
}

.express-card::before,
.express-card::after {
  content: '';
  position: absolute;
  border-radius: 50%;
  border: 1rpx solid rgba(255, 255, 255, 0.18);
  animation: pulse 2.8s infinite;
}

.express-card::before {
  width: 160rpx;
  height: 160rpx;
  right: 24rpx;
  top: -12rpx;
}

.express-card::after {
  width: 220rpx;
  height: 220rpx;
  right: -6rpx;
  top: -42rpx;
  animation-delay: 1.2s;
}

.express-card__main {
  position: relative;
  z-index: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 24rpx;
}

.express-card__title {
  font-size: 38rpx;
  font-weight: 800;
  color: #ffffff;
}

.express-card__desc {
  margin-top: 10rpx;
  font-size: 24rpx;
  line-height: 1.6;
  color: rgba(255, 255, 255, 0.88);
}

.express-card__action {
  flex-shrink: 0;
  min-width: 180rpx;
  height: 72rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12rpx;
  border-radius: 999rpx;
  background: rgba(255, 255, 255, 0.18);
  color: #ffffff;
  font-size: 26rpx;
  font-weight: 700;
}

.express-card__dot {
  width: 18rpx;
  height: 18rpx;
  border-radius: 50%;
  background: #ffffff;
  box-shadow: 0 0 0 12rpx rgba(255, 255, 255, 0.14);
}

@keyframes pulse {
  0% {
    opacity: 0.2;
    transform: scale(0.8);
  }

  50% {
    opacity: 0.4;
    transform: scale(1);
  }

  100% {
    opacity: 0.18;
    transform: scale(1.08);
  }
}
</style>
