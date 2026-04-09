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

    <view class="card profile-page__section profile-page__section--runtime">
      <view class="section-title">
        <text class="section-title__text">联调配置</text>
        <text class="section-title__desc">{{ runtimeInfo.needsLanIp ? '需改局域网 IP' : '当前地址可用' }}</text>
      </view>
      <view class="profile-page__row">
        <text>后端地址</text>
        <input v-model="runtime.baseUrl" placeholder="http://192.168.1.10:8080" />
      </view>
      <view
        class="profile-page__hint"
        :class="{ 'profile-page__hint--warning': runtimeInfo.needsLanIp }"
      >
        {{ runtimeInfo.hint }}
      </view>
      <view v-if="lastTraceId" class="profile-page__trace">
        最近一次追踪 ID：{{ lastTraceId }}
      </view>
      <button class="secondary-btn profile-page__runtime-btn" :loading="runtimeSubmitting" @tap="saveRuntime">
        保存联调地址
      </button>
    </view>
  </view>
</template>

<script setup>
/**
 * 个人资料页。
 * 1. 当前页只允许编辑昵称和手机号。
 * 2. 提交成功后同步更新本地用户状态。
 * 3. 支持在移动端运行时动态修改后端联调地址。
 */
import { computed, reactive, ref } from 'vue';
import { updateProfile } from '../../api/user';
import { useUserStore } from '../../stores/user';
import {
  getLastTraceId,
  getRequestErrorMessage,
  getRuntimeConfig,
  getRuntimeDiagnostics,
  setRuntimeConfig,
} from '../../utils/request';

const user = useUserStore();
const submitting = ref(false);
const runtimeSubmitting = ref(false);
const profile = reactive({
  ...user.profile,
});
const runtime = reactive({
  baseUrl: getRuntimeConfig().baseUrl,
});
const runtimeInfo = computed(() => getRuntimeDiagnostics(runtime.baseUrl));
const lastTraceId = computed(() => getLastTraceId());

async function save() {
  submitting.value = true;
  try {
    await updateProfile({
      nickname: profile.nickname,
      mobile: profile.mobile,
    });
    user.updateProfile(profile);
    uni.showToast({ title: '资料已更新', icon: 'none' });
  } catch (error) {
    uni.showToast({
      title: getRequestErrorMessage(error, '资料更新失败'),
      icon: 'none',
    });
  } finally {
    submitting.value = false;
  }
}

async function saveRuntime() {
  runtimeSubmitting.value = true;
  try {
    const next = setRuntimeConfig({
      baseUrl: runtime.baseUrl,
    });
    runtime.baseUrl = next.baseUrl;
    uni.showToast({ title: '联调地址已保存', icon: 'none' });
  } finally {
    runtimeSubmitting.value = false;
  }
}
</script>

<style scoped>
/* 表单区 */
.profile-page__section {
  padding: 28rpx;
}

.profile-page__section--runtime {
  margin-top: 22rpx;
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

.profile-page__hint {
  margin-top: 18rpx;
  font-size: 22rpx;
  line-height: 1.7;
  color: #667085;
}

.profile-page__hint--warning {
  color: #c2410c;
}

.profile-page__trace {
  margin-top: 12rpx;
  font-size: 22rpx;
  line-height: 1.6;
  color: #475467;
  word-break: break-all;
}

.profile-page__runtime-btn {
  margin-top: 18rpx;
}
</style>
