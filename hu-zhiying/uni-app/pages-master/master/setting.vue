<template>
  <view class="page-shell">
    <view class="card master-setting__section">
      <view class="master-setting__row">
        <text>听单状态</text>
        <switch :checked="enabled" color="#2B5CFF" @change="enabled = $event.detail.value" />
      </view>
      <view class="master-setting__row">
        <text>最大接单距离</text>
        <picker :range="distanceOptions" :value="distanceIndex" @change="handleDistanceChange">
          <view class="master-setting__picker">{{ distanceOptions[distanceIndex] }}</view>
        </picker>
      </view>
      <view class="master-setting__row">
        <text>隐私号拨打</text>
        <switch checked color="#2B5CFF" />
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref } from 'vue';

const enabled = ref(true);
const distanceOptions = ['5km', '10km', '20km', '30km'];
const distanceIndex = ref(2);

function handleDistanceChange(event) {
  distanceIndex.value = Number(event.detail.value);
}
</script>

<style scoped>
.master-setting__section {
  padding: 28rpx;
}

.master-setting__row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16rpx;
  padding: 22rpx 0;
  border-bottom: 1rpx solid #edf1f6;
  font-size: 28rpx;
}

.master-setting__picker {
  color: #2b5cff;
  font-weight: 700;
}
</style>
