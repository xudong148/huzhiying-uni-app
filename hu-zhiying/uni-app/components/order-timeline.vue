<template>
  <view class="timeline card">
    <view v-for="(item, index) in list" :key="item.key" class="timeline__item">
      <view class="timeline__axis">
        <view class="timeline__dot" :class="{ 'timeline__dot--done': item.done }"></view>
        <view v-if="index !== list.length - 1" class="timeline__line"></view>
      </view>
      <view class="timeline__content">
        <view class="timeline__title">{{ item.label }}</view>
        <view class="timeline__desc">{{ item.desc }}</view>
      </view>
    </view>
  </view>
</template>

<script setup>
defineProps({
  list: {
    type: Array,
    default: () => [],
  },
});
</script>

<style scoped>
.timeline {
  padding: 28rpx 24rpx;
}

.timeline__item {
  display: flex;
  gap: 18rpx;
}

.timeline__item + .timeline__item {
  margin-top: 18rpx;
}

.timeline__axis {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.timeline__dot {
  width: 24rpx;
  height: 24rpx;
  border-radius: 50%;
  background: #d8ddea;
}

.timeline__dot--done {
  background: #2b5cff;
  box-shadow: 0 0 0 10rpx rgba(43, 92, 255, 0.12);
}

.timeline__line {
  width: 4rpx;
  flex: 1;
  margin-top: 8rpx;
  background: #e5eaf5;
}

.timeline__title {
  font-size: 28rpx;
  font-weight: 700;
}

.timeline__desc {
  margin-top: 6rpx;
  font-size: 22rpx;
  color: #8b90a0;
  line-height: 1.6;
}
</style>
