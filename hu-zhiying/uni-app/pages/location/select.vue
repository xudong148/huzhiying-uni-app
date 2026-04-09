<template>
  <view class="page-shell">
    <view class="card location-page__current">
      <view>
        <view class="location-page__title">当前位置</view>
        <view class="location-page__desc">{{ location.current.address }}</view>
      </view>
      <button class="secondary-btn location-page__btn" :loading="relocating" @tap="safeRelocate">重新定位</button>
    </view>

    <view class="section-title location-page__section">
      <text class="section-title__text">可服务城市</text>
      <text class="section-title__desc">选择后会同步首页、圈子和下单页的城市上下文</text>
    </view>

    <view v-if="!cities.length" class="card location-page__empty">
      <text class="location-page__empty-title">暂时没有可选城市</text>
      <text class="location-page__empty-desc">请稍后重试，或先使用当前位置继续浏览。</text>
    </view>

    <view v-else class="location-page__list">
      <view
        v-for="item in cities"
        :key="item.id"
        class="card location-page__item pressable"
        @tap="selectCity(item)"
      >
        <view>
          <view class="location-page__item-name">{{ item.name }}</view>
          <view class="location-page__item-desc">{{ item.district || '全城服务' }}</view>
        </view>
        <text class="chip" v-if="item.hot">热门</text>
      </view>
    </view>
  </view>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import { getServiceCities } from '../../api/map';
import { useLocationStore } from '../../stores/location';
import { safeAsync } from '../../utils/page-task';

const location = useLocationStore();
const cities = ref([]);
const relocating = ref(false);

async function relocate() {
  relocating.value = true;
  try {
    await location.locate();
    uni.showToast({ title: '定位已更新', icon: 'none' });
  } finally {
    relocating.value = false;
  }
}

const safeRelocate = safeAsync(relocate, '刷新当前位置');

function selectCity(item) {
  location.setLocation({
    city: item.name,
    district: item.district,
    address: `${item.name}${item.district || ''}`,
  });
  uni.navigateBack();
}

onMounted(safeAsync(async () => {
  const res = await getServiceCities();
  cities.value = res.data || [];
}, '加载服务城市'));
</script>

<style scoped>
.location-page__current {
  padding: 28rpx;
}

.location-page__title {
  font-size: 30rpx;
  font-weight: 700;
}

.location-page__desc {
  margin-top: 10rpx;
  font-size: 24rpx;
  line-height: 1.6;
  color: #667085;
}

.location-page__btn {
  margin-top: 24rpx;
}

.location-page__section {
  margin-top: 26rpx;
}

.location-page__empty {
  padding: 40rpx 28rpx;
  text-align: center;
}

.location-page__empty-title {
  display: block;
  font-size: 30rpx;
  font-weight: 700;
  color: #1f2937;
}

.location-page__empty-desc {
  display: block;
  margin-top: 12rpx;
  font-size: 24rpx;
  line-height: 1.7;
  color: #667085;
}

.location-page__list {
  display: flex;
  flex-direction: column;
  gap: 16rpx;
}

.location-page__item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 24rpx;
}

.location-page__item-name {
  font-size: 28rpx;
  font-weight: 700;
}

.location-page__item-desc {
  margin-top: 8rpx;
  font-size: 22rpx;
  color: #8b90a0;
}
</style>
