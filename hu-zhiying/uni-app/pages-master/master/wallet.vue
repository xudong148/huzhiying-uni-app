<template>
  <view class="page-shell">
    <view class="card master-wallet__hero">
      <view class="master-wallet__label">钱包中心</view>
      <view class="master-wallet__balance">¥{{ Number(wallet.balance || 0).toFixed(2) }}</view>
      <view class="master-wallet__desc">
        已完工并进入结算流程的金额会回到这里。当前版本先保证结算透明，端内暂不做自助提现。
      </view>
    </view>

    <view class="master-wallet__stats">
      <view class="card master-wallet__stat">
        <view class="master-wallet__stat-label">可结算余额</view>
        <view class="master-wallet__stat-value">¥{{ Number(wallet.balance || 0).toFixed(2) }}</view>
      </view>
      <view class="card master-wallet__stat">
        <view class="master-wallet__stat-label">冻结保证金</view>
        <view class="master-wallet__stat-value">¥{{ Number(wallet.frozen || 0).toFixed(2) }}</view>
      </view>
      <view class="card master-wallet__stat">
        <view class="master-wallet__stat-label">今日入账</view>
        <view class="master-wallet__stat-value">¥{{ Number(wallet.todayIncome || 0).toFixed(2) }}</view>
      </view>
    </view>

    <view class="card master-wallet__section">
      <view class="section-title">
        <text class="section-title__text">结算说明</text>
        <text class="section-title__desc">本季先把账看清楚</text>
      </view>
      <view class="master-wallet__tip">1. 用户支付先进入平台，工单完工并确认后再进入师傅结算。</view>
      <view class="master-wallet__tip">2. 冻结保证金用于售后和履约风险控制，不代表已扣除收入。</view>
      <view class="master-wallet__tip">3. 自助提现暂未开放，这一页先承担“账单透明”和“异常对账”能力。</view>
    </view>

    <view class="card master-wallet__section">
      <view class="section-title">
        <text class="section-title__text">流水明细</text>
        <text class="section-title__desc" @tap="safeLoadWallet">刷新</text>
      </view>
      <view v-if="wallet.transactions.length">
        <view v-for="item in wallet.transactions" :key="item.id" class="master-wallet__row">
          <view class="master-wallet__row-main">
            <view class="master-wallet__row-title">{{ item.title }}</view>
            <view class="master-wallet__row-time">{{ item.time || '待补充时间' }}</view>
          </view>
          <view
            class="master-wallet__row-amount"
            :class="{ 'master-wallet__row-amount--minus': Number(item.amount) < 0 }"
          >
            {{ Number(item.amount) > 0 ? '+' : '' }}{{ Number(item.amount).toFixed(2) }}
          </view>
        </view>
      </view>
      <view v-else class="master-wallet__empty">
        <view class="master-wallet__empty-title">暂时还没有钱包流水</view>
        <view class="master-wallet__empty-desc">抢到订单并完成履约后，这里会开始展示结算、冻结和逆向冲减记录。</view>
        <button class="secondary-btn master-wallet__empty-btn" @tap="goDispatch">去抢派单大厅</button>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref } from 'vue';
import { onShow } from '@dcloudio/uni-app';
import { getWalletData } from '../../api/master';
import { safeAsync } from '../../utils/page-task';

const wallet = ref({
  balance: 0,
  frozen: 0,
  todayIncome: 0,
  transactions: [],
});

async function loadWallet() {
  const res = await getWalletData();
  wallet.value = {
    balance: Number(res.data.balance || 0),
    frozen: Number(res.data.frozen || 0),
    todayIncome: Number(res.data.todayIncome || 0),
    transactions: res.data.transactions || [],
  };
}

const safeLoadWallet = safeAsync(loadWallet, '加载师傅钱包');

function goDispatch() {
  uni.navigateTo({ url: '/pages-master/master/dispatch' });
}

onShow(safeLoadWallet);
</script>

<style scoped>
.master-wallet__hero,
.master-wallet__section,
.master-wallet__stat {
  padding: 30rpx;
}

.master-wallet__hero {
  background: linear-gradient(135deg, #17338f 0%, #2b5cff 100%);
  color: #ffffff;
}

.master-wallet__label {
  font-size: 24rpx;
  opacity: 0.88;
}

.master-wallet__balance {
  margin-top: 12rpx;
  font-size: 64rpx;
  font-weight: 800;
}

.master-wallet__desc {
  margin-top: 12rpx;
  font-size: 22rpx;
  line-height: 1.7;
  color: rgba(255, 255, 255, 0.84);
}

.master-wallet__stats {
  display: grid;
  gap: 16rpx;
  margin-top: 20rpx;
}

.master-wallet__stat-label {
  font-size: 22rpx;
  color: #8b90a0;
}

.master-wallet__stat-value {
  margin-top: 12rpx;
  font-size: 38rpx;
  font-weight: 800;
  color: #1c1e26;
}

.master-wallet__section {
  margin-top: 20rpx;
}

.master-wallet__tip {
  margin-top: 14rpx;
  font-size: 24rpx;
  line-height: 1.7;
  color: #667085;
}

.master-wallet__row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16rpx;
  padding: 22rpx 0;
  border-bottom: 1rpx solid #edf1f6;
}

.master-wallet__row:last-child {
  border-bottom: none;
}

.master-wallet__row-main {
  flex: 1;
}

.master-wallet__row-title,
.master-wallet__empty-title {
  font-size: 28rpx;
  font-weight: 700;
  color: #1c1e26;
}

.master-wallet__row-time,
.master-wallet__empty-desc {
  margin-top: 8rpx;
  font-size: 22rpx;
  line-height: 1.6;
  color: #8b90a0;
}

.master-wallet__row-amount {
  color: #00b578;
  font-size: 30rpx;
  font-weight: 800;
}

.master-wallet__row-amount--minus {
  color: #ff4a4a;
}

.master-wallet__empty {
  text-align: center;
  padding-top: 12rpx;
}

.master-wallet__empty-btn {
  margin-top: 20rpx;
}
</style>
