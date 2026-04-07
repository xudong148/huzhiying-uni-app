<template>
  <view class="empty-state card">
    <image class="empty-state__icon" :src="icon" mode="aspectFit" />
    <view class="empty-state__title">{{ title }}</view>
    <view class="empty-state__desc">{{ desc }}</view>
  </view>
</template>

<script setup>
defineProps({
  icon: {
    type: String,
    default: '/static/icons/order.svg',
  },
  title: {
    type: String,
    default: '暂无数据',
  },
  desc: {
    type: String,
    default: '当前还没有可展示的内容',
  },
});
</script>

<style scoped>
.empty-state {
  padding: 48rpx 32rpx;
  text-align: center;
}

.empty-state__icon {
  width: 96rpx;
  height: 96rpx;
}

.empty-state__title {
  margin-top: 18rpx;
  font-size: 30rpx;
  font-weight: 700;
}

.empty-state__desc {
  margin-top: 10rpx;
  font-size: 22rpx;
  color: #8b90a0;
}
</style>
