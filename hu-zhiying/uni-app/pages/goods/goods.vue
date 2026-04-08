<template>
  <!-- 兼容入口：旧商品原型页已并入新的分类与详情页。 -->
  <view class="legacy-page">
    <view class="legacy-card">
      <text class="legacy-title">旧商品入口已迁移</text>
      <text class="legacy-desc">新的商品与服务统一在分类页和详情页中展示，正在为你跳转。</text>
      <button class="legacy-button" @tap="redirectNow">进入新分类页</button>
    </view>
  </view>
</template>

<script setup>
// 兼容逻辑：保留历史路径，统一跳转到新的分类页。
import { onMounted } from 'vue';

function redirectNow() {
  uni.reLaunch({
    url: '/pages/category/index',
  });
}

onMounted(() => {
  setTimeout(() => {
    redirectNow();
  }, 300);
});
</script>

<style scoped>
/* 页面布局：旧商品原型页只保留提示和兼容跳转。 */
.legacy-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 48rpx;
  background: linear-gradient(180deg, #f7f8fb 0%, #eef2f8 100%);
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
