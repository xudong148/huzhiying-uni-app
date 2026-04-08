<template>
  <view class="page-container goods-page">
    <scroll-view scroll-y class="goods-page__scroll" :show-scrollbar="false">
      <swiper class="goods-page__swiper" circular autoplay indicator-dots>
        <swiper-item v-for="item in detail.images" :key="item">
          <image class="goods-page__image" :src="item" mode="aspectFill" />
        </swiper-item>
      </swiper>

      <view class="page-shell goods-page__body">
        <!-- 基础信息 -->
        <view class="card goods-page__summary">
          <view class="goods-page__title">{{ detail.title }}</view>
          <view class="goods-page__subtitle">{{ detail.subtitle }}</view>

          <view class="goods-page__price-row">
            <price-format :value="displayPrice" :suffix="isProduct ? '起' : '基础检测费'"></price-format>
            <template v-if="isProduct">
              <text class="goods-page__guide">{{ detail.deliveryDesc }}</text>
              <text v-if="detail.createInstallOrder" class="goods-page__guide">购买成功后自动生成安装工单</text>
            </template>
            <template v-else>
              <text class="goods-page__guide">上门费 ¥{{ detail.doorPrice }} / 指导价 {{ detail.guidePrice }}</text>
              <text class="goods-page__guide">质保说明：{{ detail.warranty }}</text>
            </template>
          </view>

          <view class="goods-page__guarantees">
            <text v-for="item in tags" :key="item" class="tag">{{ item }}</text>
          </view>
        </view>

        <!-- SKU 选择 -->
        <view v-if="isProduct" class="card goods-page__section">
          <view class="section-title">
            <text class="section-title__text">规格选择</text>
            <text class="section-title__desc">库存实时校验</text>
          </view>
          <view class="goods-page__sku-list">
            <view
              v-for="item in detail.skus"
              :key="item.id"
              class="goods-page__sku"
              :class="{ 'goods-page__sku--active': selectedSkuId === item.id }"
              @tap="selectedSkuId = item.id"
            >
              <view class="goods-page__sku-name">{{ item.name }}</view>
              <view class="goods-page__sku-meta">¥{{ Number(item.price || 0).toFixed(2) }} / 库存 {{ item.stock }}</view>
            </view>
          </view>
        </view>

        <!-- 流程说明 -->
        <view class="card goods-page__section">
          <view class="section-title">
            <text class="section-title__text">{{ isProduct ? '履约说明' : '服务流程' }}</text>
          </view>
          <view v-for="(item, index) in processList" :key="`${index}-${item}`" class="goods-page__process-item">
            <view class="goods-page__process-index">{{ index + 1 }}</view>
            <text>{{ item }}</text>
          </view>
        </view>

        <!-- 评论区 -->
        <view v-if="!isProduct" class="card goods-page__section">
          <view class="section-title">
            <text class="section-title__text">用户评价</text>
            <text class="section-title__desc" @tap="goComment">查看全部</text>
          </view>
          <view v-for="item in previewComments" :key="item.id" class="goods-page__comment">
            <view class="goods-page__comment-top">
              <text class="goods-page__comment-user">{{ item.user }}</text>
              <text class="muted">{{ item.date }}</text>
            </view>
            <view class="goods-page__comment-content">{{ item.content }}</view>
            <image
              v-if="item.images?.length"
              class="goods-page__comment-image"
              :src="item.images[0]"
              mode="aspectFill"
            />
          </view>
        </view>

        <view class="safe-bottom"></view>
      </view>
    </scroll-view>

    <!-- 底部操作栏 -->
    <view class="goods-page__dock">
      <view class="goods-page__dock-actions">
        <view class="goods-page__dock-item" @tap="goChat">客服</view>
        <view class="goods-page__dock-item" @tap="isProduct ? goOrderList() : goComment()">{{ isProduct ? '订单' : '评价' }}</view>
      </view>
      <button class="primary-btn goods-page__dock-btn" :loading="submitting" @tap="handlePrimaryAction">
        {{ isProduct ? '立即购买' : '立即预约' }}
      </button>
    </view>
  </view>
</template>

<script setup>
/**
 * 商品 / 服务详情页。
 * 1. 服务详情和商品详情都走真实接口。
 * 2. 商品购买走真实商品订单与预支付接口，服务详情直达下单页。
 */
import { computed, ref } from 'vue';
import { onLoad } from '@dcloudio/uni-app';
import PriceFormat from '../../components/price-format.vue';
import { createProductOrder, requestWechatPrepay } from '../../api/order';
import { getProductDetail, getServiceComments, getServiceDetail } from '../../api/service';
import { launchWechatPay } from '../../utils/wechat-pay';

