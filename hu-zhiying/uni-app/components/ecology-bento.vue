<template>
  <view class="ecology">
    <view class="ecology__left card pressable" @tap="$emit('select', list[0])">
      <view>
        <view class="ecology__title" :style="{ color: list[0]?.color }">{{ list[0]?.name }}</view>
        <view class="ecology__desc">{{ list[0]?.desc }}</view>
      </view>
      <image class="ecology__icon-lg" :src="list[0]?.icon" mode="aspectFit" />
    </view>
    <view class="ecology__right">
      <view
        v-for="item in list.slice(1)"
        :key="item.id"
        class="ecology__small card pressable"
        @tap="$emit('select', item)"
      >
        <view>
          <view class="ecology__title" :style="{ color: item.color }">{{ item.name }}</view>
          <view class="ecology__desc">{{ item.desc }}</view>
        </view>
        <image class="ecology__icon" :src="item.icon" mode="aspectFit" />
      </view>
    </view>
  </view>
</template>

<script setup>
defineProps({
  list: {
    type: Array,
    default: () => [],
  },
});

defineEmits(['select']);
</script>

<style scoped>
.ecology {
  display: flex;
  gap: 16rpx;
}

.ecology__left,
.ecology__small {
  display: flex;
  justify-content: space-between;
  padding: 24rpx;
}

.ecology__left {
  flex: 1.1;
  min-height: 260rpx;
  flex-direction: column;
}

.ecology__right {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 16rpx;
}

.ecology__small {
  flex: 1;
  align-items: center;
}

.ecology__title {
  font-size: 30rpx;
  font-weight: 700;
}

.ecology__desc {
  margin-top: 8rpx;
  font-size: 22rpx;
  color: #8b90a0;
}

.ecology__icon-lg {
  align-self: flex-end;
  width: 140rpx;
  height: 140rpx;
}

.ecology__icon {
  width: 74rpx;
  height: 74rpx;
}
</style>
