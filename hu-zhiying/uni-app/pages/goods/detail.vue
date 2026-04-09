<template>
  <view class="page-container goods-page">
    <scroll-view scroll-y class="goods-page__scroll" :show-scrollbar="false">
      <swiper class="goods-page__swiper" circular autoplay indicator-dots>
        <swiper-item v-for="item in galleryImages" :key="item">
          <image class="goods-page__image" :src="item" mode="aspectFill" />
        </swiper-item>
      </swiper>

      <view class="page-shell goods-page__body">
        <view class="card goods-page__summary">
          <view class="goods-page__title">{{ detail.title || (isProduct ? '精选商品' : '上门服务') }}</view>
          <view class="goods-page__subtitle">{{ detail.subtitle || defaultSubtitle }}</view>

          <view class="goods-page__price-row">
            <price-format :value="displayPrice" :suffix="priceSuffix"></price-format>
            <text v-if="showOriginalPrice" class="goods-page__origin-price">¥{{ Number(detail.tagPrice || detail.price || 0).toFixed(2) }}</text>
          </view>

          <view class="goods-page__guides">
            <text v-for="item in guideLines" :key="item" class="goods-page__guide">{{ item }}</text>
          </view>

          <view class="goods-page__guarantees">
            <text v-for="item in tags" :key="item" class="tag">{{ item }}</text>
          </view>
        </view>

        <view v-if="isProduct" class="card goods-page__section">
          <view class="section-title">
            <text class="section-title__text">规格选择</text>
            <text class="section-title__desc">下单前请确认型号、价格和库存</text>
          </view>
          <view v-if="detail.skus?.length" class="goods-page__sku-list">
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
          <view v-else class="goods-page__empty">当前商品暂未配置规格，建议联系平台先确认后再下单。</view>
        </view>

        <view class="card goods-page__section">
          <view class="section-title">
            <text class="section-title__text">{{ isProduct ? '下单后会发生什么' : '服务流程' }}</text>
            <text class="section-title__desc">{{ isProduct ? '把收货、安装和售后路径说清楚' : '让用户知道预约后下一步是什么' }}</text>
          </view>
          <view v-for="(item, index) in processList" :key="`${index}-${item}`" class="goods-page__process-item">
            <view class="goods-page__process-index">{{ index + 1 }}</view>
            <text class="goods-page__process-text">{{ item }}</text>
          </view>
        </view>

        <view class="card goods-page__section">
          <view class="section-title">
            <text class="section-title__text">{{ isProduct ? '购买提醒' : '服务承诺' }}</text>
          </view>
          <view v-for="item in assuranceList" :key="item" class="goods-page__assurance">{{ item }}</view>
        </view>

        <view v-if="!isProduct" class="card goods-page__section">
          <view class="section-title">
            <text class="section-title__text">用户评价</text>
            <text class="section-title__desc" @tap="goComment">查看全部</text>
          </view>
          <view v-if="previewComments.length">
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
          <view v-else class="goods-page__empty">还没有评价，首批下单用户的反馈会同步展示在这里。</view>
        </view>

        <view class="safe-bottom"></view>
      </view>
    </scroll-view>

    <view class="goods-page__dock">
      <view class="goods-page__dock-actions">
        <view class="goods-page__dock-item" @tap="goChat">{{ isProduct ? '售前咨询' : '在线客服' }}</view>
        <view class="goods-page__dock-item" @tap="isProduct ? goOrderList() : goComment()">{{ isProduct ? '订单' : '评价' }}</view>
      </view>
      <button class="primary-btn goods-page__dock-btn" :loading="submitting" @tap="handlePrimaryAction">
        {{ isProduct ? '立即购买' : '立即预约' }}
      </button>
    </view>
  </view>
</template>

<script setup>
import { computed, ref } from 'vue';
import { onLoad } from '@dcloudio/uni-app';
import PriceFormat from '../../components/price-format.vue';
import { createProductOrder, requestWechatPrepay } from '../../api/order';
import { getProductDetail, getServiceComments, getServiceDetail } from '../../api/service';
import { setPendingChatTarget } from '../../utils/message-center';
import { safeAsync } from '../../utils/page-task';
import { getRequestErrorMessage } from '../../utils/request';
import { isWechatPayCanceled, launchWechatPay } from '../../utils/wechat-pay';

const detailMode = ref('service');
const currentId = ref(201);
const selectedSkuId = ref(null);
const submitting = ref(false);
const detail = ref({
  images: [],
  guarantees: [],
  process: [],
  skus: [],
  highlights: [],
});
const comments = ref([]);

