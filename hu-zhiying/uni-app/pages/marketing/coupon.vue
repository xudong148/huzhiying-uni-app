<template>
  <view class="page-shell">
    <view class="coupon-page__hero card">
      <view class="coupon-page__hero-title">领券中心</view>
      <view class="coupon-page__hero-desc">平台会在下单时自动优先匹配可用优惠券，减少用户手动选券成本。</view>
    </view>

    <view v-if="!coupons.length" class="card coupon-page__empty">
      <text class="coupon-page__empty-title">当前没有可用优惠券</text>
      <text class="coupon-page__empty-desc">可以先去首页下单、参加活动或联系平台客服补发优惠券。</text>
    </view>

    <view v-for="item in coupons" :key="item.id" class="coupon-page__card">
      <view class="coupon-page__main">
        <view>
          <view class="coupon-page__title">{{ item.title }}</view>
          <view class="coupon-page__desc">{{ item.threshold }}，有效期至 {{ item.expireAt }}</view>
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
import { ref } from 'vue';
import { onShow } from '@dcloudio/uni-app';
import { getCouponList } from '../../api/user';
import { safeAsync } from '../../utils/page-task';

const coupons = ref([]);

onShow(safeAsync(async () => {
  const res = await getCouponList();
  coupons.value = res.data || [];
}, '加载优惠券'));
</script>

<style scoped>
.coupon-page__hero {
  padding: 28rpx;
  margin-bottom: 18rpx;
}

.coupon-page__hero-title {
  font-size: 32rpx;
  font-weight: 800;
}

.coupon-page__hero-desc {
  margin-top: 10rpx;
  font-size: 24rpx;
  line-height: 1.7;
  color: #667085;
}

.coupon-page__empty {
  padding: 40rpx 28rpx;
  text-align: center;
}

.coupon-page__empty-title {
  display: block;
  font-size: 30rpx;
  font-weight: 700;
  color: #1f2937;
}

.coupon-page__empty-desc {
  display: block;
  margin-top: 12rpx;
  font-size: 24rpx;
  line-height: 1.7;
  color: #667085;
}

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
