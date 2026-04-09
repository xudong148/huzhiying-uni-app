<template>
  <view class="community-page">
    <view class="community-head">
      <text class="community-head__eyebrow">Local Stories</text>
      <text class="community-head__title">同城圈子</text>
      <text class="community-head__desc">
        真实邻里经验、避坑提醒、案例复盘和师傅口碑都沉淀在这里。默认优先展示 {{ activeCity }} 的内容。
      </text>
    </view>

    <view class="community-toolbar">
      <view class="chip">{{ activeCity }}</view>
      <view class="chip" @tap="goPublish">发布经验</view>
    </view>

    <view v-if="!posts.length" class="community-empty">
      <text class="community-empty__title">还没有同城帖子</text>
      <text class="community-empty__desc">可以先发一条经验贴，或者切换定位后再回来看看。</text>
    </view>

    <view v-else class="community-list">
      <view v-for="item in posts" :key="item.id" class="community-card" @tap="goDetail(item.id)">
        <view class="community-card__header">
          <view>
            <text class="community-card__title">{{ item.title }}</text>
            <text class="community-card__sub">{{ item.cityName }} · {{ item.author }}</text>
          </view>
          <text class="community-card__time">{{ formatTime(item.publishedAt) }}</text>
        </view>
        <text class="community-card__summary">{{ item.summary }}</text>
        <image v-if="item.coverImage" class="community-card__cover" :src="item.coverImage" mode="aspectFill" />
        <view class="community-card__footer">
          <text>点赞 {{ item.likeCount || 0 }}</text>
          <text>评论 {{ item.commentCount || 0 }}</text>
        </view>
      </view>
    </view>

    <view class="community-publish" @tap="goPublish">
      <text>发布帖子</text>
    </view>
  </view>
</template>

<script setup>
import { computed, ref } from 'vue';
import { onLoad, onShow } from '@dcloudio/uni-app';
import { getCommunityPosts } from '../../api/service';
import { useLocationStore } from '../../stores/location';
import { safeAsync } from '../../utils/page-task';

const posts = ref([]);
const locationStore = useLocationStore();

const activeCity = computed(() => locationStore.current.city || '当前城市');

const loadPosts = safeAsync(async () => {
  const res = await getCommunityPosts(activeCity.value);
  posts.value = res.data || [];
}, '加载圈子列表');

function goDetail(id) {
  uni.navigateTo({ url: `/pages/community/detail?id=${id}` });
}

function goPublish() {
  uni.navigateTo({ url: '/pages/community/publish' });
}

function formatTime(value) {
  if (!value) return '刚刚';
  return String(value).slice(0, 10);
}

onLoad(() => {
  loadPosts();
});

onShow(() => {
  loadPosts();
});
</script>

<style lang="scss" scoped>
.community-page {
  min-height: 100vh;
  padding: 24rpx 24rpx 160rpx;
  box-sizing: border-box;
  background:
    radial-gradient(circle at top left, rgba(0, 181, 120, 0.12), transparent 32%),
    linear-gradient(180deg, #f5faf8 0%, #f8fafc 100%);
}

.community-head {
  padding: 32rpx 28rpx;
  border-radius: 28rpx;
  background: linear-gradient(135deg, #0f9f71 0%, #11b981 55%, #8de0c0 100%);
  color: #fff;
  box-shadow: 0 18rpx 42rpx rgba(17, 185, 129, 0.2);
}

.community-head__eyebrow {
  font-size: 20rpx;
  letter-spacing: 4rpx;
  opacity: 0.8;
}

.community-head__title {
  display: block;
  margin-top: 14rpx;
  font-size: 48rpx;
  font-weight: 700;
}

.community-head__desc {
  display: block;
  margin-top: 12rpx;
  font-size: 24rpx;
  line-height: 1.7;
  color: rgba(255, 255, 255, 0.88);
}

.community-toolbar {
  margin-top: 20rpx;
  display: flex;
  gap: 12rpx;
}

.community-empty {
  margin-top: 140rpx;
  text-align: center;
  color: #6b7280;
}

.community-empty__title {
  display: block;
  font-size: 32rpx;
  color: #1f2937;
  font-weight: 600;
}

.community-empty__desc {
  display: block;
  margin-top: 12rpx;
  font-size: 24rpx;
}

.community-list {
  margin-top: 24rpx;
  display: flex;
  flex-direction: column;
  gap: 20rpx;
}

.community-card {
  padding: 26rpx;
  border-radius: 26rpx;
  background: rgba(255, 255, 255, 0.97);
  box-shadow: 0 12rpx 30rpx rgba(15, 23, 42, 0.06);
}

.community-card__header,
.community-card__footer {
  display: flex;
  justify-content: space-between;
  gap: 24rpx;
}

.community-card__title {
  display: block;
  font-size: 34rpx;
  line-height: 1.45;
  color: #111827;
  font-weight: 700;
}

.community-card__sub,
.community-card__time,
.community-card__footer {
  color: #718096;
  font-size: 22rpx;
}

.community-card__sub {
  display: block;
  margin-top: 10rpx;
}

.community-card__summary {
  display: block;
  margin-top: 18rpx;
  font-size: 26rpx;
  line-height: 1.75;
  color: #46515f;
}

.community-card__cover {
  width: 100%;
  height: 280rpx;
  display: block;
  margin-top: 18rpx;
  border-radius: 20rpx;
  background: #dce8e3;
}

.community-card__footer {
  margin-top: 18rpx;
}

.community-publish {
  position: fixed;
  right: 28rpx;
  bottom: calc(44rpx + env(safe-area-inset-bottom));
  padding: 22rpx 32rpx;
  border-radius: 999rpx;
  background: #111827;
  color: #fff;
  font-size: 26rpx;
  box-shadow: 0 18rpx 36rpx rgba(17, 24, 39, 0.18);
}
</style>
