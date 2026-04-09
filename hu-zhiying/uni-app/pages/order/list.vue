<template>
  <view class="page-shell">
    <view class="order-list__tabs">
      <view
        v-for="tab in tabs"
        :key="tab.label"
        class="order-list__tab"
        :class="{ 'order-list__tab--active': activeTab === tab.value }"
        @tap="activeTab = tab.value"
      >
        {{ tab.label }}
      </view>
    </view>

    <view v-for="item in filteredOrders" :key="item.id" class="card order-list__card pressable" @tap="goDetail(item.id)">
      <view class="order-list__top">
        <view class="order-list__title">{{ item.title }}</view>
        <text class="chip">{{ item.statusText }}</text>
      </view>
      <view class="order-list__desc">{{ item.type === 'product' ? '商城商品订单' : '上门服务订单' }}</view>
      <view class="order-list__meta">预约时间：{{ item.appointment || '待确认' }}</view>
      <view class="order-list__meta">服务地址：{{ item.address }}</view>
      <view class="order-list__bottom">
        <view class="order-list__tags">
          <text v-for="tag in item.tags" :key="tag" class="tag">{{ tag }}</text>
        </view>
        <price-format :value="item.price"></price-format>
      </view>
    </view>

    <view v-if="!filteredOrders.length" class="card order-list__empty">
      当前没有符合条件的订单。可从首页下单，或检查筛选标签是否过窄。
    </view>
  </view>
</template>

<script setup>
import { computed, ref } from 'vue';
import { onShow } from '@dcloudio/uni-app';
import PriceFormat from '../../components/price-format.vue';
import { getOrderList } from '../../api/order';
import { safeAsync } from '../../utils/page-task';

const tabs = [
  { label: '全部', value: 'all' },
  { label: '待接单', value: 'pending' },
  { label: '服务中', value: 'service' },
  { label: '待补款', value: 'quote' },
  { label: '售后中', value: 'afterSales' },
];

const activeTab = ref('all');
const orders = ref([]);

const filteredOrders = computed(() => {
  if (activeTab.value === 'all') {
    return orders.value;
  }
  if (activeTab.value === 'pending') {
    return orders.value.filter((item) => ['PENDING_DISPATCH', 'PENDING_ACCEPT'].includes(item.status));
  }
  if (activeTab.value === 'service') {
    return orders.value.filter((item) => ['ON_THE_WAY', 'ARRIVED', 'IN_SERVICE'].includes(item.status));
  }
  if (activeTab.value === 'quote') {
    return orders.value.filter((item) => item.status === 'WAITING_SUPPLEMENT_PAYMENT');
  }
  return orders.value.filter((item) => ['REFUNDING', 'CANCELLED', 'AFTER_SALES'].includes(item.status));
});

function goDetail(id) {
  uni.navigateTo({ url: `/pages/order/detail?id=${id}` });
}

async function loadOrders() {
  const res = await getOrderList();
  orders.value = res.data || [];
}

onShow(safeAsync(loadOrders, '加载订单列表'));
</script>

<style scoped>
.order-list__tabs {
  display: flex;
  gap: 12rpx;
  margin-bottom: 20rpx;
  overflow-x: auto;
}

.order-list__tab {
  padding: 16rpx 24rpx;
  border-radius: 999rpx;
  background: #ffffff;
  color: #667085;
  font-size: 24rpx;
  font-weight: 700;
  white-space: nowrap;
}

.order-list__tab--active {
  background: #2b5cff;
  color: #ffffff;
}

.order-list__card {
  padding: 26rpx;
}

.order-list__card + .order-list__card {
  margin-top: 16rpx;
}

.order-list__top,
.order-list__bottom {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16rpx;
}

.order-list__title {
  flex: 1;
  font-size: 30rpx;
  font-weight: 800;
}

.order-list__desc,
.order-list__meta {
  margin-top: 10rpx;
  font-size: 24rpx;
  color: #667085;
}

.order-list__bottom {
  margin-top: 18rpx;
}

.order-list__tags {
  display: flex;
  gap: 10rpx;
  flex-wrap: wrap;
}

.order-list__empty {
  padding: 40rpx 28rpx;
  color: #667085;
  text-align: center;
  line-height: 1.7;
}
</style>
