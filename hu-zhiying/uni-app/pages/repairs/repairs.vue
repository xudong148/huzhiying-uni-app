<template>
  <!-- 兼容入口：旧报修原型页已合并到新的首页与下单链路。 -->
  <view class="legacy-page">
    <view class="legacy-card">
      <text class="legacy-title">旧报修入口已迁移</text>
      <text class="legacy-desc">极速报修已经纳入新版服务详情和下单流程，正在为你跳转。</text>
      <button class="legacy-button" @tap="redirectNow">返回新版首页</button>
    </view>
  </view>
</template>

<script setup>
// 兼容逻辑：旧报修页统一回到新版首页。
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
/* 页面布局：仅保留旧路径兼容提示。 */
.legacy-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 48rpx;
  background: linear-gradient(180deg, #0f1830 0%, #1b2f66 100%);
}

.legacy-card {
  width: 100%;
  padding: 48rpx 36rpx;
  border-radius: 32rpx;
  background: rgba(255, 255, 255, 0.96);
  box-shadow: 0 18rpx 48rpx rgba(0, 0, 0, 0.18);
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
