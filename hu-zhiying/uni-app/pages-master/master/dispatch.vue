<template>
  <view class="page-shell">
    <view class="card master-dispatch__hero">
      <view class="master-dispatch__title">听单中</view>
      <view class="master-dispatch__desc">当前向你推送附近 {{ orders.length }} 个可接服务订单</view>
    </view>

    <view v-for="item in orders" :key="item.id" class="card master-dispatch__card">
      <view class="master-dispatch__top">
        <view class="master-dispatch__name">{{ item.title }}</view>
        <text class="chip">{{ item.distance }}</text>
      </view>
      <view class="master-dispatch__meta">{{ item.address }}</view>
      <view class="master-dispatch__tags">
        <text v-for="tag in item.tags" :key="tag" class="tag">{{ tag }}</text>
      </view>
      <view class="master-dispatch__bottom">
        <text class="master-dispatch__price">预计收入 ¥{{ Number(item.income || 0).toFixed(2) }}</text>
        <button class="primary-btn master-dispatch__btn" :loading="loadingTaskId === item.id" @tap="grab(item)">立即抢单</button>
      </view>
    </view>

    <view v-if="!orders.length" class="card master-dispatch__empty">暂无可抢订单，系统会持续为你推送。</view>
  </view>
</template>

<script setup>
import { ref } from 'vue';
import { onShow } from '@dcloudio/uni-app';
import { claimDispatchOrder, getDispatchOrders } from '../../api/master';

const loadingTaskId = ref('');
const orders = ref([]);

async function loadOrders() {
  const res = await getDispatchOrders();
  orders.value = res.data;
}

async function grab(item) {
  loadingTaskId.value = item.id;
  try {
    await claimDispatchOrder(item.id, '张师傅');
    uni.showToast({ title: `抢单成功：${item.title}`, icon: 'none' });
    setTimeout(() => {
      uni.navigateTo({ url: `/pages-master/master/workbench?orderId=${item.orderId}` });
    }, 300);
  } finally {
    loadingTaskId.value = '';
  }
}

onShow(loadOrders);
</script>

<style scoped>
.master-dispatch__hero,
.master-dispatch__card {
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
  color: rgba(255, 255, 255, 0.82);
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

.master-dispatch__btn {
  width: 220rpx;
}

.master-dispatch__empty {
  margin-top: 18rpx;
  padding: 36rpx 28rpx;
  text-align: center;
  color: #667085;
}
</style>
