<template>
  <view class="page-shell">
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

    <view class="card order-refund__section">
      <view class="section-title">
        <text class="section-title__text">补充说明</text>
      </view>
      <textarea v-model="remark" class="order-refund__textarea" placeholder="请描述具体问题，支持平台客服快速仲裁"></textarea>
    </view>

    <button class="primary-btn order-refund__btn" @tap="submitRefund">提交申请</button>
  </view>
</template>

<script setup>
import { ref } from 'vue';

const reasons = ['乱收费', '服务未达标', '师傅未按时上门', '商品与描述不符'];
const reason = ref(reasons[0]);
const remark = ref('');

function submitRefund() {
  uni.showToast({
    title: '已提交售后申请',
    icon: 'none',
  });
  setTimeout(() => {
    uni.navigateBack();
  }, 500);
}
</script>

<style scoped>
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
