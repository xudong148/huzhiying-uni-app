<template>
  <view class="page-shell">
    <!-- 订单摘要 -->
    <view class="card order-detail__summary">
      <view class="order-detail__title">{{ order.title }}</view>
      <view class="order-detail__meta">订单号：{{ order.id }}</view>
      <view class="order-detail__meta">状态：{{ order.statusText }}</view>
      <view class="order-detail__meta">预约时间：{{ order.appointment || '待确认' }}</view>
      <view class="order-detail__meta">服务地址：{{ order.address }}</view>
      <view v-if="order.type === 'service'" class="order-detail__meta">
        服务师傅：{{ order.masterName || '待接单' }} {{ order.masterMobile }}
      </view>
      <view v-if="order.type === 'service'" class="order-detail__meta">预计到达：{{ order.eta }}</view>
      <view v-if="order.type === 'product' && order.installServiceOrderId" class="order-detail__meta">
        安装工单：{{ order.installServiceOrderId }}
      </view>
      <view class="order-detail__amount">订单金额：¥{{ Number(order.price || 0).toFixed(2) }}</view>
    </view>

    <!-- 履约时间线 -->
    <view v-if="order.type === 'service' && order.timeline?.length" class="order-detail__section-title section-title">
      <text class="section-title__text">履约时间线</text>
      <text class="section-title__desc" @tap="goTracking">查看轨迹</text>
    </view>
    <order-timeline v-if="order.type === 'service' && order.timeline?.length" :list="order.timeline"></order-timeline>

    <!-- 增项报价 -->
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
        v-if="order.status === 'WAITING_SUPPLEMENT_PAYMENT' && order.quotation?.status !== 'CONFIRMED'"
        class="primary-btn order-detail__btn"
        :loading="quoteLoading"
        @tap="handleConfirmQuotation"
      >
        确认并补款
      </button>
    </view>

    <!-- 操作区 -->
    <view class="card order-detail__actions">
      <button v-if="canPay" class="primary-btn" :loading="payLoading" @tap="handlePay">继续支付</button>
      <button class="secondary-btn" @tap="goRefund">申请售后</button>
      <button v-if="order.type === 'service' && canUrge" class="secondary-btn" :loading="urgeLoading" @tap="handleUrge">催单</button>
      <button v-if="order.type === 'service' && canCancel" class="secondary-btn" :loading="cancelLoading" @tap="handleCancel">取消订单</button>
      <button v-if="order.type === 'product' && order.installServiceOrderId" class="secondary-btn" @tap="goInstallOrder">查看安装工单</button>
      <button v-else class="secondary-btn" @tap="goChat">在线沟通</button>
    </view>
  </view>
</template>

<script setup>
/**
 * 订单详情页。
 * 1. 订单详情通过真实订单接口获取。
 * 2. 轨迹、催单、取消和报价确认都直接走真实后端。
 * 3. 支付只消费真实预支付参数，不再前端伪造成功状态。
 */
import { computed, ref } from 'vue';
import { onHide, onLoad, onShow, onUnload } from '@dcloudio/uni-app';
import OrderTimeline from '../../components/order-timeline.vue';
import { cancelOrder, confirmQuotation, getOrderDetail, requestWechatPrepay, urgeOrder } from '../../api/order';
import { buildWsUrl } from '../../utils/request';
import { launchWechatPay } from '../../utils/wechat-pay';

const orderId = ref('');
const quoteLoading = ref(false);
const payLoading = ref(false);
const urgeLoading = ref(false);
const cancelLoading = ref(false);
const order = ref({
  timeline: [],
  quotation: null,
});

let socketTask = null;

const canPay = computed(() => {
  if (order.value.type === 'product') {
    return order.value.status === 'PENDING_PAYMENT';
  }
  if (order.value.status === 'PENDING_PAYMENT') {
    return true;
  }
  return order.value.status === 'WAITING_SUPPLEMENT_PAYMENT' && order.value.quotation?.status === 'CONFIRMED';
});

const canUrge = computed(() => ['PENDING_DISPATCH', 'PENDING_ACCEPT', 'ON_THE_WAY'].includes(order.value.status));
const canCancel = computed(() => ['PENDING_PAYMENT', 'PENDING_DISPATCH', 'PENDING_ACCEPT'].includes(order.value.status));

function goTracking() {
  uni.navigateTo({ url: `/pages/order/tracking?id=${order.value.id}` });
}

function goRefund() {
  uni.navigateTo({ url: `/pages/order/refund?id=${order.value.id}` });
}

function goChat() {
  uni.setStorageSync('hzy-chat-session', 'MS-001');
  uni.setStorageSync('hzy-chat-order-id', order.value.id || '');
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
    await loadOrder();
    await handlePay();
  } finally {
    quoteLoading.value = false;
  }
}

async function handlePay() {
  if (!order.value.id) {
    return;
  }
  payLoading.value = true;
  try {
    const prepayRes = await requestWechatPrepay(order.value.id);
    await launchWechatPay(prepayRes.data);
    uni.showToast({
      title: '支付结果确认中，请稍后刷新订单状态',
      icon: 'none',
    });
  } catch (error) {
    if (error?.errMsg?.includes('cancel')) {
      uni.showToast({ title: '你已取消支付，可稍后重试', icon: 'none' });
    }
  } finally {
    payLoading.value = false;
    await loadOrder();
  }
}

async function handleUrge() {
  urgeLoading.value = true;
  try {
    await urgeOrder(order.value.id, '请尽快安排师傅联系我');
    uni.showToast({ title: '已提醒平台加速处理', icon: 'none' });
    await loadOrder();
  } finally {
    urgeLoading.value = false;
  }
}

async function handleCancel() {
  const modal = await new Promise((resolve) => {
    uni.showModal({
      title: '取消订单',
      content: '确认取消当前订单吗？',
      success: resolve,
    });
  });
  if (!modal.confirm) {
    return;
  }
  cancelLoading.value = true;
  try {
    await cancelOrder(order.value.id, '用户主动取消订单');
    uni.showToast({ title: '订单已取消', icon: 'none' });
    await loadOrder();
  } finally {
    cancelLoading.value = false;
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
/* 摘要与报价区 */
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

/* 操作区 */
.order-detail__actions {
  margin-top: 20rpx;
  display: flex;
  flex-wrap: wrap;
  gap: 16rpx;
}

.order-detail__actions button {
  flex: 1;
  min-width: 220rpx;
}
</style>
