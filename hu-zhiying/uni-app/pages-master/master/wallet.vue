<template>
  <view class="page-shell">
    <view class="card master-wallet__hero">
      <view class="master-wallet__label">可提现余额</view>
      <view class="master-wallet__balance">¥{{ wallet.balance }}</view>
      <view class="master-wallet__meta">冻结保证金 ¥{{ wallet.frozen }} · 今日收入 ¥{{ wallet.todayIncome }}</view>
    </view>

    <view class="card master-wallet__section">
      <view class="section-title">
        <text class="section-title__text">流水明细</text>
      </view>
      <view v-for="item in wallet.transactions" :key="item.id" class="master-wallet__row">
        <view>
          <view class="master-wallet__row-title">{{ item.title }}</view>
          <view class="muted">{{ item.time }}</view>
        </view>
        <view class="master-wallet__row-amount" :class="{ 'master-wallet__row-amount--minus': item.amount < 0 }">
          {{ item.amount > 0 ? '+' : '' }}{{ item.amount }}
        </view>
      </view>
    </view>
  </view>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import { getWalletData } from '../../api/master';

const wallet = ref({
  transactions: [],
});

onMounted(async () => {
  const res = await getWalletData();
  wallet.value = res.data;
});
</script>

<style scoped>
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
