<template>
  <div class="login-page">
    <div class="login-card">
      <!-- 登录卡片头部 -->
      <div class="login-card__eyebrow">Dispatch Console</div>
      <h1>呼之应运营后台</h1>
      <p>订单调度、履约监控、财务结算与仲裁处理统一工作台。</p>
      <el-button type="primary" size="large" :loading="loading" @click="handleLogin">进入后台</el-button>
    </div>
  </div>
</template>

<script setup>
/**
 * 后台登录页。
 * 当前通过真实短信登录接口获取后台会话。
 */
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import { loginAsAdmin } from '../api/request';
import { useAdminUserStore } from '../stores/user';

const router = useRouter();
const store = useAdminUserStore();
const loading = ref(false);

async function handleLogin() {
  loading.value = true;
  try {
    const session = await loginAsAdmin();
    store.login({
      ...session,
      profile: {
        name: '呼之应运营后台',
        role: '超级管理员',
      },
    });
    router.replace('/dashboard');
  } catch (error) {
    ElMessage.error(error.message || '登录失败');
  } finally {
    loading.value = false;
  }
}
</script>

<style scoped>
/* 登录页布局 */
.login-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background:
    radial-gradient(circle at top left, rgba(43, 92, 255, 0.18), transparent 36%),
    radial-gradient(circle at bottom right, rgba(255, 194, 102, 0.28), transparent 32%),
    linear-gradient(135deg, #edf2ff 0%, #f4f6f9 50%, #fff8e9 100%);
}

.login-card {
  width: 440px;
  padding: 40px;
  border-radius: 32px;
  background: rgba(255, 255, 255, 0.9);
  box-shadow: 0 24px 48px rgba(15, 23, 42, 0.08);
}

.login-card__eyebrow {
  display: inline-flex;
  align-items: center;
  height: 32px;
  padding: 0 14px;
  border-radius: 999px;
  background: rgba(43, 92, 255, 0.1);
  color: #2b5cff;
  font-size: 12px;
  font-weight: 700;
  letter-spacing: 0.08em;
  text-transform: uppercase;
}

.login-card h1 {
  margin: 18px 0 0;
  font-size: 36px;
}

.login-card p {
  margin: 14px 0 28px;
  color: #667085;
  line-height: 1.6;
}
</style>
