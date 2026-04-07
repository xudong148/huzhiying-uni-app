<template>
  <view class="kingkong card">
    <view class="kingkong__grid">
      <view
        v-for="item in list"
        :key="item.id"
        class="kingkong__item pressable"
        @tap="$emit('select', item)"
      >
        <view class="kingkong__icon-wrap">
          <image class="kingkong__icon" :src="item.icon" mode="aspectFit" />
        </view>
        <text class="kingkong__name">{{ item.name }}</text>
        <text class="kingkong__sub">{{ item.subs[0] }}</text>
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
.kingkong {
  padding: 28rpx 18rpx 16rpx;
}

.kingkong__grid {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: 18rpx 10rpx;
}

.kingkong__item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 12rpx 8rpx;
}

.kingkong__icon-wrap {
  width: 92rpx;
  height: 92rpx;
  border-radius: 28rpx;
  background: #f3f7ff;
  display: flex;
  align-items: center;
  justify-content: center;
}

.kingkong__icon {
  width: 56rpx;
  height: 56rpx;
}

.kingkong__name {
  margin-top: 14rpx;
  font-size: 24rpx;
  font-weight: 700;
}

.kingkong__sub {
  margin-top: 6rpx;
  font-size: 20rpx;
  color: #8b90a0;
}
</style>
