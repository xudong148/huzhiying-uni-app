<template>
  <view class="page-shell">
    <view class="card tracking__map">
      <view class="tracking__map-badge">ETA {{ order.eta || '待确认' }}</view>
      <view class="tracking__route"></view>
      <view class="tracking__point tracking__point--user">我</view>
      <view class="tracking__point tracking__point--master">师傅</view>
    </view>

    <view class="card tracking__section">
      <view class="section-title">
        <text class="section-title__text">履约节点</text>
      </view>
      <view class="tracking__row"><text>预约时间</text><text>{{ order.appointment || '待确认' }}</text></view>
      <view class="tracking__row"><text>服务地址</text><text>{{ order.address || '待补充地址' }}</text></view>
      <view class="tracking__row"><text>当前状态</text><text>{{ order.statusText || '处理中' }}</text></view>
    </view>
  </view>
</template>

<script setup>
import { ref } from 'vue';
import { onLoad } from '@dcloudio/uni-app';
import { getOrderDetail } from '../../api/order';

const order = ref({});

onLoad(async (options) => {
  if (!options.id) {
    return;
  }
  const res = await getOrderDetail(options.id);
  order.value = res.data;
});
</script>

<style scoped>
.tracking__map {
  position: relative;
  height: 420rpx;
  overflow: hidden;
  background:
    linear-gradient(90deg, rgba(43, 92, 255, 0.06) 1rpx, transparent 1rpx),
    linear-gradient(rgba(43, 92, 255, 0.06) 1rpx, transparent 1rpx),
    linear-gradient(180deg, #ecf1ff 0%, #ffffff 100%);
  background-size: 48rpx 48rpx;
}

.tracking__map-badge {
  position: absolute;
  top: 24rpx;
  left: 24rpx;
  z-index: 1;
  padding: 10rpx 16rpx;
  border-radius: 999rpx;
  background: #2b5cff;
  color: #ffffff;
  font-size: 22rpx;
}

.tracking__route {
  position: absolute;
  left: 180rpx;
  top: 120rpx;
  width: 260rpx;
  height: 140rpx;
  border: 6rpx solid #2b5cff;
  border-color: #2b5cff transparent transparent #2b5cff;
  border-radius: 120rpx 0 0 0;
}

.tracking__point {
  position: absolute;
  width: 72rpx;
  height: 72rpx;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24rpx;
  font-weight: 800;
}

.tracking__point--user {
  right: 120rpx;
  top: 220rpx;
  background: #ffe0a3;
}

.tracking__point--master {
  left: 140rpx;
  top: 120rpx;
  background: #2b5cff;
  color: #ffffff;
}

.tracking__section {
  margin-top: 20rpx;
  padding: 28rpx;
}

.tracking__row {
  display: flex;
  justify-content: space-between;
  gap: 16rpx;
  font-size: 26rpx;
}

.tracking__row + .tracking__row {
  margin-top: 16rpx;
}
</style>
