<template>
  <view class="page-shell">
    <view class="card order-detail__summary">
      <view class="order-detail__title">{{ order.title || '订单详情' }}</view>
      <view class="order-detail__status-row">
        <text class="chip">{{ order.statusText || '处理中' }}</text>
        <text class="order-detail__next-step">{{ nextStepText }}</text>
      </view>
      <view class="order-detail__meta">订单号：{{ order.id || '--' }}</view>
      <view class="order-detail__meta">预约时间：{{ order.appointment || '待确认' }}</view>
      <view class="order-detail__meta">服务地址：{{ order.address || '待补充地址' }}</view>
      <view v-if="order.type === 'service'" class="order-detail__meta">
        服务师傅：{{ order.masterName || '待接单' }} {{ order.masterMobile || '' }}
      </view>
      <view v-if="order.type === 'service'" class="order-detail__meta">预计到达：{{ order.eta || '待确认' }}</view>
      <view v-if="order.type === 'product' && order.installServiceOrderId" class="order-detail__meta">
        安装工单：{{ order.installServiceOrderId }}
      </view>
      <view class="order-detail__amount">订单金额：¥{{ Number(order.price || 0).toFixed(2) }}</view>
    </view>

    <view v-if="order.type === 'service' && order.timeline?.length" class="order-detail__section-title section-title">
      <text class="section-title__text">履约时间线</text>
      <text class="section-title__desc" @tap="goTracking">查看轨迹</text>
    </view>
    <order-timeline v-if="order.type === 'service' && order.timeline?.length" :list="order.timeline"></order-timeline>

    <view v-if="order.type === 'service' && !order.timeline?.length" class="card order-detail__section">
      <view class="section-title">
        <text class="section-title__text">履约状态</text>
      </view>
      <view class="order-detail__placeholder">当前还没有轨迹更新，平台派单或师傅开始履约后会实时展示在这里。</view>
    </view>

    <view v-if="order.messageSummary" class="card order-detail__section">
      <view class="section-title">
        <text class="section-title__text">沟通摘要</text>
        <text class="section-title__desc">{{ order.messageSummary.messageCount }} 条消息</text>
      </view>
      <view class="order-detail__quote-remark">{{ order.messageSummary.latestMessage || '暂无沟通记录' }}</view>
    </view>

    <view v-if="order.quotation" class="card order-detail__section">
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
      <view class="order-detail__quote-remark">{{ order.quotation.remark || '报价说明待补充' }}</view>
      <button
        v-if="order.status === 'WAITING_SUPPLEMENT_PAYMENT' && order.quotation?.status !== 'CONFIRMED'"
        class="primary-btn order-detail__btn"
        :loading="quoteLoading"
        @tap="safeHandleConfirmQuotation"
      >
        确认并补款
      </button>
    </view>

    <view class="card order-detail__section">
      <view class="section-title">
        <text class="section-title__text">你现在可以做什么</text>
      </view>
      <view class="order-detail__action-copy">1. 支付失败或取消后，可以直接回到这里继续支付。</view>
      <view class="order-detail__action-copy">2. 履约过程中可催单、看轨迹、查看沟通记录。</view>
      <view class="order-detail__action-copy">3. 对结果不满意时，可从这里进入售后申请。</view>
    </view>

    <view class="card order-detail__actions">
      <button v-if="canPay" class="primary-btn" :loading="payLoading" @tap="safeHandlePay">继续支付</button>
      <button class="secondary-btn" @tap="goRefund">申请售后</button>
      <button v-if="order.type === 'service' && canUrge" class="secondary-btn" :loading="urgeLoading" @tap="safeHandleUrge">催单</button>
      <button v-if="order.type === 'service' && canCancel" class="secondary-btn" :loading="cancelLoading" @tap="safeHandleCancel">取消订单</button>
      <button v-if="order.type === 'product' && order.installServiceOrderId" class="secondary-btn" @tap="goInstallOrder">查看安装工单</button>
      <button v-else class="secondary-btn" @tap="goChat">在线沟通</button>
    </view>
  </view>
</template>

<script setup>
import { computed, ref } from 'vue';
import { onHide, onLoad, onShow, onUnload } from '@dcloudio/uni-app';
import OrderTimeline from '../../components/order-timeline.vue';
import { cancelOrder, confirmQuotation, getOrderDetail, requestWechatPrepay, urgeOrder } from '../../api/order';
import { safeAsync } from '../../utils/page-task';
import { setPendingChatTarget } from '../../utils/message-center';
import { buildWsUrl, getRequestErrorMessage } from '../../utils/request';
import { isWechatPayCanceled, launchWechatPay } from '../../utils/wechat-pay';

const orderId = ref('');
const quoteLoading = ref(false);
const payLoading = ref(false);
const urgeLoading = ref(false);
const cancelLoading = ref(false);
const order = ref({
  timeline: [],
  quotation: null,
  messageSummary: null,
});

