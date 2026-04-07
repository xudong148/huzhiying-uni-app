<template>
  <view class="page-shell">
    <view v-for="item in coupons" :key="item.id" class="coupon-page__card">
      <view class="coupon-page__main">
        <view>
          <view class="coupon-page__title">{{ item.title }}</view>
          <view class="coupon-page__desc">{{ item.threshold }} · 有效期至 {{ item.expireAt }}</view>
        </view>
        <view class="coupon-page__amount">
          <text class="coupon-page__symbol">¥</text>
          <text class="coupon-page__value">{{ item.amount }}</text>
        </view>
      </view>
      <view class="coupon-page__tail">平台自动核销，优先抵扣上门费与服务费</view>
    </view>
  </view>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import { getCouponList } from '../../api/user';

const coupons = ref([]);

onMounted(async () => {
  const res = await getCouponList();
  coupons.value = res.data;
});
</script>

<style scoped>
.coupon-page__card {
  overflow: hidden;
  border-radius: 32rpx;
  background: linear-gradient(135deg, #2b5cff 0%, #8ab4f8 100%);
  color: #ffffff;
}

.coupon-page__card + .coupon-page__card {
  margin-top: 16rpx;
}

.coupon-page__main {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16rpx;
  padding: 30rpx;
}

.coupon-page__title {
  font-size: 30rpx;
  font-weight: 800;
}

.coupon-page__desc {
  margin-top: 10rpx;
  font-size: 22rpx;
  color: rgba(255, 255, 255, 0.88);
}

.coupon-page__amount {
  display: flex;
  align-items: baseline;
}

.coupon-page__symbol {
  font-size: 24rpx;
}

.coupon-page__value {
  font-size: 56rpx;
  font-weight: 800;
}

.coupon-page__tail {
  padding: 18rpx 30rpx;
  background: rgba(0, 0, 0, 0.12);
  font-size: 22rpx;
}
</style>
