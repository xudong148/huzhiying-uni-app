<template>
  <scroll-view scroll-y class="page-container" :show-scrollbar="false">
    <view class="home page-shell">
      <view class="home__hero">
        <view class="home__headline">一呼百应 · 专业到家</view>
        <view class="home__subline">维修、安装、保洁、商城一体化本地服务平台</view>
      </view>

      <home-header
        :location-name="`${location.current.city} · ${location.current.district}`"
        :notice="topNotice"
        :keywords="keywords"
        @location="goLocation"
        @search="goSearch"
      />

      <view class="home__block">
        <express-card @tap="goCheckout"></express-card>
      </view>

      <view class="section-title">
        <text class="section-title__text">金刚区入口</text>
        <text class="section-title__desc">维修、安装、清洗、保洁</text>
      </view>
      <kingkong-nav :list="categories" @select="goCategory"></kingkong-nav>

      <view class="home__block">
        <vip-butler @tap="goCoupons"></vip-butler>
      </view>

      <view class="section-title">
        <text class="section-title__text">生态便当盒</text>
        <text class="section-title__desc">高频内容与商城转化</text>
      </view>
      <ecology-bento :list="ecologyEntries" @select="handleEcologySelect"></ecology-bento>

      <view class="section-title home__recommend-title">
        <text class="section-title__text">热门推荐</text>
        <text class="section-title__desc">猜你需要的服务和套餐</text>
      </view>

      <view class="home__feed">
        <view
          v-for="item in feedList"
          :key="item.id"
          class="home__feed-card card pressable"
          @tap="goGoods(item.id)"
        >
          <image class="home__feed-image" :src="item.image" mode="aspectFill" />
          <view class="home__feed-body">
            <view class="home__feed-name">{{ item.title }}</view>
            <view class="home__feed-meta">
              <text class="tag">{{ item.tag }}</text>
              <text class="muted">已售 {{ item.sales }}</text>
            </view>
            <price-format :value="item.price" suffix="起"></price-format>
          </view>
        </view>
      </view>

      <view class="safe-bottom"></view>
    </view>
  </scroll-view>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue';
import HomeHeader from '../../components/home-header.vue';
import ExpressCard from '../../components/express-card.vue';
import KingkongNav from '../../components/kingkong-nav.vue';
import VipButler from '../../components/vip-butler.vue';
import EcologyBento from '../../components/ecology-bento.vue';
import PriceFormat from '../../components/price-format.vue';
import { getHomeData } from '../../api/service';
import { ecologyEntries } from '../../mock/data';
import { useAppStore } from '../../stores/app';
import { useLocationStore } from '../../stores/location';

const appStore = useAppStore();
const location = useLocationStore();

const keywords = ref([]);
const categories = ref([]);
const feedList = ref([]);

const topNotice = computed(() => appStore.topNotice?.title || '新客立减券已到账');

async function loadPage() {
  const res = await getHomeData();
  keywords.value = res.data.hotKeywords;
  categories.value = res.data.categories;
  feedList.value = res.data.recommendationList;
}

function goLocation() {
  uni.navigateTo({ url: '/pages/location/select' });
}

function goSearch() {
  uni.navigateTo({ url: '/pages/search/index' });
}

function goCategory(item) {
  uni.switchTab({ url: '/pages/category/index' });
  uni.$emit('category-selected', item.id);
}

function goCoupons() {
  uni.navigateTo({ url: '/pages/marketing/coupon' });
}

function handleEcologySelect(item) {
  uni.showToast({
    title: `${item.name} 即将开放`,
    icon: 'none',
  });
}

function goGoods(id) {
  uni.navigateTo({ url: `/pages/goods/detail?id=${id}` });
}

function goCheckout() {
  uni.navigateTo({ url: '/pages/order/checkout?source=express' });
}

onMounted(async () => {
  await loadPage();
  location.locate();
});
</script>

<style scoped>
.home {
  background:
    radial-gradient(circle at top right, rgba(138, 180, 248, 0.26), transparent 28%),
    linear-gradient(180deg, #edf2ff 0%, #f4f6f9 35%, #f4f6f9 100%);
}

.home__hero {
  margin-bottom: 24rpx;
  padding-top: 16rpx;
}

.home__headline {
  font-size: 44rpx;
  font-weight: 800;
  color: #1c1e26;
}

.home__subline {
  margin-top: 10rpx;
  font-size: 24rpx;
  color: #667085;
}

.home__block {
  margin-top: 24rpx;
}

.home__recommend-title {
  margin-top: 28rpx;
}

.home__feed {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 18rpx;
}

.home__feed-card {
  overflow: hidden;
}

.home__feed-image {
  width: 100%;
  height: 280rpx;
}

.home__feed-body {
  padding: 20rpx;
}

.home__feed-name {
  min-height: 76rpx;
  font-size: 28rpx;
  font-weight: 700;
  line-height: 1.4;
}

.home__feed-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin: 18rpx 0;
}
</style>
