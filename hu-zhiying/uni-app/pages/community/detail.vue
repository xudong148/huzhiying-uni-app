<template>
  <scroll-view scroll-y class="community-detail">
    <image class="community-detail__cover" :src="post.coverImage" mode="aspectFill" />

    <view class="community-detail__card">
      <text class="community-detail__title">{{ post.title }}</text>
      <view class="community-detail__meta">
        <text>{{ post.cityName }} · {{ post.author }}</text>
        <text>{{ formatTime(post.publishedAt) }}</text>
      </view>
      <text class="community-detail__content">{{ post.content }}</text>

      <view v-if="post.images?.length" class="community-detail__gallery">
        <image v-for="(item, index) in post.images" :key="index" class="community-detail__image" :src="item" mode="aspectFill" />
      </view>

      <view class="community-detail__actions">
        <view class="community-detail__action" @tap="handleLike">点赞 {{ post.likeCount || 0 }}</view>
        <view class="community-detail__action" @tap="handleReport">举报</view>
      </view>
    </view>

    <view class="community-detail__comment-panel">
      <view class="community-detail__comment-head">
        <text class="community-detail__comment-title">全部评论</text>
        <text class="community-detail__comment-count">{{ comments.length }} 条</text>
      </view>

      <view v-if="!comments.length" class="community-detail__empty">还没有评论，欢迎说两句。</view>

      <view v-for="item in comments" :key="item.id" class="community-detail__comment-item">
        <view class="community-detail__comment-top">
          <text class="community-detail__comment-author">{{ item.author }}</text>
          <text class="community-detail__comment-time">{{ formatTime(item.createdAt) }}</text>
        </view>
        <text class="community-detail__comment-content">{{ item.content }}</text>
      </view>
    </view>
  </scroll-view>

  <view class="community-detail__composer">
    <input v-model="commentText" class="community-detail__input" maxlength="120" placeholder="说点有帮助的经验..." />
    <button class="community-detail__send" :loading="submitting" @tap="submitComment">发送</button>
  </view>
</template>

<script setup>
import { ref } from 'vue';
import { onLoad } from '@dcloudio/uni-app';
import {
  createCommunityComment,
  getCommunityPostDetail,
  likeCommunityPost,
  reportCommunityPost,
} from '../../api/service';
import { safeAsync, showActionError } from '../../utils/page-task';

const postId = ref('');
const submitting = ref(false);
const commentText = ref('');
const post = ref({
  title: '加载中...',
  cityName: '',
  author: '',
  content: '',
  publishedAt: '',
  coverImage: '/static/icons/community.svg',
  images: [],
  likeCount: 0,
});
const comments = ref([]);

const loadDetail = safeAsync(async (id = postId.value) => {
  const res = await getCommunityPostDetail(id);
  post.value = res.data?.post || post.value;
  comments.value = res.data?.comments || [];
}, '加载帖子详情');

const handleLike = safeAsync(async () => {
  const res = await likeCommunityPost(postId.value);
  post.value = {
    ...post.value,
    ...(res.data || {}),
  };
  uni.showToast({ title: '已点赞', icon: 'none' });
}, '点赞帖子');

const handleReport = safeAsync(async () => {
  await reportCommunityPost(postId.value, {
    reason: '内容待核验',
    detail: '由用户在客户端发起举报，请运营核查。',
  });
  uni.showToast({ title: '已提交举报', icon: 'none' });
}, '举报帖子');

const submitComment = safeAsync(async () => {
  if (!commentText.value.trim()) {
    uni.showToast({ title: '请输入评论内容', icon: 'none' });
    return;
  }

  submitting.value = true;
  try {
    const res = await createCommunityComment(postId.value, { content: commentText.value.trim() });
    comments.value = [...comments.value, res.data];
    post.value = {
      ...post.value,
      commentCount: (post.value.commentCount || 0) + 1,
    };
    commentText.value = '';
    uni.showToast({ title: '评论成功', icon: 'none' });
  } finally {
    submitting.value = false;
  }
}, '发布评论');

function formatTime(value) {
  if (!value) return '刚刚';
  return String(value).replace('T', ' ').slice(0, 16);
}

onLoad((options) => {
  if (!options?.id) {
    showActionError(new Error('缺少帖子编号'), '帖子不存在');
    return;
  }
  postId.value = options.id;
  loadDetail(options.id);
});
</script>

<style lang="scss" scoped>
.community-detail {
  min-height: 100vh;
  padding-bottom: 160rpx;
  background: linear-gradient(180deg, #edf9f4 0%, #f8fafc 100%);
}

.community-detail__cover {
  width: 100%;
  height: 420rpx;
  display: block;
  background: #dce8e3;
}

.community-detail__card,
.community-detail__comment-panel {
  margin: 24rpx;
  padding: 28rpx;
  border-radius: 28rpx;
  background: rgba(255, 255, 255, 0.97);
  box-shadow: 0 14rpx 34rpx rgba(15, 23, 42, 0.06);
}

.community-detail__title {
  display: block;
  font-size: 40rpx;
  line-height: 1.35;
  color: #111827;
  font-weight: 700;
}

.community-detail__meta,
.community-detail__comment-top,
.community-detail__comment-head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 20rpx;
  color: #6b7280;
  font-size: 22rpx;
}

.community-detail__meta {
  margin-top: 16rpx;
}

.community-detail__content {
  display: block;
  margin-top: 22rpx;
  font-size: 28rpx;
  line-height: 1.9;
  color: #334155;
  white-space: pre-wrap;
}

.community-detail__gallery {
  display: flex;
  flex-wrap: wrap;
  gap: 16rpx;
  margin-top: 24rpx;
}

.community-detail__image {
  width: calc(50% - 8rpx);
  height: 220rpx;
  border-radius: 20rpx;
  background: #dce8e3;
}

.community-detail__actions {
  display: flex;
  gap: 16rpx;
  margin-top: 24rpx;
}

.community-detail__action {
  padding: 14rpx 22rpx;
  border-radius: 999rpx;
  background: #f1f5f9;
  color: #1f2937;
  font-size: 24rpx;
}

.community-detail__comment-title {
  font-size: 30rpx;
  color: #111827;
  font-weight: 700;
}

.community-detail__empty {
  padding: 28rpx 0 8rpx;
  color: #94a3b8;
  font-size: 24rpx;
}

.community-detail__comment-item + .community-detail__comment-item {
  margin-top: 24rpx;
  padding-top: 24rpx;
  border-top: 1px solid #edf2f7;
}

.community-detail__comment-author {
  color: #111827;
  font-weight: 600;
}

.community-detail__comment-content {
  display: block;
  margin-top: 12rpx;
  font-size: 26rpx;
  line-height: 1.75;
  color: #475569;
}

.community-detail__composer {
  position: fixed;
  left: 0;
  right: 0;
  bottom: 0;
  display: flex;
  gap: 16rpx;
  padding: 20rpx 24rpx calc(20rpx + env(safe-area-inset-bottom));
  background: rgba(255, 255, 255, 0.96);
  backdrop-filter: blur(16rpx);
}

.community-detail__input {
  flex: 1;
  height: 84rpx;
  padding: 0 24rpx;
  border-radius: 999rpx;
  background: #f1f5f9;
  font-size: 26rpx;
}

.community-detail__send {
  min-width: 160rpx;
  height: 84rpx;
  line-height: 84rpx;
  border: none;
  border-radius: 999rpx;
  background: #111827;
  color: #fff;
  font-size: 26rpx;
}
</style>
