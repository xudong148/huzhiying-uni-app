<template>
  <view class="page-shell">
    <view class="card search-page__box">
      <input v-model="keyword" placeholder="搜索故障、服务或商品" @confirm="loadSuggestions" />
    </view>

    <view class="section-title search-page__section">
      <text class="section-title__text">热门搜索</text>
    </view>
    <view class="search-page__chips">
      <view v-for="item in keywords" :key="item" class="chip" @tap="useKeyword(item)">{{ item }}</view>
    </view>

    <view class="section-title search-page__section">
      <text class="section-title__text">智能联想</text>
    </view>
    <view class="card search-page__suggestion">
      <view class="search-page__row" v-for="item in suggestionList" :key="item.id" @tap="handleSuggestion(item)">
        <view class="search-page__row-top">
          <view class="search-page__row-title">{{ item.title }}</view>
          <text class="chip chip--small">{{ item.type === 'product' ? '商品' : '服务' }}</text>
        </view>
        <view class="search-page__row-desc">{{ item.summary }}</view>
      </view>
    </view>
  </view>
</template>

<script setup>
import { onMounted, ref, watch } from 'vue';
import { searchCatalog } from '../../api/service';

const keyword = ref('');
const keywords = ['空调不制冷', '智能锁安装', '厨房漏水', '深度保洁', '油烟机清洗'];
const suggestionList = ref([]);

async function loadSuggestions() {
  const res = await searchCatalog(keyword.value.trim());
  suggestionList.value = res.data || [];
}

function useKeyword(value) {
  keyword.value = value;
  loadSuggestions();
}

function handleSuggestion(item) {
  const targetId = String(item.id).replace(/\D/g, '') || '201';
  if (item.type === 'product') {
    uni.navigateTo({ url: `/pages/goods/detail?id=${targetId}&type=product` });
    return;
  }
  uni.navigateTo({ url: `/pages/goods/detail?id=${targetId}&type=service` });
}

let timer = null;
watch(keyword, () => {
  clearTimeout(timer);
  timer = setTimeout(() => {
    loadSuggestions();
  }, 250);
});

onMounted(loadSuggestions);
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
  border-bottom: 1rpx solid #edf1f6;
}

.search-page__row:last-child {
  border-bottom: none;
}

.search-page__row-top {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12rpx;
}

.search-page__row-title {
  flex: 1;
  font-size: 28rpx;
  font-weight: 700;
}

.search-page__row-desc {
  margin-top: 8rpx;
  font-size: 22rpx;
  color: #8b90a0;
}

.chip--small {
  padding: 8rpx 16rpx;
  font-size: 20rpx;
}
</style>
