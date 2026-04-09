<template>
  <view class="page-shell">
    <view class="card tracking__map">
      <view class="tracking__map-badge">{{ tracking.eta || 'ETA 待确认' }}</view>
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

    <view class="card tracking__section">
      <view class="section-title">
        <text class="section-title__text">履约节点</text>
        <text class="section-title__desc" @tap="safeRefreshTracking">刷新 ETA</text>
      </view>
      <view class="tracking__row"><text>预约时间</text><text>{{ tracking.appointment || '待确认' }}</text></view>
      <view class="tracking__row"><text>服务地址</text><text>{{ tracking.address || '待补充地址' }}</text></view>
      <view class="tracking__row"><text>当前状态</text><text>{{ tracking.statusText || '处理中' }}</text></view>
      <view class="tracking__row"><text>预计距离</text><text>{{ tracking.distance || '待确认' }}</text></view>
    </view>

    <view class="card tracking__section">
      <view class="section-title">
        <text class="section-title__text">轨迹明细</text>
      </view>
      <view v-if="tracking.points?.length">
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
      <view v-else class="tracking__empty">还没有轨迹点。师傅接单、出发、到达后会陆续回传履约进度。</view>
    </view>

    <view v-if="tracking.mediaFiles?.length" class="card tracking__section">
      <view class="section-title">
        <text class="section-title__text">现场凭证</text>
        <text class="section-title__desc">{{ tracking.mediaFiles.length }} 个文件</text>
      </view>
      <view
        v-for="item in tracking.mediaFiles"
        :key="item.id"
        class="tracking__media-item"
        @tap="openMedia(item)"
      >
        <view class="tracking__media-copy">
          <view class="tracking__media-title">{{ item.originalName || '订单文件' }}</view>
          <view class="tracking__media-meta">{{ mediaLabel(item) }} · {{ formatTime(item.createdAt) }}</view>
        </view>
        <text class="tracking__media-link">{{ isImageMedia(item) ? '预览' : '复制链接' }}</text>
      </view>
    </view>
  </view>
</template>

<script setup>
import { computed, ref } from 'vue';
import { onLoad } from '@dcloudio/uni-app';
import { getOrderEta } from '../../api/map';
import { getOrderTracking } from '../../api/order';
import { safeAsync } from '../../utils/page-task';
import { buildAbsoluteUrl } from '../../utils/request';

const orderId = ref('');
const tracking = ref({
  points: [],
  distance: '',
  mediaFiles: [],
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

function mediaLabel(item) {
  const labels = {
    order_evidence: '报修凭证',
    before_work_media: '施工前',
    after_work_media: '施工后',
  };
  return labels[item?.bizType] || '订单文件';
}

function isImageMedia(item) {
  return String(item?.contentType || '').startsWith('image/');
}

function normalizeMediaUrl(url) {
  if (!url) {
    return '';
  }
  return /^https?:\/\//.test(url) ? url : buildAbsoluteUrl(url.startsWith('/') ? url : `/${url}`);
}

function openMedia(item) {
  const currentUrl = normalizeMediaUrl(item?.url);
  if (!currentUrl) {
    uni.showToast({ title: '文件链接不存在', icon: 'none' });
    return;
  }

  if (isImageMedia(item)) {
    const urls = (tracking.value.mediaFiles || [])
      .filter((media) => isImageMedia(media))
      .map((media) => normalizeMediaUrl(media.url))
      .filter(Boolean);
    uni.previewImage({
      current: currentUrl,
      urls: urls.length ? urls : [currentUrl],
    });
    return;
  }

  uni.setClipboardData({
    data: currentUrl,
    success() {
      uni.showToast({ title: '链接已复制', icon: 'none' });
    },
  });
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
    distance: etaRes.data?.distance || trackingRes.data?.distance || '',
  };
}

const safeRefreshTracking = safeAsync(refreshTracking, '刷新订单轨迹');

onLoad(safeAsync(async (options) => {
  orderId.value = options.id || '';
  await safeRefreshTracking();
}, '加载订单轨迹'));
</script>

<style scoped>
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

.tracking__empty {
  font-size: 24rpx;
  line-height: 1.7;
  color: #667085;
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

.tracking__media-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16rpx;
  padding: 20rpx 0;
  border-top: 1rpx solid #edf1f6;
}

.tracking__media-copy {
  flex: 1;
}

.tracking__media-title {
  font-size: 26rpx;
  font-weight: 700;
  color: #1c1e26;
}

.tracking__media-meta {
  margin-top: 8rpx;
  font-size: 22rpx;
  color: #667085;
}

.tracking__media-link {
  flex-shrink: 0;
  font-size: 24rpx;
  color: #2b5cff;
}
</style>
