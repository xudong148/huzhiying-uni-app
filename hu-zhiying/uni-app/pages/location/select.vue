<template>
  <view class="page-shell">
    <view class="card location-page__current">
      <view>
        <view class="location-page__title">当前位置</view>
        <view class="location-page__desc">{{ location.current.address }}</view>
      </view>
      <button class="secondary-btn location-page__btn" @tap="relocate">重新定位</button>
    </view>

    <view class="section-title location-page__section">
      <text class="section-title__text">热门城市</text>
    </view>

    <view class="location-page__list">
      <view
        v-for="item in cities"
        :key="item.id"
        class="card location-page__item pressable"
        @tap="selectCity(item)"
      >
        <view>
          <view class="location-page__item-name">{{ item.name }}</view>
          <view class="location-page__item-desc">{{ item.district }}</view>
        </view>
        <text class="chip" v-if="item.hot">热门</text>
      </view>
    </view>
  </view>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import { getCityList } from '../../api/user';
import { useLocationStore } from '../../stores/location';

const location = useLocationStore();
const cities = ref([]);

async function relocate() {
  await location.locate();
  uni.showToast({ title: '已更新定位', icon: 'none' });
}

function selectCity(item) {
  location.setLocation({
    city: item.name,
    district: item.district,
    address: `${item.name}${item.district}`,
  });
  uni.navigateBack();
}

onMounted(async () => {
  const res = await getCityList();
  cities.value = res.data;
});
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
