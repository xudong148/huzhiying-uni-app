<template>
  <!-- 兼容入口：旧“我的”原型页已迁移到新版个人中心。 -->
  <view class="legacy-page">
    <view class="legacy-card">
      <text class="legacy-title">旧个人中心入口已迁移</text>
      <text class="legacy-desc">新版个人中心统一使用新的用户页，正在为你跳转。</text>
      <button class="legacy-button" @tap="redirectNow">进入新版个人中心</button>
    </view>
  </view>
</template>

<script setup>
// 兼容逻辑：历史路径统一回到新版用户中心。
import { onMounted } from 'vue';

function redirectNow() {
  uni.reLaunch({
    url: '/pages/user/index',
  });
}

onMounted(() => {
  setTimeout(() => {
    redirectNow();
  }, 300);
});
</script>

<style scoped>
/* 页面布局：旧用户页仅保留跳转提示。 */
.legacy-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 48rpx;
  background: linear-gradient(180deg, #f4f7fb 0%, #eef2f8 100%);
}

.legacy-card {
  width: 100%;
  padding: 48rpx 36rpx;
  border-radius: 32rpx;
  background: #ffffff;
  box-shadow: 0 18rpx 48rpx rgba(24, 36, 61, 0.1);
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
