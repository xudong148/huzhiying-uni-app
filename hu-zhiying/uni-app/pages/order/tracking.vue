<template>
  <view class="page-shell">
    <!-- 地图轨迹 -->
    <view class="card tracking__map">
      <view class="tracking__map-badge">{{ tracking.eta || '待确认 ETA' }}</view>
      <map
        class="tracking__map-core"
        :latitude="mapCenter.latitude"
        :longitude="mapCenter.longitude"
        :markers="markers"
        :polyline="polyline"
        :scale="14"
        :show-location="true"
      />
    </view>

    <!-- 订单摘要 -->
    <view class="card tracking__section">
      <view class="section-title">
        <text class="section-title__text">履约节点</text>
        <text class="section-title__desc" @tap="refreshTracking">刷新 ETA</text>
      </view>
      <view class="tracking__row"><text>预约时间</text><text>{{ tracking.appointment || '待确认' }}</text></view>
      <view class="tracking__row"><text>服务地址</text><text>{{ tracking.address || '待补充地址' }}</text></view>
      <view class="tracking__row"><text>当前状态</text><text>{{ tracking.statusText || '处理中' }}</text></view>
      <view class="tracking__row"><text>预计距离</text><text>{{ tracking.distance || '待确认' }}</text></view>
    </view>

    <!-- 轨迹时间线 -->
    <view class="card tracking__section">
      <view class="section-title">
        <text class="section-title__text">轨迹明细</text>
      </view>
      <view v-for="item in tracking.points" :key="item.id" class="tracking__timeline-item">
        <view class="tracking__timeline-dot"></view>
        <view class="tracking__timeline-body">
          <view class="tracking__timeline-top">
            <text class="tracking__timeline-title">{{ item.label }}</text>
            <text class="muted">{{ formatTime(item.time) }}</text>
          </view>
          <view class="tracking__timeline-desc">{{ item.description }}</view>
        </view>
      </view>
    </view>
  </view>
</template>

<script setup>
/**
 * 订单轨迹页。
 * 1. 轨迹点从真实订单轨迹接口获取。
 * 2. ETA 和距离提示通过地图接口刷新，避免页面只展示静态文案。
 */
import { computed, ref } from 'vue';
import { onLoad } from '@dcloudio/uni-app';
import { getOrderEta } from '../../api/map';
import { getOrderTracking } from '../../api/order';

const orderId = ref('');
const tracking = ref({
  points: [],
  distance: '',
});

const mapCenter = computed(() => {
  const last = tracking.value.points?.[tracking.value.points.length - 1];
  return {
    latitude: Number(last?.latitude || 31.2253),
    longitude: Number(last?.longitude || 121.5443),
  };
});

const markers = computed(() => (tracking.value.points || []).map((item, index) => ({
  id: item.id || index + 1,
  latitude: Number(item.latitude || 31.2253),
  longitude: Number(item.longitude || 121.5443),
  title: item.label,
  width: 28,
  height: 28,
})));

const polyline = computed(() => {
  const points = (tracking.value.points || []).map((item) => ({
    latitude: Number(item.latitude || 31.2253),
    longitude: Number(item.longitude || 121.5443),
  }));
  if (points.length < 2) {
    return [];
  }
  return [{
    points,
    color: '#2B5CFF',
    width: 6,
  }];
});

function formatTime(time) {
  if (!time) {
    return '刚刚';
  }
  return String(time).replace('T', ' ').slice(0, 16);
}

async function refreshTracking() {
  if (!orderId.value) {
    return;
  }

  const [trackingRes, etaRes] = await Promise.all([
    getOrderTracking(orderId.value),
    getOrderEta(orderId.value).catch(() => ({ data: {} })),
  ]);

  tracking.value = {
    ...(trackingRes.data || { points: [] }),
    eta: etaRes.data?.eta || trackingRes.data?.eta || '',
    distance: etaRes.data?.distance || '',
  };
}

onLoad(async (options) => {
  orderId.value = options.id || '';
  await refreshTracking();
});
</script>

<style scoped>
/* 地图区块 */
.tracking__map {
  position: relative;
  height: 420rpx;
  overflow: hidden;
}

.tracking__map-core {
  width: 100%;
  height: 100%;
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

/* 节点与时间线 */
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

.tracking__timeline-item {
  position: relative;
  display: flex;
  gap: 18rpx;
}

.tracking__timeline-item + .tracking__timeline-item {
  margin-top: 22rpx;
}

.tracking__timeline-dot {
  width: 18rpx;
  height: 18rpx;
  margin-top: 12rpx;
  border-radius: 50%;
  background: #2b5cff;
}

.tracking__timeline-body {
  flex: 1;
}

.tracking__timeline-top {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16rpx;
}

.tracking__timeline-title {
  font-size: 26rpx;
  font-weight: 700;
}

.tracking__timeline-desc {
  margin-top: 10rpx;
  font-size: 24rpx;
  line-height: 1.6;
  color: #667085;
}
</style>
