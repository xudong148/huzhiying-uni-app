<template>
  <view class="page-shell">
    <view class="card master-setting__section">
      <view class="master-setting__row">
        <text>听单状态</text>
        <switch :checked="form.listening" color="#2B5CFF" @change="form.listening = $event.detail.value" />
      </view>
      <view class="master-setting__row">
        <text>最大接单距离</text>
        <picker :range="distanceOptions" :value="distanceIndex" @change="handleDistanceChange">
          <view class="master-setting__picker">{{ distanceOptions[distanceIndex] }}</view>
        </picker>
      </view>
      <view class="master-setting__row">
        <text>隐私号拨打</text>
        <switch :checked="form.privacyNumber" color="#2B5CFF" @change="form.privacyNumber = $event.detail.value" />
      </view>
    </view>

    <button class="primary-btn" @tap="submit">保存设置</button>
  </view>
</template>

<script setup>
import { reactive, ref } from 'vue';
import { onShow } from '@dcloudio/uni-app';
import { getMasterSettings, saveMasterSettings } from '../../api/master';

const distanceOptions = ['5km', '10km', '20km', '30km'];
const distanceIndex = ref(2);
const form = reactive({
  listening: true,
  maxDistance: '20km',
  privacyNumber: true,
});

function syncDistanceIndex() {
  const index = distanceOptions.findIndex((item) => item === form.maxDistance);
  distanceIndex.value = index >= 0 ? index : 2;
}

function handleDistanceChange(event) {
  distanceIndex.value = Number(event.detail.value);
  form.maxDistance = distanceOptions[distanceIndex.value];
}

async function submit() {
  await saveMasterSettings({
    listening: form.listening,
    maxDistance: form.maxDistance,
    privacyNumber: form.privacyNumber,
  });
  uni.showToast({ title: '设置已保存', icon: 'none' });
}

onShow(async () => {
  const res = await getMasterSettings();
  Object.assign(form, res.data);
  syncDistanceIndex();
});
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