const isProduct = computed(() => detailMode.value === 'product');
const selectedSku = computed(() => detail.value.skus?.find((item) => item.id === selectedSkuId.value) || detail.value.skus?.[0] || null);
const displayPrice = computed(() => (isProduct.value ? selectedSku.value?.price : detail.value.basePrice) || detail.value.price || 0);
const priceSuffix = computed(() => (isProduct.value ? '起' : '基础检测费'));
const showOriginalPrice = computed(() => isProduct.value && Number(detail.value.tagPrice || 0) > Number(displayPrice.value || 0));
const galleryImages = computed(() => detail.value.images?.length ? detail.value.images : ['/static/icons/screwdriver.svg']);
const tags = computed(() => (isProduct.value ? detail.value.highlights || [] : detail.value.guarantees || []));
const previewComments = computed(() => comments.value.slice(0, 2));

const defaultSubtitle = computed(() => (
  isProduct.value
    ? '平台精选商品，支持下单后查看发货与安装进度。'
    : '平台严选上门服务，支持预约、支付、订单跟踪和售后处理。'
));

const guideLines = computed(() => {
  if (isProduct.value) {
    return [
      detail.value.deliveryDesc || '下单后自动进入商品订单，平台同步跟踪发货进度。',
      detail.value.createInstallOrder ? '支付成功后会自动生成安装工单，避免你重复下单。' : '如需安装服务，可在订单完成后继续预约上门。',
    ];
  }
  return [
    `上门费 ¥${Number(detail.value.doorPrice || 0).toFixed(2)} / 指导价 ¥${Number(detail.value.guidePrice || 0).toFixed(2)}`,
    `质保说明：${detail.value.warranty || '按平台服务规范执行售后保障。'}`,
  ];
});

const processList = computed(() => {
  if (!isProduct.value) {
    return detail.value.process?.length
      ? detail.value.process
      : ['提交预约并支付预付款', '平台派单并同步师傅联系你', '上门检测施工并回传履约进度', '订单完成后可继续评价或申请售后'];
  }
  return [
    '完成支付后系统自动生成商品订单',
    detail.value.createInstallOrder ? '平台同步生成安装工单并匹配附近师傅' : '仓库开始备货并安排发货',
    detail.value.createInstallOrder ? '师傅会联系你确认上门时间并完成安装' : '商品送达后可在订单页查看物流与售后入口',
    '订单全程都能在订单中心查看进度',
  ];
});

const assuranceList = computed(() => {
  if (isProduct.value) {
    return [
      '商品订单支持支付后查看状态，取消支付也不会丢单。',
      '如商品关联安装，安装工单会自动创建并贯通到订单体系。',
      '售后入口统一走订单详情，避免用户重复找入口。',
    ];
  }
  return [
    '下单后会进入真实派单链路，不再停留在演示数据。',
    '支付失败或取消后，可以在订单详情继续支付，不会重复创建工单。',
    '如服务未达标，可直接从订单详情进入售后申请。',
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

const safeLoadPage = safeAsync(loadPage, '加载详情页');

function goComment() {
  uni.navigateTo({
    url: `/pages/goods/comment?serviceItemId=${currentId.value}`,
  });
}

function goOrderList() {
  uni.switchTab({ url: '/pages/order/list' });
}

function goChat() {
  setPendingChatTarget({
    sessionId: 'MS-001',
    orderId: '',
    autoOpen: true,
  });
  uni.switchTab({ url: '/pages/message/index' });
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
      if (isWechatPayCanceled(error)) {
        uni.showToast({ title: '你已取消支付，可稍后在订单详情继续支付', icon: 'none' });
      } else {
        uni.showToast({
          title: getRequestErrorMessage(error, '支付暂不可用'),
          icon: 'none',
        });
      }
    }
    setTimeout(() => {
      uni.redirectTo({ url: `/pages/order/detail?id=${orderId}` });
    }, 400);
  } finally {
    submitting.value = false;
  }
}

const safeBuyProduct = safeAsync(buyProduct, '创建商品订单');

async function handlePrimaryAction() {
  if (isProduct.value) {
    await safeBuyProduct();
    return;
  }
  goCheckout();
}

onLoad((options) => {
  detailMode.value = options.type === 'product' ? 'product' : 'service';
  currentId.value = Number(options.id || (detailMode.value === 'product' ? 1001 : 201));
  safeLoadPage();
});
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
  display: flex;
  align-items: baseline;
  gap: 12rpx;
}

.goods-page__origin-price {
  font-size: 24rpx;
  color: #98a2b3;
  text-decoration: line-through;
}

.goods-page__guides {
  margin-top: 12rpx;
}

.goods-page__guide {
  display: block;
  margin-top: 10rpx;
  font-size: 22rpx;
  color: #8b90a0;
  line-height: 1.6;
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

.goods-page__process-text,
.goods-page__assurance,
.goods-page__empty {
  font-size: 24rpx;
  line-height: 1.7;
  color: #475467;
}

.goods-page__assurance + .goods-page__assurance {
  margin-top: 12rpx;
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
