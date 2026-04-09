<template>
  <scroll-view scroll-y class="academy-detail">
    <image class="academy-detail__cover" :src="detail.coverImage" mode="aspectFill" />

    <view class="academy-detail__card">
      <view class="academy-detail__meta">
        <text class="academy-detail__tag">{{ detail.category?.name || '学堂精选' }}</text>
        <text>{{ detail.author || '平台教研组' }}</text>
      </view>
      <text class="academy-detail__title">{{ detail.title }}</text>
      <text class="academy-detail__summary">{{ detail.summary }}</text>
      <text class="academy-detail__time">{{ formatTime(detail.publishedAt) }} · {{ detail.viewCount || 0 }} 阅读</text>
    </view>

    <view class="academy-detail__content">
      <text class="academy-detail__content-text">{{ detail.content }}</text>
    </view>
  </scroll-view>
</template>

<script setup>
import { ref } from 'vue';
import { onLoad } from '@dcloudio/uni-app';
import { getAcademyArticleDetail } from '../../api/service';
import { safeAsync, showActionError } from '../../utils/page-task';

const detail = ref({
  title: '加载中...',
  summary: '',
  content: '',
  coverImage: '/static/icons/school.svg',
  author: '',
  category: null,
  publishedAt: '',
  viewCount: 0,
});

const loadDetail = safeAsync(async (id) => {
  const res = await getAcademyArticleDetail(id);
  detail.value = {
    ...detail.value,
    ...(res.data || {}),
  };
}, '加载学堂详情');

function formatTime(value) {
  if (!value) return '最近更新';
  return String(value).replace('T', ' ').slice(0, 16);
}

onLoad((options) => {
  const id = options?.id;
  if (!id) {
    showActionError(new Error('缺少文章编号'), '文章不存在');
    return;
  }
  loadDetail(id);
});
</script>

<style lang="scss" scoped>
.academy-detail {
  min-height: 100vh;
  background: linear-gradient(180deg, #eef3fb 0%, #f9fafc 100%);
}

.academy-detail__cover {
  width: 100%;
  height: 420rpx;
  display: block;
  background: #d9e2f1;
}

.academy-detail__card {
  margin: -52rpx 24rpx 0;
  padding: 28rpx;
  border-radius: 28rpx;
  background: rgba(255, 255, 255, 0.97);
  box-shadow: 0 18rpx 42rpx rgba(15, 23, 42, 0.08);
  position: relative;
  z-index: 2;
}

.academy-detail__meta {
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 22rpx;
  color: #6b7280;
}

.academy-detail__tag {
  padding: 8rpx 16rpx;
  border-radius: 999rpx;
  background: rgba(43, 92, 255, 0.1);
  color: #2b5cff;
}

.academy-detail__title {
  display: block;
  margin-top: 18rpx;
  font-size: 42rpx;
  line-height: 1.35;
  color: #111827;
  font-weight: 700;
}

.academy-detail__summary {
  display: block;
  margin-top: 14rpx;
  font-size: 26rpx;
  line-height: 1.7;
  color: #4b5563;
}

.academy-detail__time {
  display: block;
  margin-top: 18rpx;
  font-size: 22rpx;
  color: #8a94a3;
}

.academy-detail__content {
  margin: 24rpx;
  padding: 32rpx 28rpx 56rpx;
  border-radius: 28rpx;
  background: rgba(255, 255, 255, 0.96);
}

.academy-detail__content-text {
  font-size: 29rpx;
  line-height: 1.92;
  color: #1f2937;
  white-space: pre-wrap;
}
</style>
