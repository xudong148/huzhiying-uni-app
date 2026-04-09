<template>
  <view class="academy-page">
    <view class="academy-hero">
      <text class="academy-hero__eyebrow">HZY Academy</text>
      <text class="academy-hero__title">小应学堂</text>
      <text class="academy-hero__desc">
        维修经验、交付标准、售后案例和平台训练素材都沉淀在这里，方便用户决策也方便师傅进阶。
      </text>
    </view>

    <scroll-view scroll-x class="academy-tabs" :show-scrollbar="false">
      <view class="academy-tabs__inner">
        <view
          class="academy-tab"
          :class="{ 'academy-tab--active': activeCategoryId === 0 }"
          @tap="switchCategory(0)"
        >
          全部
        </view>
        <view
          v-for="item in categories"
          :key="item.id"
          class="academy-tab"
          :class="{ 'academy-tab--active': activeCategoryId === item.id }"
          @tap="switchCategory(item.id)"
        >
          {{ item.name }}
        </view>
      </view>
    </scroll-view>

    <view v-if="!articles.length" class="academy-empty">
      <text class="academy-empty__title">暂时还没有学堂内容</text>
      <text class="academy-empty__desc">后台发布后会自动出现在这里，可用于首页、搜索和详情页联动。</text>
    </view>

    <view v-else class="academy-list">
      <view v-for="item in articles" :key="item.id" class="academy-card" @tap="goDetail(item.id)">
        <image class="academy-card__cover" :src="item.coverImage" mode="aspectFill" />
        <view class="academy-card__body">
          <view class="academy-card__meta">
            <text class="academy-card__tag">{{ item.categoryName || '精选课程' }}</text>
            <text class="academy-card__read">{{ item.viewCount || 0 }} 阅读</text>
          </view>
          <text class="academy-card__title">{{ item.title }}</text>
          <text class="academy-card__summary">{{ item.summary }}</text>
          <view class="academy-card__footer">
            <text>{{ item.author || '平台教研组' }}</text>
            <text>{{ formatTime(item.publishedAt) }}</text>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref } from 'vue';
import { onLoad } from '@dcloudio/uni-app';
import { getAcademyArticles, getAcademyCategories } from '../../api/service';
import { safeAsync, showActionError } from '../../utils/page-task';

const categories = ref([]);
const articles = ref([]);
const activeCategoryId = ref(0);

const loadCategories = safeAsync(async () => {
  const res = await getAcademyCategories();
  categories.value = res.data || [];
}, '加载学堂栏目');

const loadArticles = safeAsync(async (categoryId = activeCategoryId.value) => {
  const res = await getAcademyArticles(categoryId || undefined);
  articles.value = res.data || [];
}, '加载学堂文章');

const bootPage = safeAsync(async () => {
  await loadCategories();
  await loadArticles(0);
}, '初始化学堂首页');

async function switchCategory(categoryId) {
  activeCategoryId.value = categoryId;
  try {
    await loadArticles(categoryId);
  } catch (error) {
    showActionError(error, '切换栏目失败');
  }
}

function goDetail(id) {
  uni.navigateTo({ url: `/pages/academy/detail?id=${id}` });
}

function formatTime(value) {
  if (!value) return '最近更新';
  return String(value).slice(0, 10);
}

onLoad(() => {
  bootPage();
});
</script>

<style lang="scss" scoped>
.academy-page {
  min-height: 100vh;
  background:
    radial-gradient(circle at top right, rgba(43, 92, 255, 0.12), transparent 28%),
    linear-gradient(180deg, #f6f8fc 0%, #eef2f7 100%);
  padding: 24rpx;
  box-sizing: border-box;
}

.academy-hero {
  padding: 32rpx 28rpx;
  border-radius: 28rpx;
  background: linear-gradient(135deg, #173ca8 0%, #2b5cff 58%, #8eb1ff 100%);
  box-shadow: 0 18rpx 44rpx rgba(23, 60, 168, 0.2);
  color: #fff;
}

.academy-hero__eyebrow {
  font-size: 20rpx;
  letter-spacing: 4rpx;
  opacity: 0.78;
}

.academy-hero__title {
  display: block;
  margin-top: 14rpx;
  font-size: 48rpx;
  font-weight: 700;
}

.academy-hero__desc {
  display: block;
  margin-top: 12rpx;
  font-size: 24rpx;
  line-height: 1.6;
  color: rgba(255, 255, 255, 0.88);
}

.academy-tabs {
  margin: 28rpx 0 20rpx;
}

.academy-tabs__inner {
  display: inline-flex;
  gap: 16rpx;
  padding-right: 24rpx;
}

.academy-tab {
  padding: 14rpx 26rpx;
  border-radius: 999rpx;
  background: rgba(255, 255, 255, 0.86);
  color: #5d6572;
  font-size: 24rpx;
  white-space: nowrap;
  border: 1px solid rgba(43, 92, 255, 0.08);
}

.academy-tab--active {
  background: #173ca8;
  color: #fff;
  box-shadow: 0 10rpx 20rpx rgba(23, 60, 168, 0.2);
}

.academy-empty {
  margin-top: 120rpx;
  text-align: center;
  color: #7b8490;
}

.academy-empty__title {
  display: block;
  font-size: 30rpx;
  font-weight: 600;
  color: #2c3440;
}

.academy-empty__desc {
  display: block;
  margin-top: 12rpx;
  font-size: 24rpx;
  line-height: 1.7;
}

.academy-list {
  display: flex;
  flex-direction: column;
  gap: 24rpx;
}

.academy-card {
  overflow: hidden;
  border-radius: 28rpx;
  background: rgba(255, 255, 255, 0.96);
  box-shadow: 0 12rpx 30rpx rgba(15, 23, 42, 0.06);
}

.academy-card__cover {
  width: 100%;
  height: 320rpx;
  display: block;
  background: #d9e2f1;
}

.academy-card__body {
  padding: 24rpx;
}

.academy-card__meta,
.academy-card__footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 22rpx;
  color: #7b8490;
}

.academy-card__tag {
  padding: 8rpx 14rpx;
  border-radius: 999rpx;
  background: rgba(43, 92, 255, 0.1);
  color: #2b5cff;
}

.academy-card__title {
  display: block;
  margin-top: 18rpx;
  font-size: 34rpx;
  line-height: 1.45;
  color: #1f2937;
  font-weight: 700;
}

.academy-card__summary {
  display: block;
  margin-top: 14rpx;
  font-size: 25rpx;
  line-height: 1.7;
  color: #5c6675;
}

.academy-card__footer {
  margin-top: 20rpx;
}
</style>
