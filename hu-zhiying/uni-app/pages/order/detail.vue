<template>
  <view class="page-shell">
    <view class="card order-detail__summary">
      <view class="order-detail__title">{{ order.title }}</view>
      <view class="order-detail__meta">订单号：{{ order.id }}</view>
      <view class="order-detail__meta">状态：{{ order.statusText }}</view>
      <view class="order-detail__meta">师傅：{{ order.masterName }} {{ order.masterMobile }}</view>
      <view class="order-detail__meta">ETA：{{ order.eta }}</view>
    </view>

    <view class="section-title order-detail__section-title">
      <text class="section-title__text">履约时间轴</text>
      <text class="section-title__desc" @tap="goTracking">查看轨迹</text>
    </view>
    <order-timeline :list="order.timeline"></order-timeline>

    <view v-if="order.quotation" class="card order-detail__quotation">
      <view class="section-title">
        <text class="section-title__text">增项报价单</text>
        <text class="section-title__desc">用户确认后方可施工</text>
      </view>
      <view v-for="item in order.quotation.items" :key="item.id" class="order-detail__quote-row">
        <text>{{ item.name }}</text>
        <text>¥{{ item.amount }}</text>
      </view>
      <view class="order-detail__quote-remark">{{ order.quotation.remark }}</view>
      <button class="primary-btn order-detail__btn" @tap="confirmQuotation">确认并补款</button>
    </view>

    <view class="card order-detail__actions">
      <button class="secondary-btn" @tap="goRefund">申请售后</button>
      <button class="secondary-btn" @tap="goChat">联系客服</button>
    </view>
  </view>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import OrderTimeline from '../../components/order-timeline.vue';
import { getOrderDetail } from '../../api/order';

const order = ref({
  timeline: [],
});

function goTracking() {
  uni.navigateTo({ url: `/pages/order/tracking?id=${order.value.id}` });
}

function goRefund() {
  uni.navigateTo({ url: `/pages/order/refund?id=${order.value.id}` });
}

function goChat() {
  uni.switchTab({ url: '/pages/message/chat' });
}

function confirmQuotation() {
  uni.showToast({ title: '补款单已确认', icon: 'none' });
}

onMounted(async () => {
  const pages = getCurrentPages();
  const current = pages[pages.length - 1];
  const res = await getOrderDetail(current.options.id);
  order.value = res.data;
});
</script>

<style scoped>
.order-detail__summary,
.order-detail__quotation,
.order-detail__actions {
  padding: 28rpx;
}

.order-detail__title {
  font-size: 32rpx;
  font-weight: 800;
}

.order-detail__meta {
  margin-top: 12rpx;
  font-size: 24rpx;
  color: #667085;
}

.order-detail__section-title {
  margin-top: 24rpx;
}

.order-detail__quotation {
  margin-top: 20rpx;
}

.order-detail__quote-row {
  display: flex;
  justify-content: space-between;
  font-size: 26rpx;
}

.order-detail__quote-row + .order-detail__quote-row {
  margin-top: 14rpx;
}

.order-detail__quote-remark {
  margin-top: 18rpx;
  padding: 18rpx;
  border-radius: 20rpx;
  background: #fff8ec;
  color: #8a5d00;
  font-size: 22rpx;
  line-height: 1.6;
}

.order-detail__btn {
  margin-top: 20rpx;
}

.order-detail__actions {
  margin-top: 20rpx;
  display: flex;
  gap: 16rpx;
}

.order-detail__actions button {
  flex: 1;
}
</style>
