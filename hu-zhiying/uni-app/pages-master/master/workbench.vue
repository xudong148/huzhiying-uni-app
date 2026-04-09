<template>
  <view class="page-shell">
    <view class="card master-workbench__hero">
      <view class="master-workbench__hero-top">
        <view>
          <view class="master-workbench__hero-title">{{ activeOrder.id ? '履约工作台' : '当前暂无履约中的工单' }}</view>
          <view class="master-workbench__hero-desc">{{ heroDesc }}</view>
        </view>
        <text class="chip master-workbench__hero-chip">{{ offlineCount ? `离线待同步 ${offlineCount}` : '状态已同步' }}</text>
      </view>
      <view v-if="activeOrder.id" class="master-workbench__hero-meta">
        <text>订单号 {{ activeOrder.id }}</text>
        <text>预计收入 ¥{{ Number(activeOrder.price || 0).toFixed(2) }}</text>
      </view>
    </view>

    <view v-if="activeOrder.id" class="card master-workbench__summary">
      <view class="master-workbench__summary-top">
        <view class="master-workbench__title">{{ activeOrder.title || '待确认服务工单' }}</view>
        <text class="chip">{{ activeOrder.statusText || '履约处理中' }}</text>
      </view>
      <view class="master-workbench__summary-grid">
        <view class="master-workbench__summary-item">
          <view class="master-workbench__summary-label">服务地址</view>
          <view class="master-workbench__summary-value">{{ activeOrder.address || '待补充服务地址' }}</view>
        </view>
        <view class="master-workbench__summary-item">
          <view class="master-workbench__summary-label">预约时间</view>
          <view class="master-workbench__summary-value">{{ activeOrder.appointment || '以订单页为准' }}</view>
        </view>
      </view>
      <view class="master-workbench__summary-actions">
        <button class="secondary-btn master-workbench__summary-btn" @tap="goMessageCenter">消息中心</button>
        <button class="secondary-btn master-workbench__summary-btn" @tap="safeRefreshWorkbench">刷新工单</button>
      </view>
    </view>

    <view class="card master-workbench__section">
      <view class="section-title">
        <text class="section-title__text">现在建议你做什么</text>
        <text class="section-title__desc">{{ nextAction.hint }}</text>
      </view>
      <view class="master-workbench__focus-title">{{ nextAction.title }}</view>
      <view class="master-workbench__focus-desc">{{ nextAction.desc }}</view>
      <button
        class="primary-btn master-workbench__focus-btn"
        :loading="actionLoading === nextAction.action"
        @tap="triggerAction(nextAction.action)"
      >
        {{ nextAction.buttonText }}
      </button>
    </view>

    <order-timeline v-if="activeOrder.id" :list="activeOrder.timeline || []"></order-timeline>

    <view v-if="activeOrder.id" class="card master-workbench__section">
      <view class="section-title">
        <text class="section-title__text">现场操作</text>
        <text class="section-title__desc">按状态推进，避免误操作</text>
      </view>
      <view class="master-workbench__grid">
        <view
          v-for="item in actionCards"
          :key="item.key"
          class="master-workbench__action pressable"
          :class="{
            'master-workbench__action--disabled': item.disabled,
            'master-workbench__action--highlight': item.key === nextAction.action,
          }"
          @tap="triggerAction(item.key)"
        >
          <view class="master-workbench__action-title">{{ item.title }}</view>
          <view class="master-workbench__action-desc">{{ item.desc }}</view>
        </view>
      </view>
    </view>

    <view class="card master-workbench__section">
      <view class="section-title">
        <text class="section-title__text">离线队列</text>
        <text class="section-title__desc" @tap="safeFlushQueue">立即补发</text>
      </view>
      <view class="muted">{{ offlineHint }}</view>
      <view class="master-workbench__queue">{{ offlineCount ? `待同步 ${offlineCount} 条` : '当前没有待补发任务' }}</view>
    </view>

    <view v-if="!activeOrder.id" class="card master-workbench__section master-workbench__empty">
      <view class="master-workbench__empty-title">先去抢一个适合的工单</view>
      <view class="master-workbench__empty-desc">建议先在抢派单大厅确认距离、服务区域和预估收入，再进入履约工作台。</view>
      <button class="primary-btn master-workbench__empty-btn" @tap="goDispatch">前往抢派单大厅</button>
    </view>
  </view>
