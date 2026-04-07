<template>
  <scroll-view scroll-y class="page-container" :show-scrollbar="false">
    <view class="page-shell">
      <view class="user-page__profile">
        <view class="user-page__profile-main">
          <image class="user-page__avatar" :src="user.profile.avatar" mode="aspectFill" />
          <view>
            <view class="user-page__name">{{ user.profile.nickname }}</view>
            <view class="user-page__desc">{{ user.profile.level }}</view>
          </view>
        </view>
        <view class="user-page__actions">
          <view class="chip" @tap="toggleRole">{{ roleLabel }}</view>
          <view class="chip" @tap="goProfile">编辑资料</view>
        </view>
      </view>

      <view class="card user-page__section">
        <view class="section-title">
          <text class="section-title__text">订单面板</text>
        </view>
        <view class="user-page__grid">
          <view class="user-page__entry pressable" @tap="goOrderList">
            <image class="user-page__entry-icon" src="/static/icons/order.svg" mode="aspectFit" />
            <text>全部订单</text>
          </view>
          <view class="user-page__entry pressable" @tap="goCoupons">
            <image class="user-page__entry-icon" src="/static/icons/coupon.svg" mode="aspectFit" />
            <text>优惠券</text>
          </view>
          <view class="user-page__entry pressable" @tap="goAddressList">
            <image class="user-page__entry-icon" src="/static/icons/location.svg" mode="aspectFit" />
            <text>地址簿</text>
          </view>
          <view class="user-page__entry pressable" @tap="goApplyMaster">
            <image class="user-page__entry-icon" src="/static/icons/master.svg" mode="aspectFit" />
            <text>师傅入驻</text>
          </view>
        </view>
      </view>

      <view v-if="user.role === 'master'" class="card user-page__section">
        <view class="section-title">
          <text class="section-title__text">师傅工作台</text>
          <text class="section-title__desc">弱网离线打卡已启用</text>
        </view>
        <view class="user-page__master-links">
          <view class="chip" @tap="goMaster('/pages-master/master/dispatch')">抢派单大厅</view>
          <view class="chip" @tap="goMaster('/pages-master/master/workbench')">履约工作台</view>
          <view class="chip" @tap="goMaster('/pages-master/master/wallet')">钱包</view>
          <view class="chip" @tap="goMaster('/pages-master/master/setting')">接单设置</view>
        </view>
      </view>

      <view class="safe-bottom"></view>
    </view>
  </scroll-view>
</template>

<script setup>
import { computed } from 'vue';
import { useUserStore } from '../../stores/user';

const user = useUserStore();

const roleLabel = computed(() => (user.role === 'master' ? '切到用户端' : '切到师傅端'));

function toggleRole() {
  user.switchRole(user.role === 'master' ? 'user' : 'master');
}

function goProfile() {
  uni.navigateTo({ url: '/pages/setting/profile' });
}

function goOrderList() {
  uni.switchTab({ url: '/pages/order/list' });
}

function goCoupons() {
  uni.navigateTo({ url: '/pages/marketing/coupon' });
}

function goAddressList() {
  uni.navigateTo({ url: '/pages/setting/address-list' });
}

function goApplyMaster() {
  uni.navigateTo({ url: '/pages/apply/master' });
}

function goMaster(url) {
  uni.navigateTo({ url });
}
</script>

<style scoped>
.user-page__profile {
  padding: 30rpx;
  border-radius: 32rpx;
  background: linear-gradient(135deg, #1c1e26 0%, #36405a 100%);
  color: #ffffff;
}

.user-page__profile-main {
  display: flex;
  align-items: center;
  gap: 20rpx;
}

.user-page__avatar {
  width: 108rpx;
  height: 108rpx;
  border-radius: 50%;
}

.user-page__name {
  font-size: 34rpx;
  font-weight: 800;
}

.user-page__desc {
  margin-top: 8rpx;
  font-size: 24rpx;
  color: rgba(255, 255, 255, 0.8);
}

.user-page__actions {
  display: flex;
  gap: 12rpx;
  margin-top: 22rpx;
}

.user-page__section {
  margin-top: 20rpx;
  padding: 28rpx;
}

.user-page__grid {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 16rpx;
}

.user-page__entry {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12rpx;
  padding: 18rpx 10rpx;
  border-radius: 22rpx;
  background: #f4f6f9;
  font-size: 22rpx;
  font-weight: 700;
}

.user-page__entry-icon {
  width: 48rpx;
  height: 48rpx;
}

.user-page__master-links {
  display: flex;
  flex-wrap: wrap;
  gap: 12rpx;
}
</style>
