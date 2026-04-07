<template>
  <view class="page-shell">
    <view class="card search-page__box">
      <input v-model="keyword" placeholder="搜索故障、服务、商品" />
    </view>

    <view class="section-title search-page__section">
      <text class="section-title__text">热门搜索</text>
    </view>
    <view class="search-page__chips">
      <view v-for="item in keywords" :key="item" class="chip" @tap="keyword = item">{{ item }}</view>
    </view>

    <view class="section-title search-page__section">
      <text class="section-title__text">智能联想</text>
    </view>
    <view class="card search-page__suggestion">
      <view class="search-page__row" v-for="item in suggestionList" :key="item" @tap="handleSuggestion(item)">
        {{ item }}
      </view>
    </view>
  </view>
</template>

<script setup>
import { computed, ref } from 'vue';
import { hotKeywords } from '../../mock/data';

const keyword = ref('');
const keywords = hotKeywords;

const suggestionList = computed(() => {
  const list = [
    '空调不制冷 上门维修',
    '空调清洗 抗菌套餐',
    '智能锁安装 标准服务',
    '厨房漏水 检测维修',
  ];
  if (!keyword.value) {
    return list;
  }
  return list.filter((item) => item.includes(keyword.value));
});

function handleSuggestion(item) {
  uni.navigateTo({ url: `/pages/goods/detail?keyword=${encodeURIComponent(item)}` });
}
</script>

<style scoped>
.search-page__box {
  padding: 18rpx 26rpx;
}

.search-page__box input {
  height: 68rpx;
  font-size: 28rpx;
}

.search-page__section {
  margin-top: 24rpx;
}

.search-page__chips {
  display: flex;
  flex-wrap: wrap;
  gap: 14rpx;
}

.search-page__suggestion {
  padding: 8rpx 24rpx;
}

.search-page__row {
  padding: 24rpx 0;
  font-size: 28rpx;
  border-bottom: 1rpx solid #edf1f6;
}

.search-page__row:last-child {
  border-bottom: none;
}
</style>
