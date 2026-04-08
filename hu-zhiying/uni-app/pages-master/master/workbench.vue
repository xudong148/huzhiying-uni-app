<template>
  <view class="page-shell">
    <!-- 工单摘要 -->
    <view v-if="activeOrder.id" class="card master-workbench__summary">
      <view class="master-workbench__title">{{ activeOrder.title }}</view>
      <view class="master-workbench__meta">当前状态：{{ activeOrder.statusText }}</view>
      <view class="master-workbench__meta">服务地址：{{ activeOrder.address }}</view>
      <view class="master-workbench__meta">预约时间：{{ activeOrder.appointment }}</view>
    </view>

    <!-- 状态时间线 -->
    <order-timeline :list="activeOrder.timeline || []"></order-timeline>

    <!-- 现场操作 -->
    <view v-if="activeOrder.id" class="card master-workbench__section">
      <view class="section-title">
        <text class="section-title__text">现场操作</text>
      </view>
      <view class="master-workbench__grid">
        <view class="master-workbench__action" @tap="handleStatus('ON_THE_WAY', '开始出发')">开始出发</view>
        <view class="master-workbench__action" @tap="handleCheckIn">到场签到</view>
        <view class="master-workbench__action" @tap="handleStatus('IN_SERVICE', '开始施工')">开始施工</view>
        <view class="master-workbench__action" @tap="handleQuotation">提交报价</view>
        <view class="master-workbench__action" @tap="uploadStageMedia('before')">施工前拍照</view>
        <view class="master-workbench__action" @tap="uploadStageMedia('after')">完工拍照</view>
        <view class="master-workbench__action" @tap="handleStatus('COMPLETED', '完工提交')">完工提交</view>
      </view>
    </view>

    <!-- 离线队列 -->
    <view class="card master-workbench__section">
      <view class="section-title">
        <text class="section-title__text">离线任务队列</text>
        <text class="section-title__desc" @tap="flushQueueManually">立即补发</text>
      </view>
      <view class="muted">弱网环境下的状态推进、签到和媒体上传会进入离线队列，网络恢复后继续补发。</view>
      <view class="master-workbench__queue">待同步 {{ offlineCount }} 条</view>
    </view>

    <!-- 空状态 -->
    <view v-if="!activeOrder.id" class="card master-workbench__section">
      <view class="muted">当前没有待处理工单，请先去抢派单大厅接单。</view>
    </view>
  </view>
</template>

<script setup>
/**
 * 师傅工作台。
 * 1. 状态推进、签到、施工前后媒体都走真实接口。
 * 2. 网络失败时写入离线队列，恢复后按原始请求重新补发。
 */
import { computed, ref } from 'vue';
import { onHide, onLoad, onShow, onUnload } from '@dcloudio/uni-app';
import OrderTimeline from '../../components/order-timeline.vue';
import { uploadMedia } from '../../api/file';
import { createQuotation, getOrderDetail, updateServiceOrderStatus } from '../../api/order';
import { checkInOrder, getMasterDashboard, uploadAfterWorkMedia, uploadBeforeWorkMedia } from '../../api/master';
import { useLocationStore } from '../../stores/location';
import { enqueueOfflineTask, flushOfflineQueue, listOfflineTasks } from '../../utils/offline-queue';
import { buildWsUrl } from '../../utils/request';

const locationStore = useLocationStore();
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

async function bindStageMedia(stage, targetOrderId, tempFilePaths, note) {
  const bizType = stage === 'before' ? 'before_work_media' : 'after_work_media';
  const uploaded = await Promise.all(
    tempFilePaths.map((filePath) => uploadMedia(filePath, bizType, targetOrderId)),
  );
  const fileIds = uploaded.map((item) => item.data.fileId);
  const payload = { fileIds, note };
  if (stage === 'before') {
    await uploadBeforeWorkMedia(targetOrderId, payload);
    return;
  }
  await uploadAfterWorkMedia(targetOrderId, payload);
}

async function flushQueueManually() {
  const result = await flushOfflineQueue(async (item) => {
    switch (item.type) {
      case 'workbench-status':
        await updateServiceOrderStatus(item.orderId, item.status);
        break;
      case 'workbench-quotation':
        await createQuotation(item.orderId, item.remark);
        break;
      case 'workbench-checkin':
        await checkInOrder(item.orderId, item.payload);
        break;
      case 'workbench-media-before':
        await bindStageMedia('before', item.orderId, item.tempFilePaths || [], item.note);
        break;
      case 'workbench-media-after':
        await bindStageMedia('after', item.orderId, item.tempFilePaths || [], item.note);
        break;
      default:
        break;
    }
  });

  updateOfflineCount();
  if (result.total) {
    uni.showToast({
      title: result.failed ? `已补发，剩余 ${result.failed} 条` : '离线任务已同步',
      icon: 'none',
    });
  }
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
  const remark = '新增辅材与工时费用，请确认后继续施工。';
  try {
    await createQuotation(activeOrder.value.id, remark);
    uni.showToast({ title: '报价已提交', icon: 'none' });
    await loadWorkbench();
  } catch (error) {
    enqueueOfflineTask({
      type: 'workbench-quotation',
      orderId: activeOrder.value.id,
      remark,
    });
    updateOfflineCount();
    uni.showToast({ title: '网络异常，已加入离线队列', icon: 'none' });
  }
}

async function handleCheckIn() {
  if (!activeOrder.value.id) {
    return;
  }
  const current = await locationStore.locate();
  const payload = {
    latitude: current.latitude,
    longitude: current.longitude,
    accuracy: 30,
  };

  try {
    const result = await checkInOrder(activeOrder.value.id, payload);
    const text = result.data?.verified
      ? `签到成功，距服务点约 ${result.data.distanceMeters} 米`
      : '已记录到场签到';
    uni.showToast({ title: text, icon: 'none' });
    await loadWorkbench();
  } catch (error) {
    enqueueOfflineTask({
      type: 'workbench-checkin',
      orderId: activeOrder.value.id,
      payload,
    });
    updateOfflineCount();
    uni.showToast({ title: '网络异常，已加入离线队列', icon: 'none' });
  }
}

async function uploadStageMedia(stage) {
  if (!activeOrder.value.id) {
    return;
  }

  const chooseRes = await uni.chooseMedia({
    count: 3,
    mediaType: ['image'],
    sourceType: ['album', 'camera'],
  });
  const files = chooseRes.tempFiles || [];
  if (!files.length) {
    return;
  }

  const tempFilePaths = files.map((item) => item.tempFilePath || item.path).filter(Boolean);
  const note = stage === 'before' ? '施工前现场照片' : '完工现场照片';

  try {
    await bindStageMedia(stage, activeOrder.value.id, tempFilePaths, note);
    uni.showToast({
      title: stage === 'before' ? '施工前照片已上传' : '完工照片已上传',
      icon: 'none',
    });
    await loadWorkbench();
  } catch (error) {
    enqueueOfflineTask({
      type: stage === 'before' ? 'workbench-media-before' : 'workbench-media-after',
      orderId: activeOrder.value.id,
      tempFilePaths,
      note,
    });
    updateOfflineCount();
    uni.showToast({ title: '网络异常，已加入离线队列', icon: 'none' });
  }
}

onLoad((options) => {
  orderId.value = options.orderId || '';
});

onShow(async () => {
  await flushQueueManually();
  await loadWorkbench();
  connectSocket();
});

onHide(closeSocket);
onUnload(closeSocket);
</script>

<style scoped>
/* 摘要与区块 */
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

/* 操作网格 */
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
