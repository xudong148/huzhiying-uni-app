<template>
  <view class="page-container category-page">
    <view class="category-page__shell">
      <!-- 左侧类目菜单 -->
      <view class="category-page__menu">
        <view
          v-for="(item, index) in categories"
          :key="item.id"
          class="category-page__menu-item"
          :class="{ 'category-page__menu-item--active': index === activeIndex }"
          @tap="activeIndex = index"
        >
          {{ item.name }}
        </view>
      </view>

      <!-- 右侧分类内容 -->
      <scroll-view scroll-y class="category-page__content" :show-scrollbar="false">
        <view class="card category-page__hero">
          <view class="category-page__hero-title">{{ activeCategory.name }}</view>
          <view class="category-page__hero-desc">{{ activeCategory.subs.join(' / ') }}</view>
          <image class="category-page__hero-icon" :src="activeCategory.icon" mode="aspectFit" />
        </view>

        <view v-for="group in activeCategory.groups" :key="group.title" class="category-page__group">
          <view class="section-title">
            <text class="section-title__text">{{ group.title }}</text>
          </view>
          <view class="category-page__grid">
            <view
              v-for="service in group.list"
              :key="service.id"
              class="card category-page__service pressable"
              @tap="goGoods(service.id)"
            >
              <image class="category-page__service-icon" :src="service.icon" mode="aspectFit" />
              <view class="category-page__service-name">{{ service.name }}</view>
              <view class="category-page__service-price">¥{{ service.price }} 起</view>
            </view>
          </view>
        </view>

        <view class="safe-bottom"></view>
      </scroll-view>
    </view>
  </view>
</template>

<script setup>
/**
 * 分类页。
 * 1. 左侧主类目和右侧服务分组都来自真实接口。
 * 2. 首页选中的类目通过事件总线同步到当前页。
 */
import { computed, onMounted, onUnmounted, ref } from 'vue';
import { onShow } from '@dcloudio/uni-app';
import { getCategoryTree } from '../../api/service';
import { safeAsync } from '../../utils/page-task';

const categories = ref([]);
const activeIndex = ref(0);

const activeCategory = computed(() => categories.value[activeIndex.value] || {
  name: '暂无类目',
  icon: '/static/icons/screwdriver.svg',
  subs: [],
  groups: [],
});

function goGoods(id) {
  uni.navigateTo({ url: `/pages/goods/detail?id=${id}` });
}

function findCategoryIndex(identifier) {
  if (identifier === undefined || identifier === null || identifier === '') {
    return -1;
  }

  const normalized = String(identifier);
  return categories.value.findIndex((item) => (
    String(item.id) === normalized
    || item.name === normalized
    || (item.subs || []).includes(normalized)
  ));
}

function handleCategorySelected(identifier) {
  const index = findCategoryIndex(identifier);
  if (index >= 0) {
    activeIndex.value = index;
  }
}

function applyPendingCategory() {
  const selected = uni.getStorageSync('hzy-category-selection');
  if (!selected) {
    return;
  }

  uni.removeStorageSync('hzy-category-selection');
  handleCategorySelected(selected);
}

const safeLoadCategories = safeAsync(async () => {
  const res = await getCategoryTree();
  categories.value = res.data || [];
  applyPendingCategory();
}, '加载分类页');

onMounted(() => {
  uni.$on('category-selected', handleCategorySelected);
  safeLoadCategories();
});

onShow(() => {
  applyPendingCategory();
});

onUnmounted(() => {
  uni.$off('category-selected', handleCategorySelected);
});
</script>

<style scoped>
/* 页面布局 */
.category-page {
  background: #f4f6f9;
}

.category-page__shell {
  display: flex;
  height: calc(100vh - 88rpx);
}

.category-page__menu {
  width: 190rpx;
  padding: 20rpx 0;
  background: #f7f8fb;
}

.category-page__menu-item {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  height: 96rpx;
  color: #7b8192;
  font-size: 26rpx;
  font-weight: 600;
}

.category-page__menu-item--active {
  color: #1c1e26;
  background: #ffffff;
}

.category-page__menu-item--active::before {
  content: '';
  position: absolute;
  left: 0;
  top: 30rpx;
  bottom: 30rpx;
  width: 8rpx;
  border-radius: 0 999rpx 999rpx 0;
  background: #2b5cff;
}

.category-page__content {
  flex: 1;
  padding: 20rpx;
}

/* 顶部类目卡片 */
.category-page__hero {
  position: relative;
  overflow: hidden;
  padding: 28rpx;
  background: linear-gradient(135deg, #2b5cff 0%, #8ab4f8 100%);
}

.category-page__hero-title {
  font-size: 34rpx;
  font-weight: 800;
  color: #ffffff;
}

.category-page__hero-desc {
  margin-top: 10rpx;
  font-size: 24rpx;
  color: rgba(255, 255, 255, 0.86);
}

.category-page__hero-icon {
  position: absolute;
  right: 24rpx;
  top: 24rpx;
  width: 100rpx;
  height: 100rpx;
}

/* 服务宫格 */
.category-page__group {
  margin-top: 24rpx;
}

.category-page__grid {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 16rpx;
}

.category-page__service {
  padding: 24rpx 18rpx;
  text-align: center;
}

.category-page__service-icon {
  width: 56rpx;
  height: 56rpx;
}

.category-page__service-name {
  margin-top: 12rpx;
  min-height: 72rpx;
  font-size: 24rpx;
  font-weight: 700;
  line-height: 1.4;
}

.category-page__service-price {
  margin-top: 10rpx;
  color: #ff4a4a;
  font-size: 22rpx;
}
</style>