let socketTask = null;

const canPay = computed(() => {
  if (order.value.type === 'product') {
    return order.value.status === 'PENDING_PAYMENT';
  }
  return Boolean(order.value.canPay || order.value.status === 'WAITING_SUPPLEMENT_PAYMENT');
});

const canUrge = computed(() => Boolean(order.value.canUrge));
const canCancel = computed(() => Boolean(order.value.canCancel));

const nextStepText = computed(() => {
  const status = order.value.status;
  if (status === 'PENDING_PAYMENT') {
    return '完成支付后平台才会开始处理';
  }
  if (status === 'PENDING_DISPATCH') {
    return '平台正在匹配可服务师傅';
  }
  if (status === 'PENDING_ACCEPT') {
    return '等待师傅确认上门';
  }
  if (status === 'ON_THE_WAY' || status === 'ARRIVED' || status === 'IN_SERVICE') {
    return '履约进行中，可关注轨迹和沟通消息';
  }
  if (status === 'WAITING_SUPPLEMENT_PAYMENT') {
    return '请先确认报价并补款，施工才能继续';
  }
  if (status === 'REFUNDING') {
    return '售后处理中，请保持消息可联系';
  }
  if (status === 'COMPLETED') {
    return '订单已完成，如有问题可继续申请售后';
  }
  if (status === 'CANCELLED') {
    return '订单已取消，可重新下单或联系平台';
  }
  return '订单状态更新后会在这里同步提醒';
});

function goTracking() {
  uni.navigateTo({ url: `/pages/order/tracking?id=${order.value.id}` });
}

function goRefund() {
  uni.navigateTo({ url: `/pages/order/refund?id=${order.value.id}` });
}

function goChat() {
  setPendingChatTarget({
    sessionId: order.value.messageSummary?.sessionId || 'MS-001',
    orderId: order.value.id || '',
    autoOpen: true,
  });
  uni.switchTab({ url: '/pages/message/index' });
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
    await safeLoadOrder();
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
    if (isWechatPayCanceled(error)) {
      uni.showToast({ title: '你已取消支付，可稍后重试', icon: 'none' });
    } else {
      uni.showToast({
        title: getRequestErrorMessage(error, '支付暂不可用'),
        icon: 'none',
      });
    }
  } finally {
    payLoading.value = false;
    await safeLoadOrder();
  }
}

async function handleUrge() {
  urgeLoading.value = true;
  try {
    await urgeOrder(order.value.id, '请尽快安排师傅联系我');
    uni.showToast({ title: '已提醒平台加速处理', icon: 'none' });
    await safeLoadOrder();
  } finally {
    urgeLoading.value = false;
  }
}

async function handleCancel() {
  const modal = await new Promise((resolve) => {
    uni.showModal({
      title: '取消订单',
      content: '确认取消当前订单吗？如果已经有师傅在路上，建议先沟通确认。',
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
    await safeLoadOrder();
  } finally {
    cancelLoading.value = false;
  }
}

async function loadOrder() {
  if (!orderId.value) {
    return;
  }
  const res = await getOrderDetail(orderId.value);
  order.value = res.data || {};
}

const safeLoadOrder = safeAsync(loadOrder, '加载订单详情');
const safeHandlePay = safeAsync(handlePay, '继续支付');
const safeHandleUrge = safeAsync(handleUrge, '催单');
const safeHandleCancel = safeAsync(handleCancel, '取消订单');
const safeHandleConfirmQuotation = safeAsync(handleConfirmQuotation, '确认报价');

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
    safeLoadOrder();
  });
  socketTask.onClose(() => {
    socketTask = null;
  });
}

onLoad((options) => {
  orderId.value = options.id || '';
});

onShow(safeLoadOrder);
onShow(connectSocket);
onHide(closeSocket);
onUnload(closeSocket);
</script>

<style scoped>
.order-detail__summary,
.order-detail__section,
.order-detail__actions {
  padding: 28rpx;
}

.order-detail__title {
  font-size: 32rpx;
  font-weight: 800;
}

.order-detail__status-row {
  margin-top: 14rpx;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16rpx;
}

.order-detail__next-step {
  flex: 1;
  text-align: right;
  font-size: 22rpx;
  color: #667085;
}

.order-detail__meta {
  margin-top: 12rpx;
  font-size: 24rpx;
  color: #667085;
  line-height: 1.7;
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

.order-detail__section {
  margin-top: 20rpx;
}

.order-detail__placeholder,
.order-detail__action-copy {
  font-size: 24rpx;
  line-height: 1.7;
  color: #475467;
}

.order-detail__action-copy + .order-detail__action-copy {
  margin-top: 10rpx;
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
  flex-wrap: wrap;
  gap: 16rpx;
}

.order-detail__actions button {
  flex: 1;
  min-width: 220rpx;
}
</style>