</template>

<script setup>
import { computed, ref } from 'vue';
import { onHide, onLoad, onShow, onUnload } from '@dcloudio/uni-app';
import OrderTimeline from '../../components/order-timeline.vue';
import { uploadMedia } from '../../api/file';
import { createQuotation, getOrderDetail, updateServiceOrderStatus } from '../../api/order';
import { checkInOrder, getMasterDashboard, uploadAfterWorkMedia, uploadBeforeWorkMedia } from '../../api/master';
import { useLocationStore } from '../../stores/location';
import { safeAsync, showActionError } from '../../utils/page-task';
import { enqueueOfflineTask, flushOfflineQueue, listOfflineTasks } from '../../utils/offline-queue';
import { buildWsUrl } from '../../utils/request';

const QUOTATION_REMARKS = [
  '新增辅材费用，请确认后继续施工。',
  '需要增加人工工时，请确认补款后继续。',
  '发现隐蔽问题，需补充维修项目。',
];

const locationStore = useLocationStore();
const orderId = ref('');
const orders = ref([]);
const offlineCount = ref(0);
const actionLoading = ref('');
const pendingQuotationRemark = ref('');
const pendingMediaTask = ref(null);

let socketTask = null;
let socketOrderId = '';

const activeOrder = computed(() => {
  if (orderId.value) {
    return orders.value.find((item) => item.id === orderId.value) || {};
  }
  return orders.value[0] || {};
});

const heroDesc = computed(() => {
  if (!activeOrder.value.id) {
    return '工作台只展示你当前正在履约的工单。抢单成功后，状态推进、签到、拍照和离线补发都会在这里完成。';
  }

  const statusMap = {
    PENDING_ACCEPT: '你已经接到订单，建议先点击“开始出发”，让用户知道你正在赶往现场。',
    ON_THE_WAY: '当前重点是尽快到场签到。签到会记录位置，方便平台核对履约过程。',
    ARRIVED: '已经到场后，先确认现场情况，再点击“开始施工”进入施工状态。',
    WAITING_SUPPLEMENT_PAYMENT: '这笔工单正在等待用户补款确认，建议先查看消息并保留现场凭证。',
    IN_SERVICE: '施工中请优先补齐现场照片和必要报价，完工后再提交完工。',
    COMPLETED: '当前工单已完工，可以回到抢派单大厅继续接单，或在钱包里查看结算变化。',
  };
  return statusMap[activeOrder.value.status] || '请先确认当前状态，再按页面建议一步步推进，避免跳步操作。';
});

const offlineHint = computed(() => (
  offlineCount.value
    ? '弱网下的状态推进、签到和现场照片会进入离线队列。网络恢复后系统会自动补发，你也可以手动同步。'
    : '当前网络同步正常。弱网时的状态推进、签到和现场照片会自动进入离线队列。'
));

