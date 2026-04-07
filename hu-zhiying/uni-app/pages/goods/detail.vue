<template>
  <view class="page-container goods-page">
    <scroll-view scroll-y class="goods-page__scroll" :show-scrollbar="false">
      <swiper class="goods-page__swiper" circular autoplay indicator-dots>
        <swiper-item v-for="item in detail.images" :key="item">
          <image class="goods-page__image" :src="item" mode="aspectFill" />
        </swiper-item>
      </swiper>

      <view class="page-shell goods-page__body">
        <view class="card goods-page__summary">
          <view class="goods-page__title">{{ detail.title }}</view>
          <view class="goods-page__subtitle">{{ detail.subtitle }}</view>
          <view class="goods-page__price-row">
            <price-format :value="detail.basePrice" suffix="基础检测费"></price-format>
            <text class="goods-page__guide">上门费 ¥{{ detail.doorPrice }} / 指导价 {{ detail.guidePrice }}</text>
          </view>
          <view class="goods-page__guarantees">
            <text v-for="item in detail.guarantees" :key="item" class="tag">{{ item }}</text>
          </view>
        </view>

        <view class="card goods-page__section">
          <view class="section-title">
            <text class="section-title__text">服务流程</text>
          </view>
          <view class="goods-page__process-item" v-for="(item, index) in detail.process" :key="item">
            <view class="goods-page__process-index">{{ index + 1 }}</view>
            <text>{{ item }}</text>
          </view>
        </view>

        <view class="card goods-page__section">
          <view class="section-title">
            <text class="section-title__text">用户评价</text>
            <text class="section-title__desc" @tap="goComment">查看全部</text>
          </view>
          <view v-for="item in detail.comments" :key="item.id" class="goods-page__comment">
            <view class="goods-page__comment-top">
              <text class="goods-page__comment-user">{{ item.user }}</text>
              <text class="muted">{{ item.date }}</text>
            </view>
            <view class="goods-page__comment-content">{{ item.content }}</view>
            <image
              v-if="item.images.length"
              class="goods-page__comment-image"
              :src="item.images[0]"
              mode="aspectFill"
            />
          </view>
        </view>
        <view class="safe-bottom"></view>
      </view>
    </scroll-view>

    <view class="goods-page__dock">
      <view class="goods-page__dock-actions">
        <view class="goods-page__dock-item" @tap="goChat">客服</view>
        <view class="goods-page__dock-item" @tap="goComment">评价</view>
      </view>
      <button class="primary-btn goods-page__dock-btn" @tap="goCheckout">立即预约</button>
    </view>
  </view>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import PriceFormat from '../../components/price-format.vue';
import { getServiceDetail } from '../../api/service';

const detail = ref({
  images: [],
  guarantees: [],
  process: [],
  comments: [],
});

async function loadPage() {
  const res = await getServiceDetail();
  detail.value = res.data;
}

function goComment() {
  uni.navigateTo({ url: '/pages/goods/comment' });
}

function goCheckout() {
  uni.navigateTo({ url: '/pages/order/checkout' });
}

function goChat() {
  uni.switchTab({ url: '/pages/message/chat' });
}

onMounted(loadPage);
</script>

<style scoped>
.goods-page {
  background: #f4f6f9;
}

.goods-page__scroll {
  height: calc(100vh - 132rpx);
}

.goods-page__swiper {
  height: 520rpx;
}

.goods-page__image {
  width: 100%;
  height: 100%;
}

.goods-page__body {
  margin-top: -36rpx;
}

.goods-page__summary,
.goods-page__section {
  padding: 28rpx;
}

.goods-page__title {
  font-size: 38rpx;
  font-weight: 800;
}

.goods-page__subtitle {
  margin-top: 10rpx;
  font-size: 24rpx;
  color: #667085;
  line-height: 1.6;
}

.goods-page__price-row {
  margin-top: 20rpx;
}

.goods-page__guide {
  display: block;
  margin-top: 10rpx;
  font-size: 22rpx;
  color: #8b90a0;
}

.goods-page__guarantees {
  display: flex;
  flex-wrap: wrap;
  gap: 12rpx;
  margin-top: 18rpx;
}

.goods-page__section {
  margin-top: 20rpx;
}

.goods-page__process-item {
  display: flex;
  gap: 16rpx;
  align-items: flex-start;
}

.goods-page__process-item + .goods-page__process-item {
  margin-top: 18rpx;
}

.goods-page__process-index {
  width: 40rpx;
  height: 40rpx;
  border-radius: 50%;
  background: rgba(43, 92, 255, 0.12);
  color: #2b5cff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 22rpx;
  font-weight: 700;
}

.goods-page__comment + .goods-page__comment {
  margin-top: 24rpx;
  padding-top: 24rpx;
  border-top: 1rpx solid #edf1f6;
}

.goods-page__comment-top {
  display: flex;
  justify-content: space-between;
}

.goods-page__comment-user {
  font-size: 26rpx;
  font-weight: 700;
}

.goods-page__comment-content {
  margin-top: 10rpx;
  font-size: 24rpx;
  line-height: 1.7;
  color: #1c1e26;
}

.goods-page__comment-image {
  margin-top: 14rpx;
  width: 180rpx;
  height: 120rpx;
  border-radius: 18rpx;
}

.goods-page__dock {
  position: fixed;
  left: 0;
  right: 0;
  bottom: 0;
  display: flex;
  align-items: center;
  gap: 18rpx;
  padding: 20rpx 24rpx calc(20rpx + env(safe-area-inset-bottom));
  background: rgba(255, 255, 255, 0.94);
  backdrop-filter: blur(18px);
}

.goods-page__dock-actions {
  width: 180rpx;
  display: flex;
  gap: 12rpx;
}

.goods-page__dock-item {
  flex: 1;
  height: 84rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 24rpx;
  background: #f4f6f9;
  font-size: 24rpx;
  font-weight: 700;
}

.goods-page__dock-btn {
  flex: 1;
}
</style>
