<template>
  <view class="page-shell">
    <view class="card order-refund__summary">
      <view class="section-title">
        <text class="section-title__text">售后订单</text>
        <text class="section-title__desc">{{ order.id || '待确认' }}</text>
      </view>
      <view class="order-refund__title">{{ order.title || '正在加载订单信息' }}</view>
      <view class="order-refund__meta">当前状态：{{ order.statusText || '待确认' }}</view>
      <view class="order-refund__meta">订单金额：¥{{ Number(order.price || 0).toFixed(2) }}</view>
      <view class="order-refund__meta">服务地址：{{ order.address || '待补充地址' }}</view>
    </view>

    <view class="card order-refund__section">
      <view class="section-title">
        <text class="section-title__text">售后原因</text>
        <text class="section-title__desc">请选择最接近当前问题的一项</text>
      </view>
      <view
        v-for="item in reasons"
        :key="item"
        class="order-refund__reason"
        :class="{ 'order-refund__reason--active': reason === item }"
        @tap="reason = item"
      >
        {{ item }}
      </view>
    </view>

    <view class="card order-refund__section">
      <view class="section-title">
        <text class="section-title__text">补充说明</text>
        <text class="section-title__desc">{{ remark.length }}/120</text>
      </view>
      <textarea
        v-model="remark"
        class="order-refund__textarea"
        maxlength="120"
        placeholder="请描述问题经过、期望处理方式或与师傅沟通的关键情况，方便平台快速审核。"
      />
    </view>

    <view class="card order-refund__section">
      <view class="section-title">
        <text class="section-title__text">证据材料</text>
        <text class="section-title__desc">最多上传 6 张图片</text>
      </view>
      <view class="order-refund__evidence-actions">
        <button class="secondary-btn order-refund__upload" :loading="uploading" @tap="safeUploadEvidence">上传图片</button>
      </view>
      <view v-if="evidenceFiles.length" class="order-refund__evidence-list">
        <view v-for="item in evidenceFiles" :key="item.fileId || item.id" class="order-refund__evidence-item">
          <image class="order-refund__evidence-image" :src="item.url" mode="aspectFill" @tap="previewEvidence(item.url)" />
          <view class="order-refund__evidence-foot">
            <text class="order-refund__evidence-name">{{ item.originalName || '售后凭证' }}</text>
            <text class="order-refund__evidence-remove" @tap.stop="removeEvidence(item.fileId || item.id)">移除</text>
          </view>
        </view>
      </view>
      <view v-else class="order-refund__placeholder">暂未上传材料。订单现场照片、商品问题照片或聊天截图都可以作为补充说明。</view>
    </view>

    <view class="card order-refund__section">
      <view class="section-title">
        <text class="section-title__text">处理说明</text>
      </view>
      <view class="order-refund__tip">1. 申请提交后，订单会进入“退款处理中”状态。</view>
      <view class="order-refund__tip">2. 平台会结合订单轨迹、沟通记录和支付状态进行审核。</view>
      <view class="order-refund__tip">3. 如需补充材料，提交成功后可在售后详情页继续上传。</view>
    </view>

    <button class="primary-btn order-refund__btn" :loading="submitting" @tap="safeSubmitRefund">提交申请</button>
  </view>
</template>

<script setup>
import { ref } from 'vue';
import { onLoad } from '@dcloudio/uni-app';
import { uploadMedia } from '../../api/file';
import { getOrderDetail, refundOrder } from '../../api/order';
import { safeAsync } from '../../utils/page-task';

const reasons = ['乱收费', '服务未达标', '师傅未按时上门', '商品与描述不符', '重复扣款', '其他问题'];
const reason = ref(reasons[0]);
const remark = ref('');
const submitting = ref(false);
const uploading = ref(false);
const orderId = ref('');
const order = ref({});
const evidenceFiles = ref([]);

async function loadOrder() {
  if (!orderId.value) {
    uni.showToast({ title: '缺少订单编号', icon: 'none' });
    return;
  }
  const res = await getOrderDetail(orderId.value);
  order.value = res.data || {};
}

const safeLoadOrder = safeAsync(loadOrder, '加载售后订单');

function previewEvidence(url) {
  uni.previewImage({
    urls: evidenceFiles.value.map((item) => item.url),
    current: url,
  });
}