const nextAction = computed(() => {
  if (!activeOrder.value.id) {
    return {
      action: 'go-dispatch',
      title: '先去抢派单大厅',
      desc: '没有履约中的工单时，工作台不会展示施工动作。先去确认距离和收入，再决定是否接单。',
      buttonText: '前往抢派单大厅',
      hint: '没有进行中的工单',
    };
  }

  const map = {
    PENDING_ACCEPT: {
      action: 'start-route',
      title: '开始出发',
      desc: '接单后第一步是同步“我已出发”。这能减少用户焦虑，也能让平台知道你已进入履约。',
      buttonText: '开始出发',
      hint: '接单后先同步行程',
    },
    ON_THE_WAY: {
      action: 'check-in',
      title: '到场签到',
      desc: '到场后尽快签到。签到会记录当前定位，是后续售后、争议处理的重要凭证。',
      buttonText: '到场签到',
      hint: '到场后尽快留痕',
    },
    ARRIVED: {
      action: 'start-service',
      title: '开始施工',
      desc: '已到场并确认现场情况后，再点击开始施工。进入施工中后，用户也能看到状态变化。',
      buttonText: '开始施工',
      hint: '确认现场再开工',
    },
    WAITING_SUPPLEMENT_PAYMENT: {
      action: 'refresh',
      title: '等待用户补款确认',
      desc: '报价已经提交，当前不要继续推进完工。建议先去消息中心和用户确认，再刷新工单状态。',
      buttonText: '刷新工单状态',
      hint: '等待补款确认',
    },
    IN_SERVICE: {
      action: 'complete',
      title: '准备完工提交',
      desc: '完工前请确认现场照片、施工说明和必要报价都已经提交，再点击完工提交。',
      buttonText: '完工提交',
      hint: '完工前先补齐凭证',
    },
    COMPLETED: {
      action: 'go-dispatch',
      title: '这单已经完成',
      desc: '你可以继续去抢派单大厅接新单，或者到钱包查看结算和冻结金变化。',
      buttonText: '继续去接单',
      hint: '当前工单已闭环',
    },
  };

  return map[activeOrder.value.status] || {
    action: 'refresh',
    title: '先刷新当前工单',
    desc: '当前状态需要从服务端再确认一次，建议先刷新工单，确认后再继续操作。',
    buttonText: '刷新工单',
    hint: '状态待确认',
  };
});

function getActionAvailability(actionKey) {
  const status = activeOrder.value.status;
  const matrix = {
    'start-route': {
      enabled: ['PENDING_ACCEPT'].includes(status),
      blockReason: '只有接单成功后才能开始出发。',
    },
    'check-in': {
      enabled: ['ON_THE_WAY', 'ARRIVED'].includes(status),
      blockReason: '请先出发，到场后再签到。',
    },
    'start-service': {
      enabled: ['ARRIVED'].includes(status),
      blockReason: '到场签到后才能开始施工。',
    },
    quote: {
      enabled: ['ARRIVED', 'IN_SERVICE'].includes(status),
      blockReason: '只有到场确认或施工中才能提交增项报价。',
    },
    'upload-before': {
      enabled: ['PENDING_ACCEPT', 'ON_THE_WAY', 'ARRIVED', 'IN_SERVICE', 'WAITING_SUPPLEMENT_PAYMENT'].includes(status),
      blockReason: '建议在完工前上传施工前照片。',
    },
    'upload-after': {
      enabled: ['IN_SERVICE', 'WAITING_SUPPLEMENT_PAYMENT', 'COMPLETED'].includes(status),
      blockReason: '施工中或完工后再上传完工照片。',
    },
    complete: {
      enabled: ['IN_SERVICE', 'WAITING_SUPPLEMENT_PAYMENT'].includes(status),
      blockReason: '施工中并补齐凭证后才能完工提交。',
    },
    refresh: {
      enabled: true,
      blockReason: '',
    },
    'go-dispatch': {
      enabled: true,
      blockReason: '',
    },
  };
  return matrix[actionKey] || { enabled: false, blockReason: '当前状态暂不支持此操作。' };
}

const actionCards = computed(() => ([
  {
    key: 'start-route',
    title: '开始出发',
    desc: '接单后第一时间同步行程',
  },
  {
    key: 'check-in',
    title: '到场签到',
    desc: '记录定位，留下到场凭证',
  },
  {
    key: 'start-service',
    title: '开始施工',
    desc: '确认现场无误后再开工',
  },
  {
    key: 'quote',
    title: '提交增项报价',
    desc: '新增辅材或工时时使用',
  },
  {
    key: 'upload-before',
    title: '施工前拍照',
    desc: '建议开工前保留现场照片',
  },
  {
    key: 'upload-after',
    title: '完工拍照',
    desc: '完工前后都要有可追溯凭证',
  },
  {
    key: 'complete',
    title: '完工提交',
    desc: '确认施工完成后再提交',
  },
]).map((item) => ({
  ...item,
  disabled: !getActionAvailability(item.key).enabled,
  blockReason: getActionAvailability(item.key).blockReason,
})));

