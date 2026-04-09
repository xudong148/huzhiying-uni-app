<template>
  <view class="page-shell">
    <view class="card master-dispatch__hero">
      <view class="master-dispatch__title">抢派单大厅</view>
      <view class="master-dispatch__desc">
        当前为你推送 {{ orders.length }} 个可接订单。建议先看距离、预估收入和服务地址，再决定是否抢单。
      </view>
    </view>

    <view class="card master-dispatch__notice">
      <view class="master-dispatch__notice-title">接单提醒</view>
      <view class="master-dispatch__notice-text">1. 抢单成功后会立刻进入履约工作台。</view>
      <view class="master-dispatch__notice-text">2. 距离和区域受接单设置影响，建议先配置接单范围。</view>
    </view>

    <view v-for="item in orders" :key="item.id" class="card master-dispatch__card">
      <view class="master-dispatch__top">
        <view class="master-dispatch__name">{{ item.title }}</view>
        <text class="chip">{{ item.distance || '距离待计算' }}</text>
      </view>
      <view class="master-dispatch__meta">{{ item.address || '待补充服务区域' }}</view>
      <view class="master-dispatch__tags">
        <text v-for="tag in item.tags" :key="tag" class="tag">{{ tag }}</text>
      </view>
      <view class="master-dispatch__bottom">
        <text class="master-dispatch__price">预估收入 ¥{{ Number(item.income || 0).toFixed(2) }}</text>
        <button class="primary-btn master-dispatch__btn" :loading="loadingTaskId === item.id" @tap="grab(item)">立即抢单</button>
      </view>
    </view>

    <view v-if="!orders.length" class="card master-dispatch__empty">
      <view class="master-dispatch__empty-title">暂时没有可抢订单</view>
      <view class="master-dispatch__empty-desc">系统会继续推送符合你技能和距离范围的工单，也可以去调整接单设置扩大范围。</view>
      <button class="secondary-btn master-dispatch__empty-btn" @tap="goSetting">接单设置</button>
    </view>
  </view>
</template>

<script setup>
import { ref } from 'vue';
import { onHide, onShow, onUnload } from '@dcloudio/uni-app';
import { claimDispatchOrder, getDispatchOrders } from '../../api/master';
import { safeAsync, showActionError } from '../../utils/page-task';
import { buildWsUrl } from '../../utils/request';

const loadingTaskId = ref('');
const orders = ref([]);
let socketTask = null;

async function loadOrders() {
  const res = await getDispatchOrders();
  orders.value = res.data || [];
}

const safeLoadOrders = safeAsync(loadOrders, '加载抢派单大厅');

function closeSocket() {
  if (socketTask) {
    socketTask.close({});
    socketTask = null;
  }
}

function connectSocket() {
  if (socketTask) {
    return;
  }
  socketTask = uni.connectSocket({
    url: buildWsUrl('/ws/dispatch'),
  });
  socketTask.onOpen(() => {
    socketTask?.send({ data: 'ping' });
  });
  socketTask.onMessage(() => {
    safeLoadOrders();
  });
  socketTask.onClose(() => {
    socketTask = null;
  });
}

async function grab(item) {
  loadingTaskId.value = item.id;
  try {
    await claimDispatchOrder(item.id);
    uni.showToast({ title: `抢单成功：${item.title}`, icon: 'none' });
    setTimeout(() => {
      uni.navigateTo({ url: `/pages-master/master/workbench?orderId=${item.orderId}` });
    }, 250);
  } catch (error) {
    showActionError(error, '抢单失败');
  } finally {
    loadingTaskId.value = '';
  }
}

function goSetting() {
  uni.navigateTo({ url: '/pages-master/master/setting' });
}

onShow(safeAsync(async () => {
  await safeLoadOrders();
  connectSocket();
}, '初始化抢派单大厅'));

onHide(closeSocket);
onUnload(closeSocket);
</script>

<style scoped>
.master-dispatch__hero,
.master-dispatch__notice,
.master-dispatch__card,
.master-dispatch__empty {
  padding: 28rpx;
}

.master-dispatch__hero {
  background: linear-gradient(135deg, #1c1e26 0%, #36405a 100%);
  color: #ffffff;
}

.master-dispatch__title {
  font-size: 34rpx;
  font-weight: 800;
}

.master-dispatch__desc {
  margin-top: 10rpx;
  font-size: 22rpx;
  line-height: 1.7;
  color: rgba(255, 255, 255, 0.82);
}

.master-dispatch__notice {
  margin-top: 18rpx;
}

.master-dispatch__notice-title,
.master-dispatch__empty-title {
  font-size: 30rpx;
  font-weight: 700;
  color: #1f2937;
}

.master-dispatch__notice-text,
.master-dispatch__empty-desc {
  margin-top: 10rpx;
  font-size: 24rpx;
  line-height: 1.7;
  color: #667085;
}

.master-dispatch__card {
  margin-top: 18rpx;
}

.master-dispatch__top,
.master-dispatch__bottom {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16rpx;
}

.master-dispatch__name {
  flex: 1;
  font-size: 30rpx;
  font-weight: 800;
}

.master-dispatch__meta {
  margin-top: 10rpx;
  font-size: 24rpx;
  color: #667085;
}

.master-dispatch__tags {
  display: flex;
  gap: 10rpx;
  flex-wrap: wrap;
  margin-top: 14rpx;
}

.master-dispatch__price {
  color: #ff4a4a;
  font-size: 30rpx;
  font-weight: 800;
}

.master-dispatch__btn,
.master-dispatch__empty-btn {
  width: 220rpx;
}

.master-dispatch__empty {
  margin-top: 18rpx;
  text-align: center;
}

.master-dispatch__empty-btn {
  margin-top: 20rpx;
}
</style>
