<template>
  <view class="page-shell">
    <view class="card master-setting__hero">
      <view class="master-setting__hero-title">接单设置</view>
      <view class="master-setting__hero-desc">{{ summaryText }}</view>
    </view>

    <view class="card master-setting__section">
      <view class="section-title">
        <text class="section-title__text">接单偏好</text>
        <text class="section-title__desc">保存后立即生效</text>
      </view>

      <view class="master-setting__row">
        <view class="master-setting__row-main">
          <view class="master-setting__row-title">听单状态</view>
          <view class="master-setting__row-desc">关闭后不会继续收到新的抢单提醒，但你仍然可以手动进入抢派单大厅查看工单。</view>
        </view>
        <switch :checked="form.listening" color="#2B5CFF" @change="form.listening = $event.detail.value" />
      </view>

      <view class="master-setting__row">
        <view class="master-setting__row-main">
          <view class="master-setting__row-title">最大接单距离</view>
          <view class="master-setting__row-desc">距离越大，看到的工单越多，但跨区赶路和迟到风险也会更高。</view>
        </view>
        <picker :range="distanceOptions" :value="distanceIndex" @change="handleDistanceChange">
          <view class="master-setting__picker">{{ distanceOptions[distanceIndex] }}</view>
        </picker>
      </view>

      <view class="master-setting__row master-setting__row--last">
        <view class="master-setting__row-main">
          <view class="master-setting__row-title">隐私号外呼</view>
          <view class="master-setting__row-desc">建议默认开启，减少师傅手机号直接暴露给用户带来的售后和骚扰风险。</view>
        </view>
        <switch :checked="form.privacyNumber" color="#2B5CFF" @change="form.privacyNumber = $event.detail.value" />
      </view>
    </view>

    <view class="card master-setting__section">
      <view class="section-title">
        <text class="section-title__text">上线建议</text>
        <text class="section-title__desc">从履约风险倒推</text>
      </view>
      <view class="master-setting__tip">1. 新师傅首周建议先把接单范围控制在 10km 或 20km，先把准时率和好评率跑稳。</view>
      <view class="master-setting__tip">2. 如果近期售后集中，优先保持隐私号开启，避免订单结束后手机号持续外泄。</view>
      <view class="master-setting__tip">3. 如果准备休息或无法准时到场，直接关闭听单，比抢单后取消更影响平台信任。</view>
    </view>

    <button class="primary-btn master-setting__submit" :loading="saving" @tap="safeSubmit">保存并立即生效</button>
  </view>
</template>

<script setup>
import { computed, reactive, ref } from 'vue';
import { onShow } from '@dcloudio/uni-app';
import { getMasterSettings, saveMasterSettings } from '../../api/master';
import { safeAsync, showActionError } from '../../utils/page-task';

const distanceOptions = ['5km', '10km', '20km', '30km'];
const distanceIndex = ref(2);
const saving = ref(false);

const form = reactive({
  listening: true,
  maxDistance: '20km',
  privacyNumber: true,
});

const summaryText = computed(() => {
  const listeningText = form.listening
    ? `当前正在听单，系统会按 ${form.maxDistance} 范围给你推送新工单。`
    : '当前已暂停自动听单，系统不会再主动推送新的抢单提醒。';
  const privacyText = form.privacyNumber ? '已开启隐私号外呼。' : '当前未开启隐私号外呼。';
  return `${listeningText}${privacyText}`;
});

function syncDistanceIndex() {
  const index = distanceOptions.findIndex((item) => item === form.maxDistance);
  distanceIndex.value = index >= 0 ? index : 2;
}

function handleDistanceChange(event) {
  distanceIndex.value = Number(event.detail.value);
  form.maxDistance = distanceOptions[distanceIndex.value];
}

async function loadSettings() {
  const res = await getMasterSettings();
  Object.assign(form, res.data);
  syncDistanceIndex();
}

const safeLoadSettings = safeAsync(loadSettings, '加载师傅接单设置');

async function submit() {
  saving.value = true;
  try {
    const res = await saveMasterSettings({
      listening: form.listening,
      maxDistance: form.maxDistance,
      privacyNumber: form.privacyNumber,
    });
    Object.assign(form, res.data || {});
    syncDistanceIndex();
    uni.showToast({ title: '接单设置已保存', icon: 'none' });
  } catch (error) {
    showActionError(error, '保存接单设置失败');
  } finally {
    saving.value = false;
  }
}

const safeSubmit = safeAsync(submit, '保存师傅接单设置');

onShow(safeLoadSettings);
</script>

<style scoped>
.master-setting__hero,
.master-setting__section {
  padding: 28rpx;
}

.master-setting__hero {
  background: linear-gradient(135deg, #1c1e26 0%, #36405a 100%);
  color: #ffffff;
}

.master-setting__hero-title {
  font-size: 34rpx;
  font-weight: 800;
}

.master-setting__hero-desc {
  margin-top: 10rpx;
  font-size: 24rpx;
  line-height: 1.7;
  color: rgba(255, 255, 255, 0.82);
}

.master-setting__section {
  margin-top: 20rpx;
}

.master-setting__row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 24rpx;
  padding: 24rpx 0;
  border-bottom: 1rpx solid #edf1f6;
}

.master-setting__row--last {
  border-bottom: none;
}

.master-setting__row-main {
  flex: 1;
}

.master-setting__row-title {
  font-size: 28rpx;
  font-weight: 700;
  color: #1c1e26;
}

.master-setting__row-desc,
.master-setting__tip {
  margin-top: 8rpx;
  font-size: 22rpx;
  line-height: 1.7;
  color: #667085;
}

.master-setting__picker {
  min-width: 124rpx;
  text-align: right;
  color: #2b5cff;
  font-size: 28rpx;
  font-weight: 700;
}

.master-setting__submit {
  margin-top: 24rpx;
}
</style>
