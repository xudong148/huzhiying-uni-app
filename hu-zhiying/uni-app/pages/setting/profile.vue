<template>
  <view class="page-shell">
    <!-- 资料编辑表单 -->
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
        <input v-model="profile.level" disabled />
      </view>
    </view>

    <button class="primary-btn profile-page__btn" :loading="submitting" @tap="save">保存资料</button>
  </view>
</template>

<script setup>
/**
 * 个人资料页。
 * 1. 当前页只允许编辑昵称和手机号。
 * 2. 提交成功后同步更新本地用户状态。
 */
import { reactive, ref } from 'vue';
import { updateProfile } from '../../api/user';
import { useUserStore } from '../../stores/user';

const user = useUserStore();
const submitting = ref(false);
const profile = reactive({
  ...user.profile,
});

async function save() {
  submitting.value = true;
  try {
    await updateProfile({
      nickname: profile.nickname,
      mobile: profile.mobile,
    });
    user.updateProfile(profile);
    uni.showToast({ title: '资料已更新', icon: 'none' });
  } finally {
    submitting.value = false;
  }
}
</script>

<style scoped>
/* 表单区 */
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
