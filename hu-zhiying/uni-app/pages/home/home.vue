<template>
  <!-- 兼容入口：旧首页原型页已迁移到新的首页路由。 -->
  <view class="legacy-page">
    <view class="legacy-card">
      <text class="legacy-title">旧首页入口已迁移</text>
      <text class="legacy-desc">当前版本统一使用新的首页模块，正在为你跳转。</text>
      <button class="legacy-button" @tap="redirectNow">立即进入新首页</button>
    </view>
  </view>
</template>

<script setup>
// 兼容逻辑：保留旧路径，统一跳转到新的首页实现。
import { onMounted } from 'vue';

function redirectNow() {
  uni.reLaunch({
    url: '/pages/index/index',
  });
}

onMounted(() => {
  setTimeout(() => {
    redirectNow();
  }, 300);
});
</script>

<style scoped>
/* 页面布局：旧入口只保留兼容跳转提示。 */
.legacy-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 48rpx;
  background: linear-gradient(180deg, #f4f8ff 0%, #eef3fb 100%);
}

.legacy-card {
  width: 100%;
  padding: 48rpx 36rpx;
  border-radius: 32rpx;
  background: #ffffff;
  box-shadow: 0 18rpx 48rpx rgba(43, 92, 255, 0.12);
}

.legacy-title {
  display: block;
  margin-bottom: 16rpx;
  font-size: 36rpx;
  font-weight: 600;
  color: #18243d;
}

.legacy-desc {
  display: block;
  margin-bottom: 32rpx;
  font-size: 28rpx;
  line-height: 1.6;
  color: #5f6b85;
}

.legacy-button {
  height: 88rpx;
  line-height: 88rpx;
  border: none;
  border-radius: 999rpx;
  background: #2b5cff;
  color: #ffffff;
  font-size: 30rpx;
}
</style>