function removeEvidence(fileId) {
  evidenceFiles.value = evidenceFiles.value.filter((item) => (item.fileId || item.id) !== fileId);
}

async function uploadEvidence() {
  if (evidenceFiles.value.length >= 6) {
    uni.showToast({ title: '最多上传 6 张图片', icon: 'none' });
    return;
  }
  const chooseRes = await uni.chooseMedia({
    count: Math.min(6 - evidenceFiles.value.length, 6),
    mediaType: ['image'],
    sourceType: ['album', 'camera'],
  });
  const files = chooseRes.tempFiles || [];
  if (!files.length) {
    return;
  }

  uploading.value = true;
  try {
    const uploaded = [];
    for (const file of files) {
      const res = await uploadMedia(file.tempFilePath, 'refund_draft', orderId.value);
      if (res.data) {
        uploaded.push(res.data);
      }
    }
    evidenceFiles.value = evidenceFiles.value.concat(uploaded);
  } finally {
    uploading.value = false;
  }
}

async function submitRefund() {
  if (!orderId.value) {
    uni.showToast({ title: '缺少订单编号', icon: 'none' });
    return;
  }
  submitting.value = true;
  try {
    await refundOrder(orderId.value, {
      reason: reason.value,
      remark: remark.value.trim(),
      source: 'uni-app-refund-page',
      evidenceFileIds: evidenceFiles.value.map((item) => item.fileId || item.id),
    });
    uni.showToast({
      title: '售后申请已提交',
      icon: 'none',
    });
    setTimeout(() => {
      uni.redirectTo({ url: `/pages/order/after-sales?id=${orderId.value}` });
    }, 350);
  } finally {
    submitting.value = false;
  }
}

const safeUploadEvidence = safeAsync(uploadEvidence, '上传售后材料');
const safeSubmitRefund = safeAsync(submitRefund, '提交售后申请');

onLoad((options) => {
  orderId.value = options.id || '';
  safeLoadOrder();
});
</script>

<style scoped>
.order-refund__summary,
.order-refund__section {
  padding: 28rpx;
}

.order-refund__section + .order-refund__section {
  margin-top: 18rpx;
}

.order-refund__title {
  font-size: 30rpx;
  line-height: 1.5;
  font-weight: 800;
}

.order-refund__meta {
  margin-top: 10rpx;
  font-size: 24rpx;
  color: #667085;
}

.order-refund__reason {
  padding: 24rpx;
  border-radius: 22rpx;
  background: #f4f6f9;
  font-size: 26rpx;
}

.order-refund__reason + .order-refund__reason {
  margin-top: 14rpx;
}

.order-refund__reason--active {
  background: rgba(43, 92, 255, 0.1);
  color: #2b5cff;
}

.order-refund__textarea {
  width: 100%;
  min-height: 220rpx;
  padding: 18rpx;
  box-sizing: border-box;
  border-radius: 22rpx;
  background: #f4f6f9;
  font-size: 26rpx;
}

.order-refund__evidence-actions {
  display: flex;
  justify-content: flex-start;
}

.order-refund__upload {
  min-width: 220rpx;
}

.order-refund__evidence-list {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 18rpx;
  margin-top: 18rpx;
}

.order-refund__evidence-item {
  padding: 18rpx;
  border-radius: 24rpx;
  background: #f8fafc;
}

.order-refund__evidence-image {
  width: 100%;
  height: 220rpx;
  border-radius: 18rpx;
  background: #e5e7eb;
}

.order-refund__evidence-foot {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12rpx;
  margin-top: 14rpx;
}

.order-refund__evidence-name {
  flex: 1;
  min-width: 0;
  font-size: 22rpx;
  color: #475467;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.order-refund__evidence-remove {
  flex-shrink: 0;
  font-size: 22rpx;
  color: #f04438;
}

.order-refund__placeholder {
  margin-top: 18rpx;
  font-size: 24rpx;
  line-height: 1.7;
  color: #667085;
}

.order-refund__tip {
  font-size: 24rpx;
  line-height: 1.7;
  color: #475467;
}

.order-refund__tip + .order-refund__tip {
  margin-top: 10rpx;
}

.order-refund__btn {
  margin-top: 24rpx;
}
</style>
