<template>
  <view class="page-shell login-page">
    <view class="login-page__hero">
      <view class="login-page__headline">欢迎来到呼之应</view>
      <view class="login-page__subline">微信授权、验证码登录和师傅端切换共用同一套鉴权体系。</view>
    </view>

    <view class="card login-page__card">
      <button class="primary-btn" @tap="loginAs('user')">微信一键登录</button>
      <button class="secondary-btn login-page__btn" @tap="loginAs('user')">手机号验证码登录</button>
      <button class="secondary-btn login-page__btn" @tap="loginAs('master')">演示切换到师傅端</button>
    </view>
  </view>
</template>

<script setup>
import { getCurrentUser, loginWithRole } from '../../api/user';
import { useUserStore } from '../../stores/user';

const user = useUserStore();

async function loginAs(role) {
  const authRes = await loginWithRole(role);
  const profileRes = await getCurrentUser();
  const profile = {
    ...(profileRes.data?.profile || user.profile),
    nickname: role === 'master' ? '张师傅' : (profileRes.data?.profile?.nickname || '周女士'),
    level: role === 'master' ? '认证工程师' : (profileRes.data?.profile?.level || 'SVIP 预备用户'),
  };

  user.login({
    token: authRes.data.token,
    refreshToken: authRes.data.refreshToken,
    role,
    profile,
  });

  uni.showToast({ title: '登录成功', icon: 'none' });
  setTimeout(() => {
    uni.switchTab({ url: '/pages/user/index' });
  }, 300);
}
</script>

<style scoped>
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
