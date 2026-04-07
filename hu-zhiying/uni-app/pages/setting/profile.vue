<template>
  <view class="page-shell">
    <view class="card profile-page__section">
      <view class="profile-page__row">
        <text>昵称</text>
        <input v-model="profile.nickname" />
      </view>
      <view class="profile-page__row">
        <text>手机号</text>
        <input v-model="profile.mobile" />
      </view>
      <view class="profile-page__row">
        <text>会员等级</text>
        <input v-model="profile.level" />
      </view>
    </view>
    <button class="primary-btn profile-page__btn" @tap="save">保存</button>
  </view>
</template>

<script setup>
import { reactive } from 'vue';
import { useUserStore } from '../../stores/user';

const user = useUserStore();
const profile = reactive({
  ...user.profile,
});

function save() {
  user.updateProfile(profile);
  uni.showToast({ title: '资料已更新', icon: 'none' });
}
</script>

<style scoped>
.profile-page__section {
  padding: 28rpx;
}

.profile-page__row {
  display: flex;
  align-items: center;
  gap: 20rpx;
  padding: 24rpx 0;
  border-bottom: 1rpx solid #edf1f6;
}

.profile-page__row text {
  width: 140rpx;
  font-size: 26rpx;
}

.profile-page__row input {
  flex: 1;
  height: 56rpx;
  font-size: 26rpx;
}

.profile-page__btn {
  margin-top: 22rpx;
}
</style>
