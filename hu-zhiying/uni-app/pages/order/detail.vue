<template>
  <view class="page-shell">
    <view class="card order-detail__summary">
      <view class="order-detail__title">{{ order.title }}</view>
      <view class="order-detail__meta">订单号：{{ order.id }}</view>
      <view class="order-detail__meta">状态：{{ order.statusText }}</view>
      <view class="order-detail__meta">预约时间：{{ order.appointment || '待确认' }}</view>
      <view class="order-detail__meta">服务地址：{{ order.address }}</view>
      <view v-if="order.type === 'service'" class="order-detail__meta">服务师傅：{{ order.masterName || '待接单' }} {{ order.masterMobile }}</view>
      <view v-if="order.type === 'service'" class="order-detail__meta">预计到达：{{ order.eta }}</view>
      <view v-if="order.type === 'product' && order.installServiceOrderId" class="order-detail__meta">
        安装工单：{{ order.installServiceOrderId }}
      </view>
      <view class="order-detail__amount">订单金额：¥{{ Number(order.price || 0).toFixed(2) }}</view>
    </view>

    <view v-if="order.type === 'service' && order.timeline?.length" class="order-detail__section-title section-title">
      <text class="section-title__text">履约时间轴</text>
      <text class="section-title__desc" @tap="goTracking">查看轨迹</text>
    </view>
    <order-timeline v-if="order.type === 'service' && order.timeline?.length" :list="order.timeline"></order-timeline>

    <view v-if="order.quotation" class="card order-detail__quotation">
      <view class="section-title">
        <text class="section-title__text">增项报价单</text>
        <text class="section-title__desc">确认后继续施工</text>
      </view>
      <view v-for="item in order.quotation.items" :key="item.id" class="order-detail__quote-row">
        <text>{{ item.name }}</text>
        <text>¥{{ Number(item.amount || 0).toFixed(2) }}</text>
      </view>
      <view class="order-detail__quote-row order-detail__quote-row--total">
        <text>合计</text>
        <text>¥{{ Number(order.quotation.totalAmount || 0).toFixed(2) }}</text>
      </view>
      <view class="order-detail__quote-remark">{{ order.quotation.remark }}</view>
      <button
        v-if="order.status === 'WAITING_SUPPLEMENT_PAYMENT'"
        class="primary-btn order-detail__btn"
        :loading="quoteLoading"
        @tap="handleConfirmQuotation"
      >
        确认并补款
      </button>
    </view>

    <view class="card order-detail__actions">
      <button class="secondary-btn" @tap="goRefund">申请售后</button>
      <button v-if="order.type === 'product' && order.installServiceOrderId" class="secondary-btn" @tap="goInstallOrder">
        查看安装工单
      </button>
      <button v-else class="secondary-btn" @tap="goChat">在线沟通</button>
    </view>
  </view>
</template>

<script setup>
import { ref } from 'vue';
import { onHide, onLoad, onShow, onUnload } from '@dcloudio/uni-app';
import OrderTimeline from '../../components/order-timeline.vue';
import { confirmQuotation, getOrderDetail } from '../../api/order';
import { buildWsUrl } from '../../utils/request';

const orderId = ref('');
const quoteLoading = ref(false);
const order = ref({
  timeline: [],
  quotation: null,
});
let socketTask = null;

function goTracking() {
  uni.navigateTo({ url: `/pages/order/tracking?id=${order.value.id}` });
}

function goRefund() {
  uni.navigateTo({ url: `/pages/order/refund?id=${order.value.id}` });
}

function goChat() {
  uni.setStorageSync('hzy-chat-session', 'MS-001');
  uni.switchTab({ url: '/pages/message/chat' });
}

function goInstallOrder() {
  if (!order.value.installServiceOrderId) {
    return;
  }
  uni.navigateTo({ url: `/pages/order/detail?id=${order.value.installServiceOrderId}` });
}

async function handleConfirmQuotation() {
  if (!order.value.quotation?.id) {
    return;
  }
  quoteLoading.value = true;
  try {
    await confirmQuotation(order.value.quotation.id);
    uni.showToast({ title: '补款确认成功', icon: 'none' });
    await loadOrder();
  } finally {
    quoteLoading.value = false;
  }
}

async function loadOrder() {
  if (!orderId.value) {
    return;
  }
  const res = await getOrderDetail(orderId.value);
  order.value = res.data;
}

function closeSocket() {
  if (socketTask) {
    socketTask.close({});
    socketTask = null;
  }
}

function connectSocket() {
  if (!orderId.value || socketTask) {
    return;
  }
  socketTask = uni.connectSocket({
    url: buildWsUrl(`/ws/orders/${orderId.value}`),
  });

  socketTask.onOpen(() => {
    socketTask?.send({ data: 'ping' });
  });

  socketTask.onMessage(() => {
    loadOrder();
  });

  socketTask.onClose(() => {
    socketTask = null;
  });
}

onLoad((options) => {
  orderId.value = options.id || '';
});

onShow(loadOrder);
onShow(connectSocket);
onHide(closeSocket);
onUnload(closeSocket);
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

.order-detail__amount {
  margin-top: 18rpx;
  font-size: 28rpx;
  font-weight: 800;
  color: #1c1e26;
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

.order-detail__quote-row--total {
  margin-top: 18rpx;
  padding-top: 18rpx;
  border-top: 1rpx solid #edf1f6;
  font-weight: 700;
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