function updateOfflineCount() {
  offlineCount.value = listOfflineTasks().length;
}

function mergeDashboardOrders(data = {}) {
  const merged = [];
  [data.currentOrder, ...(data.orders || [])].forEach((item) => {
    if (!item?.id || merged.some((existing) => existing.id === item.id)) {
      return;
    }
    merged.push(item);
  });
  return merged;
}

function closeSocket() {
  if (socketTask) {
    socketTask.close({});
    socketTask = null;
  }
  socketOrderId = '';
}

function syncSocketConnection() {
  const targetOrderId = orderId.value || activeOrder.value.id;
  if (!targetOrderId || typeof uni.connectSocket !== 'function') {
    closeSocket();
    return;
  }
  if (socketTask && socketOrderId === targetOrderId) {
    return;
  }

  closeSocket();
  socketOrderId = targetOrderId;
  socketTask = uni.connectSocket({
    url: buildWsUrl(`/ws/orders/${targetOrderId}`),
  });
  socketTask.onOpen(() => {
    socketTask?.send({ data: 'ping' });
  });
  socketTask.onMessage(() => {
    safeRefreshWorkbench();
  });
  socketTask.onClose(() => {
    socketTask = null;
    socketOrderId = '';
  });
}

async function loadWorkbench() {
  updateOfflineCount();
  if (orderId.value) {
    const detailRes = await getOrderDetail(orderId.value);
    orders.value = [detailRes.data];
    syncSocketConnection();
    return;
  }

  const res = await getMasterDashboard();
  orders.value = mergeDashboardOrders(res.data);
  syncSocketConnection();
}

const safeRefreshWorkbench = safeAsync(loadWorkbench, '刷新师傅工作台');

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

async function flushQueueManually(options = {}) {
  const silent = options.silent === true;
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
  if (silent) {
    return result;
  }
  if (result.total) {
    uni.showToast({
      title: result.failed ? `已补发，剩余 ${result.failed} 条` : '离线任务已同步',
      icon: 'none',
    });
  } else {
    uni.showToast({ title: '当前没有待补发任务', icon: 'none' });
  }
}

const safeFlushQueue = safeAsync(flushQueueManually, '同步离线任务');

async function handleStatus(status, label) {
  await updateServiceOrderStatus(activeOrder.value.id, status);
  uni.showToast({ title: `${label}成功`, icon: 'none' });
  await loadWorkbench();
}

async function pickQuotationRemark() {
  try {
    const result = await uni.showActionSheet({
      itemList: QUOTATION_REMARKS,
      itemColor: '#1c1e26',
    });
    if (typeof result.tapIndex === 'number') {
      return QUOTATION_REMARKS[result.tapIndex];
    }
  } catch (error) {
    if (String(error?.errMsg || '').includes('cancel')) {
      return '';
    }
    throw error;
  }
  return '';
}

async function handleQuotation() {
  const remark = await pickQuotationRemark();
  if (!remark) {
    return;
  }
  pendingQuotationRemark.value = remark;
  await createQuotation(activeOrder.value.id, remark);
  pendingQuotationRemark.value = '';
  uni.showToast({ title: '报价已提交', icon: 'none' });
  await loadWorkbench();
}

async function handleCheckIn() {
  const current = await locationStore.locate();
  const payload = {
    latitude: current.latitude,
    longitude: current.longitude,
    accuracy: 30,
  };
  const result = await checkInOrder(activeOrder.value.id, payload);
  const text = result.data?.verified
    ? `签到成功，距服务点约 ${result.data.distanceMeters} 米`
    : '已记录到场签到';
  uni.showToast({ title: text, icon: 'none' });
  await loadWorkbench();
}

