<template>
  <view class="price-format">
    <text class="price-format__symbol">¥</text>
    <text class="price-format__value">{{ value }}</text>
    <text v-if="suffix" class="price-format__suffix">{{ suffix }}</text>
  </view>
</template>

<script setup>
defineProps({
  value: {
    type: [String, Number],
    default: 0,
  },
  suffix: {
    type: String,
    default: '',
  },
});
</script>

<style scoped>
.price-format {
  display: inline-flex;
  align-items: baseline;
  color: #ff4a4a;
}

.price-format__symbol {
  font-size: 24rpx;
  margin-right: 4rpx;
}

.price-format__value {
  font-size: 42rpx;
  font-weight: 800;
}

.price-format__suffix {
  margin-left: 8rpx;
  font-size: 22rpx;
  color: #8b90a0;
}
</style>
