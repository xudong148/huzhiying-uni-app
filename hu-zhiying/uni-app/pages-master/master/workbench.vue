<template>
  <view class="page-shell">
    <view v-if="activeOrder.id" class="card master-workbench__summary">
      <view class="master-workbench__title">{{ activeOrder.title }}</view>
      <view class="master-workbench__meta">当前状态：{{ activeOrder.statusText }}</view>
      <view class="master-workbench__meta">服务地址：{{ activeOrder.address }}</view>
      <view class="master-workbench__meta">预约时间：{{ activeOrder.appointment }}</view>
    </view>

    <order-timeline :list="activeOrder.timeline || []"></order-timeline>

    <view class="card master-workbench__section" v-if="activeOrder.id">
      <view class="section-title">
        <text class="section-title__text">现场操作</text>
      </view>
      <view class="master-workbench__grid">
        <view class="master-workbench__action" @tap="handleStatus('ON_THE_WAY', '开始出发')">开始出发</view>
        <view class="master-workbench__action" @tap="handleStatus('ARRIVED', '到场打卡')">到场打卡</view>
        <view class="master-workbench__action" @tap="handleStatus('IN_SERVICE', '开始施工')">开始施工</view>
        <view class="master-workbench__action" @tap="handleQuotation">提交报价</view>
        <view class="master-workbench__action" @tap="handleStatus('COMPLETED', '完工提交')">完工提交</view>
      </view>
    </view>

    <view class="card master-workbench__section">
      <view class="section-title">
        <text class="section-title__text">离线任务队列</text>
      </view>
      <view class="muted">弱网环境下的状态推进和报价会进入离线队列，网络恢复后可继续补传。</view>
      <view class="master-workbench__queue">待同步 {{ offlineCount }} 条</view>
    </view>

    <view v-if="!activeOrder.id" class="card master-workbench__section">
      <view class="muted">当前没有待处理工单，请先去抢派单大厅接单。</view>
    </view>
  </view>
</template>

<script setup>
import { computed, ref } from 'vue';
import { onHide, onLoad, onShow, onUnload } from '@dcloudio/uni-app';
import OrderTimeline from '../../components/order-timeline.vue';
import { createQuotation, getOrderDetail, updateServiceOrderStatus } from '../../api/order';
import { getMasterDashboard } from '../../api/master';
import { enqueueOfflineTask, listOfflineTasks } from '../../utils/offline-queue';
import { buildWsUrl } from '../../utils/request';

const orderId = ref('');
const orders = ref([]);
const offlineCount = ref(0);
let socketTask = null;

const activeOrder = computed(() => {
  if (orderId.value) {
    return orders.value.find((item) => item.id === orderId.value) || {};
  }
  return orders.value[0] || {};
});

function updateOfflineCount() {
  offlineCount.value = listOfflineTasks().length;
}

async function loadWorkbench() {
  updateOfflineCount();
  if (orderId.value) {
    const detailRes = await getOrderDetail(orderId.value);
    orders.value = [detailRes.data];
    return;
  }
  const res = await getMasterDashboard();
  orders.value = res.data.orders || [];
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
    loadWorkbench();
  });
  socketTask.onClose(() => {
    socketTask = null;
  });
}

async function handleStatus(status, label) {
  if (!activeOrder.value.id) {
    return;
  }
  try {
    await updateServiceOrderStatus(activeOrder.value.id, status);
    uni.showToast({ title: `${label}成功`, icon: 'none' });
    await loadWorkbench();
  } catch (error) {
    enqueueOfflineTask({
      type: 'workbench-status',
      orderId: activeOrder.value.id,
      status,
      label,
    });
    updateOfflineCount();
    uni.showToast({ title: '网络异常，已加入离线队列', icon: 'none' });
  }
}

async function handleQuotation() {
  if (!activeOrder.value.id) {
    return;
  }
  try {
    await createQuotation(activeOrder.value.id, '新增辅材与工时费用，请确认后继续施工。');
    uni.showToast({ title: '报价已提交', icon: 'none' });
    await loadWorkbench();
  } catch (error) {
    enqueueOfflineTask({
      type: 'workbench-quotation',
      orderId: activeOrder.value.id,
      label: '报价提交',
    });
    updateOfflineCount();
    uni.showToast({ title: '网络异常，已加入离线队列', icon: 'none' });
  }
}

onLoad((options) => {
  orderId.value = options.orderId || '';
});

onShow(loadWorkbench);
onShow(connectSocket);
onHide(closeSocket);
onUnload(closeSocket);
</script>

<style scoped>
.master-workbench__summary,
.master-workbench__section {
  margin-top: 20rpx;
  padding: 28rpx;
}

.master-workbench__title {
  font-size: 32rpx;
  font-weight: 800;
}

.master-workbench__meta {
  margin-top: 10rpx;
  font-size: 24rpx;
  color: #667085;
}

.master-workbench__grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 14rpx;
}

.master-workbench__action {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 90rpx;
  border-radius: 24rpx;
  background: #eef2ff;
  color: #2b5cff;
  font-size: 24rpx;
  font-weight: 700;
}

.master-workbench__queue {
  margin-top: 16rpx;
  font-size: 24rpx;
  font-weight: 700;
  color: #1c1e26;
}
</style>