async function uploadStageMedia(stage) {
  let chooseRes;
  try {
    chooseRes = await uni.chooseMedia({
      count: 3,
      mediaType: ['image'],
      sourceType: ['album', 'camera'],
    });
  } catch (error) {
    if (String(error?.errMsg || '').includes('cancel')) {
      return;
    }
    throw error;
  }

  const files = chooseRes.tempFiles || [];
  if (!files.length) {
    return;
  }

  const tempFilePaths = files.map((item) => item.tempFilePath || item.path).filter(Boolean);
  const note = stage === 'before' ? '施工前现场照片' : '完工现场照片';
  pendingMediaTask.value = {
    stage,
    tempFilePaths,
    note,
  };
  await bindStageMedia(stage, activeOrder.value.id, tempFilePaths, note);
  pendingMediaTask.value = null;
  uni.showToast({
    title: stage === 'before' ? '施工前照片已上传' : '完工照片已上传',
    icon: 'none',
  });
  await loadWorkbench();
}

async function runAction(actionKey) {
  switch (actionKey) {
    case 'start-route':
      await handleStatus('ON_THE_WAY', '开始出发');
      break;
    case 'check-in':
      await handleCheckIn();
      break;
    case 'start-service':
      await handleStatus('IN_SERVICE', '开始施工');
      break;
    case 'quote':
      await handleQuotation();
      break;
    case 'upload-before':
      await uploadStageMedia('before');
      break;
    case 'upload-after':
      await uploadStageMedia('after');
      break;
    case 'complete':
      await handleStatus('COMPLETED', '完工提交');
      break;
    case 'refresh':
      await loadWorkbench();
      uni.showToast({ title: '工单状态已刷新', icon: 'none' });
      break;
    case 'go-dispatch':
      goDispatch();
      break;
    default:
      break;
  }
}

async function enqueueFailedAction(actionKey, error) {
  if (!activeOrder.value.id) {
    showActionError(error, '操作失败');
    return;
  }

  if (actionKey === 'start-route') {
    enqueueOfflineTask({
      type: 'workbench-status',
      orderId: activeOrder.value.id,
      status: 'ON_THE_WAY',
      label: '开始出发',
    });
  } else if (actionKey === 'start-service') {
    enqueueOfflineTask({
      type: 'workbench-status',
      orderId: activeOrder.value.id,
      status: 'IN_SERVICE',
      label: '开始施工',
    });
  } else if (actionKey === 'complete') {
    enqueueOfflineTask({
      type: 'workbench-status',
      orderId: activeOrder.value.id,
      status: 'COMPLETED',
      label: '完工提交',
    });
  } else if (actionKey === 'check-in') {
    const current = await locationStore.locate();
    enqueueOfflineTask({
      type: 'workbench-checkin',
      orderId: activeOrder.value.id,
      payload: {
        latitude: current.latitude,
        longitude: current.longitude,
        accuracy: 30,
      },
    });
  } else if (actionKey === 'quote') {
    enqueueOfflineTask({
      type: 'workbench-quotation',
      orderId: activeOrder.value.id,
      remark: pendingQuotationRemark.value || QUOTATION_REMARKS[0],
    });
  } else if (actionKey === 'upload-before' && pendingMediaTask.value?.tempFilePaths?.length) {
    enqueueOfflineTask({
      type: 'workbench-media-before',
      orderId: activeOrder.value.id,
      tempFilePaths: pendingMediaTask.value.tempFilePaths,
      note: pendingMediaTask.value.note,
    });
  } else if (actionKey === 'upload-after' && pendingMediaTask.value?.tempFilePaths?.length) {
    enqueueOfflineTask({
      type: 'workbench-media-after',
      orderId: activeOrder.value.id,
      tempFilePaths: pendingMediaTask.value.tempFilePaths,
      note: pendingMediaTask.value.note,
    });
  } else {
    showActionError(error, '操作失败');
    return;
  }

  pendingQuotationRemark.value = '';
  pendingMediaTask.value = null;
  updateOfflineCount();
  uni.showToast({ title: '网络异常，已加入离线队列', icon: 'none' });
}

