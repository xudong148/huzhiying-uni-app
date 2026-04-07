<template>
  <view class="vip-butler pressable" @tap="$emit('tap')">
    <view class="vip-butler__content">
      <view class="vip-butler__left">
        <image class="vip-butler__icon" src="/static/icons/butler.svg" mode="aspectFit" />
        <view>
          <view class="vip-butler__title">
            <text>{{ title }}</text>
            <text class="vip-butler__badge">SVIP</text>
          </view>
          <view class="vip-butler__desc">{{ desc }}</view>
        </view>
      </view>
      <view class="vip-butler__action">立即体验</view>
    </view>
  </view>
</template>

<script setup>
defineProps({
  title: {
    type: String,
    default: '全屋金管家',
  },
  desc: {
    type: String,
    default: '包年服务、专属客服、优先派单',
  },
});

defineEmits(['tap']);
</script>

<style scoped>
.vip-butler {
  position: relative;
  overflow: hidden;
  padding: 32rpx;
  border-radius: 32rpx;
  background: linear-gradient(135deg, #1c1e26 0%, #2c3242 100%);
  box-shadow: 0 18rpx 44rpx rgba(28, 30, 38, 0.18);
}

.vip-butler::after {
  content: '';
  position: absolute;
  top: 0;
  left: -40%;
  width: 30%;
  height: 100%;
  transform: skewX(-25deg);
  background: linear-gradient(90deg, transparent, rgba(255, 224, 163, 0.26), transparent);
  animation: glare 5s infinite linear;
}

.vip-butler__content {
  position: relative;
  z-index: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 20rpx;
}

.vip-butler__left {
  display: flex;
  align-items: center;
  gap: 20rpx;
}

.vip-butler__icon {
  width: 88rpx;
  height: 88rpx;
}

.vip-butler__title {
  display: flex;
  align-items: center;
  gap: 12rpx;
  font-size: 34rpx;
  font-weight: 800;
  color: #ffe0a3;
}

.vip-butler__badge {
  padding: 6rpx 12rpx;
  border-radius: 12rpx;
  background: #ffe0a3;
  color: #1c1e26;
  font-size: 20rpx;
}

.vip-butler__desc {
  margin-top: 10rpx;
  font-size: 22rpx;
  color: rgba(255, 224, 163, 0.78);
}

.vip-butler__action {
  flex-shrink: 0;
  padding: 20rpx 28rpx;
  border-radius: 999rpx;
  background: linear-gradient(90deg, #ffe0a3, #f1cb82);
  color: #1c1e26;
  font-size: 24rpx;
  font-weight: 800;
}

@keyframes glare {
  0% {
    left: -40%;
  }

  16% {
    left: 120%;
  }

  100% {
    left: 120%;
  }
}
</style>
