<template>
  <view class="home-header card glass-card">
    <view class="home-header__top">
      <view class="home-header__location" @tap="$emit('location')">
        <image class="home-header__location-icon" src="/static/icons/location.svg" mode="aspectFit" />
        <view>
          <view class="home-header__city">{{ locationName }}</view>
          <view class="home-header__desc">当前位置将用于智能派单与服务范围判断</view>
        </view>
      </view>
      <view class="home-header__notice">{{ notice }}</view>
    </view>
    <view class="home-header__search" @tap="$emit('search')">
      <text class="home-header__search-label">搜索服务、故障、商品</text>
      <swiper class="home-header__keywords" vertical autoplay circular interval="2800">
        <swiper-item v-for="item in keywords" :key="item">
          <view class="home-header__keyword-item">{{ item }}</view>
        </swiper-item>
      </swiper>
    </view>
  </view>
</template>

<script setup>
/**
 * 首页头部组件。
 * 展示城市、通知和滚动热搜词。
 */
defineProps({
  locationName: {
    type: String,
    default: '上海 · 浦东新区',
  },
  notice: {
    type: String,
    default: '新客最高可领 80 元券包',
  },
  keywords: {
    type: Array,
    default: () => [],
  },
});

defineEmits(['location', 'search']);
</script>

<style scoped>
.home-header {
  padding: 26rpx;
  background: linear-gradient(180deg, rgba(255, 255, 255, 0.82), rgba(255, 255, 255, 0.74));
}

.home-header__top {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 24rpx;
}

.home-header__location {
  display: flex;
  gap: 16rpx;
  align-items: center;
}

.home-header__location-icon {
  width: 48rpx;
  height: 48rpx;
}

.home-header__city {
  font-size: 32rpx;
  font-weight: 700;
  color: #1c1e26;
}

.home-header__desc {
  margin-top: 6rpx;
  font-size: 22rpx;
  color: #8b90a0;
}

.home-header__notice {
  padding: 10rpx 18rpx;
  border-radius: 999rpx;
  background: rgba(43, 92, 255, 0.08);
  color: #2b5cff;
  font-size: 22rpx;
}

.home-header__search {
  margin-top: 24rpx;
  display: flex;
  align-items: center;
  min-height: 92rpx;
  border-radius: 999rpx;
  padding: 0 28rpx;
  background: #eef2ff;
}

.home-header__search-label {
  font-size: 24rpx;
  color: #7b8192;
  margin-right: 16rpx;
}

.home-header__keywords {
  flex: 1;
  height: 88rpx;
}

.home-header__keyword-item {
  height: 88rpx;
  display: flex;
  align-items: center;
  color: #2b5cff;
  font-size: 24rpx;
  font-weight: 600;
}
</style>
