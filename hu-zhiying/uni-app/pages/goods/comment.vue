<template>
  <view class="page-shell">
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

    <view v-for="item in comments" :key="item.id" class="card goods-comment__card">
      <view class="goods-comment__top">
        <text class="goods-comment__user">{{ item.user }}</text>
        <text class="muted">{{ item.date }}</text>
      </view>
      <view class="goods-comment__content">{{ item.content }}</view>
      <view class="goods-comment__images">
        <image v-for="image in item.images" :key="image" class="goods-comment__image" :src="image" mode="aspectFill" />
      </view>
    </view>
  </view>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue';
import { getServiceDetail } from '../../api/service';

const filters = ['全部', '好评', '中评', '差评'];
const active = ref('全部');
const list = ref([]);

const comments = computed(() => list.value);

onMounted(async () => {
  const res = await getServiceDetail();
  list.value = res.data.comments;
});
</script>

<style scoped>
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
