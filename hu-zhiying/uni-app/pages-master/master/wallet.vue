<template>
  <view class="page-shell">
    <!-- 钱包概览 -->
    <view class="card master-wallet__hero">
      <view class="master-wallet__label">可提现余额</view>
      <view class="master-wallet__balance">¥{{ Number(wallet.balance || 0).toFixed(2) }}</view>
      <view class="master-wallet__meta">
        冻结保证金 ¥{{ Number(wallet.frozen || 0).toFixed(2) }} · 今日收入 ¥{{ Number(wallet.todayIncome || 0).toFixed(2) }}
      </view>
    </view>

    <!-- 流水列表 -->
    <view class="card master-wallet__section">
      <view class="section-title">
        <text class="section-title__text">流水明细</text>
      </view>
      <view v-for="item in wallet.transactions" :key="item.id" class="master-wallet__row">
        <view>
          <view class="master-wallet__row-title">{{ item.title }}</view>
          <view class="muted">{{ item.time }}</view>
        </view>
        <view class="master-wallet__row-amount" :class="{ 'master-wallet__row-amount--minus': Number(item.amount) < 0 }">
          {{ Number(item.amount) > 0 ? '+' : '' }}{{ Number(item.amount).toFixed(2) }}
        </view>
      </view>
    </view>
  </view>
</template>

<script setup>
/**
 * 师傅钱包页面。
 * 1. 展示可提现余额、冻结金额与今日收入。
 * 2. 流水列表直接读取真实钱包接口。
 */
import { ref } from 'vue';
import { onShow } from '@dcloudio/uni-app';
import { getWalletData } from '../../api/master';

const wallet = ref({
  transactions: [],
});

onShow(async () => {
  const res = await getWalletData();
  wallet.value = res.data;
});
</script>

<style scoped>
/* 钱包头图与流水区块 */
.master-wallet__hero,
.master-wallet__section {
  padding: 30rpx;
}

.master-wallet__hero {
  background: linear-gradient(135deg, #2b5cff 0%, #143bc9 100%);
  color: #ffffff;
}

.master-wallet__label {
  font-size: 24rpx;
  opacity: 0.88;
}

.master-wallet__balance {
  margin-top: 14rpx;
  font-size: 64rpx;
  font-weight: 800;
}

.master-wallet__meta {
  margin-top: 12rpx;
  font-size: 22rpx;
  opacity: 0.84;
}

.master-wallet__section {
  margin-top: 20rpx;
}

.master-wallet__row {
  display: flex;
  justify-content: space-between;
  gap: 16rpx;
  padding: 20rpx 0;
  border-bottom: 1rpx solid #edf1f6;
}

.master-wallet__row-title {
  font-size: 26rpx;
  font-weight: 700;
}

.master-wallet__row-amount {
  color: #00b578;
  font-size: 30rpx;
  font-weight: 800;
}

.master-wallet__row-amount--minus {
  color: #ff4a4a;
}
</style>
