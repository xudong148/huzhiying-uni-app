<template>
  <view class="mall-page">
    <view class="mall-hero">
      <text class="mall-hero__eyebrow">HZY Mall</text>
      <text class="mall-hero__title">精选商城</text>
      <text class="mall-hero__desc">
        安装耗材、五金配件和平台精选商品统一在这里下单，支付后可直接进入商品订单与安装联动链路。
      </text>
    </view>

    <view v-if="!products.length && !loading" class="mall-empty">
      <text class="mall-empty__title">暂时没有上架商品</text>
      <text class="mall-empty__desc">运营后台上架后会自动展示在这里，当前可先回首页浏览服务类目。</text>
      <button class="mall-empty__btn" @tap="reloadProducts">重新加载</button>
    </view>

    <view v-else class="mall-list">
      <view v-for="item in products" :key="item.id" class="mall-card" @tap="goDetail(item.id)">
        <image class="mall-card__cover" :src="item.coverImage" mode="aspectFill" />
        <view class="mall-card__body">
          <view class="mall-card__meta">
            <text class="mall-card__badge">{{ item.createInstallOrder ? '含安装联动' : '现货发货' }}</text>
            <text class="mall-card__sku">{{ item.skuCount || 0 }} 个规格</text>
          </view>
          <text class="mall-card__title">{{ item.title }}</text>
          <text class="mall-card__summary">{{ item.summary || '平台精选配件与耗材，支持下单后跟踪发货和安装进度。' }}</text>
          <view class="mall-card__footer">
            <view class="mall-card__price-box">
              <text class="mall-card__currency">¥</text>
              <text class="mall-card__price">{{ formatPrice(item.price) }}</text>
            </view>
            <text v-if="Number(item.tagPrice || 0) > Number(item.price || 0)" class="mall-card__tag-price">
              ¥{{ formatPrice(item.tagPrice) }}
            </text>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref } from 'vue';
import { onLoad, onShow } from '@dcloudio/uni-app';
import { getProductList } from '../../api/service';
import { safeAsync, showActionError } from '../../utils/page-task';

const products = ref([]);
const loading = ref(false);

const loadProducts = safeAsync(async () => {
  loading.value = true;
  try {
    const res = await getProductList();
    products.value = res.data || [];
  } finally {
    loading.value = false;
  }
}, '加载商城列表');

async function reloadProducts() {
  try {
    await loadProducts();
  } catch (error) {
    showActionError(error, '重新加载商城失败');
  }
}

function goDetail(id) {
  uni.navigateTo({ url: `/pages/goods/detail?id=${id}&type=product` });
}

function formatPrice(value) {
  return Number(value || 0).toFixed(2);
}

onLoad(() => {
  loadProducts();
});

onShow(() => {
  if (!products.value.length) {
    loadProducts();
  }
});
</script>

<style lang="scss" scoped>
.mall-page {
  min-height: 100vh;
  padding: 24rpx;
  box-sizing: border-box;
  background:
    radial-gradient(circle at top right, rgba(255, 125, 0, 0.12), transparent 30%),
    linear-gradient(180deg, #fff8f2 0%, #f8fafc 100%);
}

.mall-hero {
  padding: 32rpx 28rpx;
  border-radius: 28rpx;
  background: linear-gradient(135deg, #d46a00 0%, #ff7d00 55%, #ffd2a6 100%);
  color: #fff;
  box-shadow: 0 18rpx 44rpx rgba(212, 106, 0, 0.2);
}

.mall-hero__eyebrow {
  font-size: 20rpx;
  letter-spacing: 4rpx;
  opacity: 0.78;
}

.mall-hero__title {
  display: block;
  margin-top: 14rpx;
  font-size: 48rpx;
  font-weight: 700;
}

.mall-hero__desc {
  display: block;
  margin-top: 12rpx;
  font-size: 24rpx;
  line-height: 1.7;
  color: rgba(255, 255, 255, 0.88);
}

.mall-empty {
  margin-top: 120rpx;
  text-align: center;
  color: #6b7280;
}

.mall-empty__title {
  display: block;
  font-size: 32rpx;
  font-weight: 700;
  color: #1f2937;
}

.mall-empty__desc {
  display: block;
  margin-top: 12rpx;
  font-size: 24rpx;
  line-height: 1.7;
}

.mall-empty__btn {
  margin-top: 28rpx;
  width: 260rpx;
  height: 84rpx;
  line-height: 84rpx;
  border: none;
  border-radius: 999rpx;
  background: #111827;
  color: #fff;
  font-size: 26rpx;
}

.mall-list {
  margin-top: 24rpx;
  display: flex;
  flex-direction: column;
  gap: 20rpx;
}

.mall-card {
  overflow: hidden;
  border-radius: 28rpx;
  background: rgba(255, 255, 255, 0.96);
  box-shadow: 0 12rpx 30rpx rgba(15, 23, 42, 0.06);
}

.mall-card__cover {
  width: 100%;
  height: 260rpx;
  display: block;
  background: #f1e3d6;
}

.mall-card__body {
  padding: 24rpx;
}

.mall-card__meta,
.mall-card__footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 18rpx;
}

.mall-card__badge {
  padding: 8rpx 14rpx;
  border-radius: 999rpx;
  background: rgba(255, 125, 0, 0.12);
  color: #c25b00;
  font-size: 22rpx;
  font-weight: 600;
}

.mall-card__sku {
  color: #7b8490;
  font-size: 22rpx;
}

.mall-card__title {
  display: block;
  margin-top: 16rpx;
  font-size: 34rpx;
  line-height: 1.45;
  color: #111827;
  font-weight: 700;
}

.mall-card__summary {
  display: block;
  margin-top: 12rpx;
  font-size: 25rpx;
  line-height: 1.7;
  color: #4b5563;
}

.mall-card__footer {
  margin-top: 18rpx;
}

.mall-card__price-box {
  display: flex;
  align-items: flex-end;
  color: #ff4a4a;
  font-weight: 700;
}

.mall-card__currency {
  font-size: 22rpx;
  margin-right: 4rpx;
}

.mall-card__price {
  font-size: 40rpx;
  line-height: 1;
}

.mall-card__tag-price {
  font-size: 22rpx;
  color: #9ca3af;
  text-decoration: line-through;
}
</style>
