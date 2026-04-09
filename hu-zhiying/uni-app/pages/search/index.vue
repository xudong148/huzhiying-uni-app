<template>
  <view class="page-shell">
    <view class="card search-page__box">
      <view class="search-page__input-row">
        <input
          v-model="keyword"
          class="search-page__input"
          placeholder="搜索故障、服务或商品"
          confirm-type="search"
          @confirm="triggerSearch"
        />
        <text v-if="keyword" class="search-page__clear" @tap="clearKeyword">清空</text>
      </view>
    </view>

    <view class="section-title search-page__section">
      <text class="section-title__text">热门搜索</text>
      <text class="section-title__desc">优先保障高频需求的命中率</text>
    </view>
    <view class="search-page__chips">
      <view v-for="item in keywords" :key="item" class="chip" @tap="useKeyword(item)">{{ item }}</view>
    </view>

    <view class="section-title search-page__section">
      <text class="section-title__text">{{ keyword ? '搜索结果' : '智能联想' }}</text>
      <text class="section-title__desc">{{ statusText }}</text>
    </view>

    <view class="card search-page__suggestion">
      <view v-if="loading" class="search-page__state">正在搜索，请稍候…</view>

      <view v-else-if="!suggestionList.length" class="search-page__state search-page__state--empty">
        <text class="search-page__state-title">{{ keyword ? '没有找到完全匹配的结果' : '先输入关键词试试' }}</text>
        <text class="search-page__state-desc">
          {{ keyword ? '可以换个说法，比如“空调不制冷”“洗衣机漏水”“灯具安装”。' : '支持搜索服务、商品、常见故障和热门场景。' }}
        </text>
      </view>

      <view
        v-for="item in suggestionList"
        v-else
        :key="`${item.type}-${item.id}`"
        class="search-page__row"
        @tap="handleSuggestion(item)"
      >
        <view class="search-page__row-top">
          <view class="search-page__row-title">{{ item.title }}</view>
          <text class="chip chip--small">{{ item.type === 'product' ? '商品' : '服务' }}</text>
        </view>
        <view class="search-page__row-desc">{{ item.summary || '点击查看详情与下单入口' }}</view>
      </view>
    </view>
  </view>
</template>

<script setup>
import { computed, onMounted, onUnmounted, ref, watch } from 'vue';
import { searchCatalog } from '../../api/service';
import { safeAsync } from '../../utils/page-task';

const keyword = ref('');
const keywords = ['空调不制冷', '智能锁安装', '厨房漏水', '深度保洁', '油烟机清洗'];
const suggestionList = ref([]);
const loading = ref(false);

let timer = null;
let requestSeq = 0;

const statusText = computed(() => {
  if (loading.value) {
    return '正在同步最新服务和商品';
  }
  if (keyword.value.trim()) {
    return `“${keyword.value.trim()}”的匹配结果`;
  }
  return '根据热词和输入实时推荐';
});

async function loadSuggestions(nextKeyword = keyword.value.trim()) {
  const currentSeq = ++requestSeq;
  loading.value = true;
  try {
    const res = await searchCatalog(nextKeyword);
    if (currentSeq !== requestSeq) {
      return;
    }
    suggestionList.value = res.data || [];
  } finally {
    if (currentSeq === requestSeq) {
      loading.value = false;
    }
  }
}

const safeLoadSuggestions = safeAsync(loadSuggestions, '加载搜索结果');

function triggerSearch() {
  safeLoadSuggestions(keyword.value.trim());
}

function useKeyword(value) {
  keyword.value = value;
  safeLoadSuggestions(value);
}

function clearKeyword() {
  keyword.value = '';
  suggestionList.value = [];
  safeLoadSuggestions('');
}

function handleSuggestion(item) {
  const targetId = String(item.id).replace(/\D/g, '') || '201';
  if (item.type === 'product') {
    uni.navigateTo({ url: `/pages/goods/detail?id=${targetId}&type=product` });
    return;
  }
  uni.navigateTo({ url: `/pages/goods/detail?id=${targetId}&type=service` });
}

watch(keyword, (value) => {
  clearTimeout(timer);
  timer = setTimeout(() => {
    safeLoadSuggestions(value.trim());
  }, value.trim() ? 250 : 120);
});

onMounted(() => {
  safeLoadSuggestions('');
});

onUnmounted(() => {
  clearTimeout(timer);
});
</script>

<style scoped>
.search-page__box {
  padding: 18rpx 26rpx;
}

.search-page__input-row {
  display: flex;
  align-items: center;
  gap: 16rpx;
}

.search-page__input {
  flex: 1;
  height: 68rpx;
  font-size: 28rpx;
}

.search-page__clear {
  font-size: 24rpx;
  color: #2b5cff;
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

.search-page__state {
  padding: 40rpx 0;
  text-align: center;
  color: #667085;
  font-size: 24rpx;
}

.search-page__state--empty {
  display: flex;
  flex-direction: column;
  gap: 10rpx;
}

.search-page__state-title {
  color: #1f2937;
  font-size: 28rpx;
  font-weight: 700;
}

.search-page__state-desc {
  line-height: 1.7;
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
  line-height: 1.6;
}

.chip--small {
  padding: 8rpx 16rpx;
  font-size: 20rpx;
}
</style>
