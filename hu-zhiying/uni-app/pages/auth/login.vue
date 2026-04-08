<template>
  <view class="page-shell login-page">
    <!-- 欢迎信息 -->
    <view class="login-page__hero">
      <view class="login-page__headline">欢迎来到呼之应</view>
      <view class="login-page__subline">用户端与师傅端共用同一套登录体系，登录后按真实接口回填资料。</view>
    </view>

    <!-- 登录操作 -->
    <view class="card login-page__card">
      <button class="primary-btn" @tap="loginAs('user')">微信一键登录</button>
      <button class="secondary-btn login-page__btn" @tap="loginAs('user')">手机号验证码登录</button>
      <button class="secondary-btn login-page__btn" @tap="loginAs('master')">登录师傅端</button>
    </view>
  </view>
</template>

<script setup>
/**
 * 登录页
 * 1. 登录成功后立即拉取当前用户资料，避免前端拼示例身份。
 * 2. 资料缺失时使用通用空态，不再补造示例身份。
 */
import { getCurrentUser, loginWithRole } from '../../api/user';
import { useUserStore } from '../../stores/user';

const user = useUserStore();

function normalizeProfile(payload) {
  const profile = payload || {};
  return {
    id: profile.id ?? null,
    nickname: profile.nickname || '未命名账号',
    mobile: profile.mobile || '',
    avatar: profile.avatar || '/static/user.png',
    level: profile.level || '',
    role: profile.role || '',
  };
}

async function loginAs(role) {
  const authRes = await loginWithRole(role);
  const profileRes = await getCurrentUser();

  user.login({
    token: authRes.data.token,
    refreshToken: authRes.data.refreshToken,
    role,
    profile: normalizeProfile(profileRes.data?.profile),
  });

  uni.showToast({ title: '登录成功', icon: 'none' });
  setTimeout(() => {
    uni.switchTab({ url: '/pages/user/index' });
  }, 300);
}
</script>

<style scoped>
/* 登录页背景与结构 */
.login-page {
  min-height: 100vh;
  background:
    radial-gradient(circle at top right, rgba(255, 224, 163, 0.25), transparent 28%),
    linear-gradient(180deg, #edf2ff 0%, #f4f6f9 35%, #f4f6f9 100%);
}

.login-page__hero {
  padding-top: 80rpx;
}

.login-page__headline {
  font-size: 48rpx;
  font-weight: 800;
}

.login-page__subline {
  margin-top: 14rpx;
  font-size: 24rpx;
  line-height: 1.7;
  color: #667085;
}

.login-page__card {
  margin-top: 40rpx;
  padding: 34rpx;
}

.login-page__btn {
  margin-top: 18rpx;
}
</style>
