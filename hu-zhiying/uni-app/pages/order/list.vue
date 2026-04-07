<template>
  <view class="page-shell">
    <view class="order-list__tabs">
      <view
        v-for="tab in tabs"
        :key="tab"
        class="order-list__tab"
        :class="{ 'order-list__tab--active': activeTab === tab }"
        @tap="activeTab = tab"
      >
        {{ tab }}
      </view>
    </view>

    <view v-for="item in orders" :key="item.id" class="card order-list__card pressable" @tap="goDetail(item.id)">
      <view class="order-list__top">
        <view class="order-list__title">{{ item.title }}</view>
        <text class="chip">{{ item.status }}</text>
      </view>
      <view class="order-list__desc">{{ item.statusText }}</view>
      <view class="order-list__meta">预约：{{ item.appointment }}</view>
      <view class="order-list__meta">地址：{{ item.address }}</view>
      <view class="order-list__bottom">
        <view class="order-list__tags">
          <text v-for="tag in item.tags" :key="tag" class="tag">{{ tag }}</text>
        </view>
        <price-format :value="item.price"></price-format>
      </view>
    </view>
  </view>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import PriceFormat from '../../components/price-format.vue';
import { getOrderList } from '../../api/order';

const tabs = ['全部', '待付款', '进行中', '待评价', '售后'];
const activeTab = ref('全部');
const orders = ref([]);

function goDetail(id) {
  uni.navigateTo({ url: `/pages/order/detail?id=${id}` });
}

onMounted(async () => {
  const res = await getOrderList();
  orders.value = res.data;
});
</script>

<style scoped>
.order-list__tabs {
  display: flex;
  gap: 12rpx;
  margin-bottom: 20rpx;
}

.order-list__tab {
  padding: 16rpx 24rpx;
  border-radius: 999rpx;
  background: #ffffff;
  color: #667085;
  font-size: 24rpx;
  font-weight: 700;
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
</style>
