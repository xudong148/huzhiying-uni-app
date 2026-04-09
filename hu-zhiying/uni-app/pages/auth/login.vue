<template>
  <view class="page-shell login-page">
    <view class="login-page__hero">
      <view class="login-page__headline">欢迎来到呼之应</view>
      <view class="login-page__subline">
        用户端和师傅端共用一套登录体系。登录后会同步订单、消息、地址和身份资料，避免跨端信息不一致。
      </view>
    </view>

    <view class="card login-page__card">
      <view class="login-page__tips">
        <view class="login-page__tip">1. 用户端适合下单、支付、售后、查看消息。</view>
        <view class="login-page__tip">2. 师傅端适合抢单、履约、上传凭证、查看钱包。</view>
      </view>

      <button class="primary-btn" :loading="loadingRole === 'user'" @tap="loginAs('user')">微信一键登录</button>
      <button class="secondary-btn login-page__btn" :loading="loadingRole === 'user'" @tap="loginAs('user')">手机号验证码登录</button>
      <button class="secondary-btn login-page__btn" :loading="loadingRole === 'master'" @tap="loginAs('master')">登录师傅端</button>
    </view>
  </view>
</template>

<script setup>
import { ref } from 'vue';
import { onLoad } from '@dcloudio/uni-app';
import { getCurrentUser, loginWithRole } from '../../api/user';
import { useUserStore } from '../../stores/user';
import { showActionError } from '../../utils/page-task';

const user = useUserStore();
const redirectPath = ref('/pages/user/index');
const loadingRole = ref('');

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

function finishLogin() {
  const target = decodeURIComponent(redirectPath.value || '/pages/user/index');
  const tabPages = new Set([
    '/pages/index/index',
    '/pages/category/index',
    '/pages/order/list',
    '/pages/message/index',
    '/pages/user/index',
  ]);

  if (tabPages.has(target)) {
    uni.switchTab({ url: target });
    return;
  }

  uni.redirectTo({ url: target });
}

async function loginAs(role) {
  loadingRole.value = role;
  try {
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
      finishLogin();
    }, 250);
  } catch (error) {
    showActionError(error, '登录失败');
  } finally {
    loadingRole.value = '';
  }
}

onLoad((options) => {
  redirectPath.value = options?.redirect || '/pages/user/index';
});
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

.login-page__tips {
  margin-bottom: 24rpx;
}

.login-page__tip {
  font-size: 24rpx;
  line-height: 1.7;
  color: #475467;
}

.login-page__tip + .login-page__tip {
  margin-top: 8rpx;
}

.login-page__btn {
  margin-top: 18rpx;
}
</style>
