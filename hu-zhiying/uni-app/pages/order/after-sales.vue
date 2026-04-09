<template>
  <view class="page-shell after-sales">
    <view class="card after-sales__hero" :class="`after-sales__hero--${statusTone}`">
      <view class="after-sales__hero-title">{{ detail.statusText || '售后处理中' }}</view>
      <view class="after-sales__hero-subtitle">申请号：{{ detail.refundRequestNo || '--' }}</view>
      <view class="after-sales__hero-meta">订单号：{{ detail.orderId || '--' }}</view>
      <view class="after-sales__hero-meta">申请原因：{{ detail.reason || '待补充' }}</view>
      <view class="after-sales__hero-meta">售后金额：¥{{ Number(detail.amount || 0).toFixed(2) }}</view>
    </view>

    <view class="card after-sales__section">
      <view class="section-title">
        <text class="section-title__text">处理进度</text>
        <text class="section-title__desc">{{ detail.channel || 'uni-app' }}</text>
      </view>
      <view v-if="detail.timeline?.length" class="after-sales__timeline">
        <view v-for="item in detail.timeline" :key="item.key" class="after-sales__timeline-item">
          <view class="after-sales__timeline-axis">
            <view class="after-sales__timeline-dot"></view>
            <view v-if="item !== detail.timeline[detail.timeline.length - 1]" class="after-sales__timeline-line"></view>
          </view>
          <view class="after-sales__timeline-body">
            <view class="after-sales__timeline-head">
              <text class="after-sales__timeline-title">{{ item.label }}</text>
              <text class="after-sales__timeline-time">{{ formatTime(item.time) }}</text>
            </view>
            <view class="after-sales__timeline-desc">{{ item.desc || '平台已更新当前节点' }}</view>
          </view>
        </view>
      </view>
      <view v-else class="after-sales__placeholder">平台正在同步当前售后节点，请稍后刷新查看。</view>
    </view>

    <view class="card after-sales__section">
      <view class="section-title">
        <text class="section-title__text">审核备注</text>
      </view>
      <view class="after-sales__remark">{{ detail.reviewRemark || '当前节点暂无额外备注，平台处理后会同步更新。' }}</view>
      <view class="after-sales__times">
        <view class="after-sales__time-item">申请时间：{{ formatTime(detail.requestedAt) }}</view>
        <view class="after-sales__time-item">审核时间：{{ formatTime(detail.approvedAt) }}</view>
        <view class="after-sales__time-item">完成时间：{{ formatTime(detail.completedAt) }}</view>
      </view>
    </view>

    <view class="card after-sales__section">
      <view class="section-title">
        <text class="section-title__text">证据材料</text>
        <text class="section-title__desc">{{ detail.evidenceFiles?.length || 0 }} 份</text>
      </view>
      <button
        v-if="detail.canAppendEvidence"
        class="secondary-btn after-sales__upload"
        :loading="uploading"
        @tap="safeAppendEvidence"
      >
        补充材料
      </button>
      <view v-if="detail.evidenceFiles?.length" class="after-sales__evidence-list">
        <view v-for="item in detail.evidenceFiles" :key="item.fileId || item.id" class="after-sales__evidence-item">
          <image
            v-if="isImage(item)"
            class="after-sales__evidence-image"
            :src="item.url"
            mode="aspectFill"
            @tap="previewEvidence(item.url)"
          />
          <view v-else class="after-sales__file">{{ item.originalName || '附件' }}</view>
          <view class="after-sales__evidence-name">{{ item.originalName || '售后凭证' }}</view>
        </view>
      </view>
      <view v-else class="after-sales__placeholder">还没有补充材料，平台需要时你可以在这里继续上传。</view>
    </view>

    <view class="card after-sales__actions">
      <button class="primary-btn" @tap="goOrderDetail">返回订单详情</button>
      <button class="secondary-btn" @tap="safeReload">刷新进度</button>
    </view>
  </view>
</template>

<script setup>
import { computed, ref } from 'vue';
import { onLoad } from '@dcloudio/uni-app';
import { uploadMedia } from '../../api/file';
import { getAfterSalesDetail } from '../../api/order';
import { safeAsync } from '../../utils/page-task';

const orderId = ref('');
const uploading = ref(false);
const detail = ref({
  timeline: [],
  evidenceFiles: [],
});

const statusTone = computed(() => {
  if (detail.value.status === 'COMPLETED') {
    return 'completed';
  }
  if (detail.value.status === 'REJECTED') {
    return 'rejected';
  }
  return 'processing';
});

function formatTime(value) {
  if (!value) {
    return '待同步';
  }
  return String(value).replace('T', ' ');
}

function isImage(file) {
  return String(file?.contentType || '').startsWith('image/');
}

