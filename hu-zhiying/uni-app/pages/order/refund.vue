<template>
  <view class="page-shell">
    <!-- 售后原因 -->
    <view class="card order-refund__section">
      <view class="section-title">
        <text class="section-title__text">售后原因</text>
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

    <!-- 补充说明 -->
    <view class="card order-refund__section">
      <view class="section-title">
        <text class="section-title__text">补充说明</text>
      </view>
      <textarea v-model="remark" class="order-refund__textarea" placeholder="请描述具体问题，方便平台客服快速处理"></textarea>
    </view>

    <button class="primary-btn order-refund__btn" :loading="submitting" @tap="submitRefund">提交申请</button>
  </view>
</template>

<script setup>
/**
 * 售后退款页。
 * 当前提交直接走退款接口，原因和备注用于收集问题上下文。
 */
import { ref } from 'vue';
import { onLoad } from '@dcloudio/uni-app';
import { refundOrder } from '../../api/order';

const reasons = ['乱收费', '服务未达标', '师傅未按时上门', '商品与描述不符'];
const reason = ref(reasons[0]);
const remark = ref('');
const submitting = ref(false);
const orderId = ref('');

async function submitRefund() {
  if (!orderId.value) {
    return;
  }
  submitting.value = true;
  try {
    await refundOrder(orderId.value);
    uni.showToast({
      title: '售后申请已提交',
      icon: 'none',
    });
    setTimeout(() => {
      uni.navigateBack();
    }, 500);
  } finally {
    submitting.value = false;
  }
}

onLoad((options) => {
  orderId.value = options.id || '';
});
</script>

<style scoped>
/* 区块与选项 */
.order-refund__section {
  padding: 28rpx;
}

.order-refund__section + .order-refund__section {
  margin-top: 18rpx;
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
  border-radius: 22rpx;
  background: #f4f6f9;
  font-size: 26rpx;
}

.order-refund__btn {
  margin-top: 24rpx;
}
</style>