const detailMode = ref('service');
const currentId = ref(201);
const selectedSkuId = ref(null);
const submitting = ref(false);
const detail = ref({
  images: [],
  guarantees: [],
  process: [],
  comments: [],
  skus: [],
  highlights: [],
});
const comments = ref([]);

const isProduct = computed(() => detailMode.value === 'product');
const selectedSku = computed(() => detail.value.skus?.find((item) => item.id === selectedSkuId.value) || detail.value.skus?.[0] || null);
const displayPrice = computed(() => (isProduct.value ? selectedSku.value?.price : detail.value.basePrice) || detail.value.price || 0);
const tags = computed(() => (isProduct.value ? detail.value.highlights || [] : detail.value.guarantees || []));
const previewComments = computed(() => comments.value.slice(0, 2));
const processList = computed(() => {
  if (!isProduct.value) {
    return detail.value.process || [];
  }
  return [
    '完成支付后系统自动生成商品订单',
    detail.value.createInstallOrder ? '平台同步生成安装工单并推送附近师傅' : '仓库开始备货并安排发货',
    detail.value.createInstallOrder ? '师傅联系用户确认上门时间并执行安装' : '物流送达后可在订单页查看状态',
    '订单全程可在订单中心查看进度',
  ];
});

async function loadPage() {
  const loader = isProduct.value ? getProductDetail : getServiceDetail;
  const res = await loader(currentId.value);
  detail.value = res.data || {};
  if (isProduct.value && detail.value.skus?.length) {
    selectedSkuId.value = detail.value.skus[0].id;
  }
  if (!isProduct.value) {
    const commentRes = await getServiceComments(currentId.value);
    comments.value = commentRes.data || [];
  }
}

function goComment() {
  uni.navigateTo({
    url: `/pages/goods/comment?serviceItemId=${currentId.value}`,
  });
}

function goOrderList() {
  uni.switchTab({ url: '/pages/order/list' });
}

function goChat() {
  uni.setStorageSync('hzy-chat-session', 'MS-001');
  uni.switchTab({ url: '/pages/message/chat' });
}

function goCheckout() {
  uni.navigateTo({
    url: `/pages/order/checkout?serviceItemId=${currentId.value}&title=${encodeURIComponent(detail.value.title || '')}`,
  });
}

async function buyProduct() {
  if (!selectedSku.value) {
    uni.showToast({ title: '请选择商品规格', icon: 'none' });
    return;
  }
  submitting.value = true;
  try {
    const createRes = await createProductOrder({
      productId: currentId.value,
      skuId: selectedSku.value.id,
    });
    const orderId = createRes.data.id;
    try {
      const prepayRes = await requestWechatPrepay(orderId);
      await launchWechatPay(prepayRes.data);
      uni.showToast({
        title: '支付结果确认中，请稍后查看订单状态',
        icon: 'none',
      });
    } catch (error) {
      if (error?.errMsg?.includes('cancel')) {
        uni.showToast({ title: '你已取消支付，可稍后在订单详情继续支付', icon: 'none' });
      }
    }
    setTimeout(() => {
      uni.redirectTo({ url: `/pages/order/detail?id=${orderId}` });
    }, 400);
  } finally {
    submitting.value = false;
  }
}

function handlePrimaryAction() {
  if (isProduct.value) {
    buyProduct();
    return;
  }
  goCheckout();
}

onLoad((options) => {
  detailMode.value = options.type === 'product' ? 'product' : 'service';
  currentId.value = Number(options.id || (detailMode.value === 'product' ? 1001 : 201));
  loadPage();
});
</script>

<style scoped>
/* 页面主体 */
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

/* SKU 与流程 */
.goods-page__sku-list {
  display: flex;
  flex-direction: column;
  gap: 14rpx;
}

.goods-page__sku {
  padding: 22rpx 24rpx;
  border-radius: 24rpx;
  background: #f7f8fb;
  border: 2rpx solid transparent;
}

.goods-page__sku--active {
  border-color: #2b5cff;
  background: rgba(43, 92, 255, 0.06);
}

.goods-page__sku-name {
  font-size: 26rpx;
  font-weight: 700;
}

.goods-page__sku-meta {
  margin-top: 8rpx;
  font-size: 22rpx;
  color: #667085;
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

/* 评论区域 */
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

/* 底部操作栏 */
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
  display: flex;
  gap: 12rpx;
}

.goods-page__dock-item {
  min-width: 88rpx;
  padding: 16rpx 18rpx;
  border-radius: 20rpx;
  background: #eef2ff;
  color: #2b5cff;
  text-align: center;
  font-size: 24rpx;
  font-weight: 700;
}

.goods-page__dock-btn {
  flex: 1;
}
</style>