function previewEvidence(url) {
  const images = (detail.value.evidenceFiles || []).filter((item) => isImage(item)).map((item) => item.url);
  if (!images.length) {
    return;
  }
  uni.previewImage({
    urls: images,
    current: url,
  });
}

async function loadDetail() {
  if (!orderId.value) {
    uni.showToast({ title: '缺少订单编号', icon: 'none' });
    return;
  }
  const res = await getAfterSalesDetail(orderId.value);
  detail.value = res.data || {
    timeline: [],
    evidenceFiles: [],
  };
}

async function appendEvidence() {
  if (!detail.value.refundRequestNo) {
    uni.showToast({ title: '当前售后单不可补充材料', icon: 'none' });
    return;
  }
  const chooseRes = await uni.chooseMedia({
    count: 6,
    mediaType: ['image'],
    sourceType: ['album', 'camera'],
  });
  const files = chooseRes.tempFiles || [];
  if (!files.length) {
    return;
  }

  uploading.value = true;
  try {
    for (const file of files) {
      await uploadMedia(file.tempFilePath, 'refund_evidence', detail.value.refundRequestNo);
    }
    uni.showToast({ title: '材料已补充', icon: 'none' });
    await loadDetail();
  } finally {
    uploading.value = false;
  }
}

function goOrderDetail() {
  if (!orderId.value) {
    return;
  }
  uni.redirectTo({ url: `/pages/order/detail?id=${orderId.value}` });
}

const safeReload = safeAsync(loadDetail, '刷新售后详情');
const safeAppendEvidence = safeAsync(appendEvidence, '补充售后材料');

onLoad((options) => {
  orderId.value = options.id || '';
  safeReload();
});
</script>

<style scoped>
.after-sales {
  padding-bottom: calc(36rpx + env(safe-area-inset-bottom));
}

.after-sales__hero,
.after-sales__section,
.after-sales__actions {
  padding: 28rpx;
}

.after-sales__hero {
  color: #fff;
  background: linear-gradient(135deg, #1c1e26 0%, #2b5cff 100%);
}

.after-sales__hero--completed {
  background: linear-gradient(135deg, #0f766e 0%, #22c55e 100%);
}

.after-sales__hero--rejected {
  background: linear-gradient(135deg, #7f1d1d 0%, #ef4444 100%);
}

.after-sales__hero-title {
  font-size: 34rpx;
  font-weight: 800;
}

.after-sales__hero-subtitle,
.after-sales__hero-meta {
  margin-top: 10rpx;
  font-size: 24rpx;
  line-height: 1.6;
  color: rgba(255, 255, 255, 0.84);
}

.after-sales__timeline {
  margin-top: 8rpx;
}

.after-sales__timeline-item {
  display: flex;
  gap: 18rpx;
}

.after-sales__timeline-item + .after-sales__timeline-item {
  margin-top: 18rpx;
}

.after-sales__timeline-axis {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.after-sales__timeline-dot {
  width: 22rpx;
  height: 22rpx;
  border-radius: 50%;
  background: #2b5cff;
  box-shadow: 0 0 0 10rpx rgba(43, 92, 255, 0.12);
}

.after-sales__timeline-line {
  width: 4rpx;
  flex: 1;
  margin-top: 8rpx;
  background: #dbe3ff;
}

.after-sales__timeline-body {
  flex: 1;
}

.after-sales__timeline-head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16rpx;
}

.after-sales__timeline-title {
  font-size: 28rpx;
  font-weight: 700;
  color: #1c1e26;
}

.after-sales__timeline-time {
  flex-shrink: 0;
  font-size: 22rpx;
  color: #8b90a0;
}

.after-sales__timeline-desc,
.after-sales__remark,
.after-sales__placeholder,
.after-sales__time-item {
  margin-top: 8rpx;
  font-size: 24rpx;
  line-height: 1.7;
  color: #667085;
}

.after-sales__upload {
  margin-top: 12rpx;
}

.after-sales__evidence-list {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 18rpx;
  margin-top: 18rpx;
}

.after-sales__evidence-item {
  padding: 18rpx;
  border-radius: 24rpx;
  background: #f8fafc;
}

.after-sales__evidence-image,
.after-sales__file {
  width: 100%;
  height: 220rpx;
  border-radius: 18rpx;
  background: #e5e7eb;
}

.after-sales__file {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 24rpx;
  box-sizing: border-box;
  text-align: center;
  font-size: 22rpx;
  color: #475467;
}

.after-sales__evidence-name {
  margin-top: 12rpx;
  font-size: 22rpx;
  color: #475467;
  word-break: break-all;
}

.after-sales__actions {
  display: flex;
  flex-direction: column;
  gap: 16rpx;
}
</style>