async function triggerAction(actionKey) {
  if (actionLoading.value) {
    return;
  }

  if (actionKey !== 'go-dispatch' && !activeOrder.value.id) {
    goDispatch();
    return;
  }

  const availability = getActionAvailability(actionKey);
  if (!availability.enabled) {
    uni.showToast({ title: availability.blockReason || '当前状态暂不支持此操作', icon: 'none' });
    return;
  }

  actionLoading.value = actionKey;
  try {
    await runAction(actionKey);
  } catch (error) {
    try {
      await enqueueFailedAction(actionKey, error);
    } catch (queueError) {
      showActionError(queueError, '操作失败');
    }
  } finally {
    actionLoading.value = '';
  }
}

function goDispatch() {
  uni.navigateTo({ url: '/pages-master/master/dispatch' });
}

function goMessageCenter() {
  uni.switchTab({ url: '/pages/message/index' });
}

onLoad((options) => {
  orderId.value = options.orderId || '';
});

onShow(safeAsync(async () => {
  await flushQueueManually({ silent: true });
  await loadWorkbench();
}, '初始化师傅工作台'));

onHide(closeSocket);
onUnload(closeSocket);
</script>

<style scoped>
.master-workbench__hero,
.master-workbench__summary,
.master-workbench__section {
  padding: 28rpx;
}

.master-workbench__hero {
  background: linear-gradient(135deg, #1c1e26 0%, #2c3858 100%);
  color: #ffffff;
}

.master-workbench__hero-top,
.master-workbench__summary-top {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 16rpx;
}

.master-workbench__hero-title,
.master-workbench__title,
.master-workbench__focus-title,
.master-workbench__empty-title {
  font-size: 32rpx;
  font-weight: 800;
  color: #1c1e26;
}

.master-workbench__hero-title {
  color: #ffffff;
}

.master-workbench__hero-desc,
.master-workbench__focus-desc,
.master-workbench__empty-desc {
  margin-top: 10rpx;
  font-size: 24rpx;
  line-height: 1.7;
  color: #667085;
}

.master-workbench__hero-desc {
  color: rgba(255, 255, 255, 0.84);
}

.master-workbench__hero-chip {
  flex-shrink: 0;
  background: rgba(255, 255, 255, 0.16);
  color: #ffffff;
}

.master-workbench__hero-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 12rpx 20rpx;
  margin-top: 18rpx;
  font-size: 22rpx;
  color: rgba(255, 255, 255, 0.82);
}

.master-workbench__summary,
.master-workbench__section {
  margin-top: 20rpx;
}

.master-workbench__summary-grid {
  display: grid;
  gap: 18rpx;
  margin-top: 20rpx;
}

.master-workbench__summary-item {
  padding: 20rpx;
  border-radius: 20rpx;
  background: #f6f8fc;
}

.master-workbench__summary-label {
  font-size: 22rpx;
  color: #8b90a0;
}

.master-workbench__summary-value {
  margin-top: 8rpx;
  font-size: 26rpx;
  color: #1c1e26;
  line-height: 1.6;
  font-weight: 600;
}

.master-workbench__summary-actions {
  display: flex;
  gap: 16rpx;
  margin-top: 20rpx;
}

.master-workbench__summary-btn {
  flex: 1;
}

.master-workbench__focus-btn,
.master-workbench__empty-btn {
  margin-top: 24rpx;
}

.master-workbench__grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 14rpx;
}

.master-workbench__action {
  min-height: 148rpx;
  padding: 24rpx 22rpx;
  border-radius: 24rpx;
  background: #f6f8fc;
  border: 2rpx solid transparent;
}

.master-workbench__action--highlight {
  background: rgba(43, 92, 255, 0.08);
  border-color: rgba(43, 92, 255, 0.16);
}

.master-workbench__action--disabled {
  opacity: 0.45;
}

.master-workbench__action-title {
  font-size: 26rpx;
  font-weight: 700;
  color: #1c1e26;
}

.master-workbench__action-desc {
  margin-top: 10rpx;
  font-size: 22rpx;
  line-height: 1.6;
  color: #667085;
}

.master-workbench__queue {
  margin-top: 16rpx;
  font-size: 26rpx;
  font-weight: 700;
  color: #1c1e26;
}

.master-workbench__empty {
  text-align: center;
}
</style>
