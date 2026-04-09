<template>
  <view class="page-shell">
    <!-- 筛选标签 -->
    <view class="goods-comment__filters">
      <view
        v-for="item in filters"
        :key="item"
        class="chip"
        :class="{ 'goods-comment__filter--active': item === active }"
        @tap="active = item"
      >
        {{ item }}
      </view>
    </view>

    <!-- 评论列表 -->
    <view v-for="item in comments" :key="item.id" class="card goods-comment__card">
      <view class="goods-comment__top">
        <text class="goods-comment__user">{{ item.user }}</text>
        <text class="muted">{{ item.date }}</text>
      </view>
      <view class="goods-comment__tags">
        <text v-for="tag in item.tags" :key="tag" class="tag">{{ tag }}</text>
      </view>
      <view class="goods-comment__content">{{ item.content }}</view>
      <view class="goods-comment__images">
        <image
          v-for="image in item.images"
          :key="image"
          class="goods-comment__image"
          :src="image"
          mode="aspectFill"
          @tap="previewImages(item.images, image)"
        />
      </view>
    </view>
  </view>
</template>

<script setup>
/**
 * 服务评论页面。
 * 1. 评论列表通过真实评论接口获取。
 * 2. 前端只做轻量评分筛选。
 */
import { computed, ref } from 'vue';
import { onLoad } from '@dcloudio/uni-app';
import { getServiceComments } from '../../api/service';
import { safeAsync } from '../../utils/page-task';

const filters = ['全部', '好评', '中评', '差评'];
const active = ref('全部');
const serviceItemId = ref(201);
const list = ref([]);

const comments = computed(() => {
  if (active.value === '好评') {
    return list.value.filter((item) => Number(item.score || 0) >= 4);
  }
  if (active.value === '中评') {
    return list.value.filter((item) => Number(item.score || 0) === 3);
  }
  if (active.value === '差评') {
    return list.value.filter((item) => Number(item.score || 0) <= 2);
  }
  return list.value;
});

function previewImages(urls, current) {
  uni.previewImage({ urls, current });
}

onLoad(safeAsync(async (options) => {
  serviceItemId.value = Number(options.serviceItemId || 201);
  const res = await getServiceComments(serviceItemId.value);
  list.value = res.data || [];
}, '加载服务评价'));
</script>

<style scoped>
/* 筛选区 */
.goods-comment__filters {
  display: flex;
  gap: 12rpx;
  flex-wrap: wrap;
  margin-bottom: 20rpx;
}

.goods-comment__filter--active {
  background: #2b5cff;
  color: #ffffff;
}

/* 评论卡片 */
.goods-comment__card {
  padding: 24rpx;
}

.goods-comment__card + .goods-comment__card {
  margin-top: 16rpx;
}

.goods-comment__top {
  display: flex;
  justify-content: space-between;
}

.goods-comment__user {
  font-size: 28rpx;
  font-weight: 700;
}

.goods-comment__tags {
  display: flex;
  flex-wrap: wrap;
  gap: 10rpx;
  margin-top: 12rpx;
}

.goods-comment__content {
  margin-top: 12rpx;
  font-size: 24rpx;
  line-height: 1.7;
}

.goods-comment__images {
  display: flex;
  gap: 12rpx;
  margin-top: 14rpx;
}

.goods-comment__image {
  width: 180rpx;
  height: 128rpx;
  border-radius: 18rpx;
}
</style>
