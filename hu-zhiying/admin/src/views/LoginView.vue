<template>
  <div class="login-page">
    <div class="login-card">
      <h1>呼之应后台</h1>
      <p>订单调度、履约监控、财务与仲裁统一工作台</p>
      <el-button type="primary" size="large" @click="handleLogin">进入后台</el-button>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router';
import { useAdminUserStore } from '../stores/user';

const router = useRouter();
const store = useAdminUserStore();

function handleLogin() {
  store.login();
  router.push('/dashboard');
}
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #edf2ff 0%, #f4f6f9 50%, #ffeccd 100%);
}

.login-card {
  width: 420px;
  padding: 40px;
  border-radius: 32px;
  background: rgba(255, 255, 255, 0.88);
  box-shadow: 0 24px 48px rgba(15, 23, 42, 0.08);
}

.login-card h1 {
  margin: 0;
  font-size: 36px;
}

.login-card p {
  margin: 14px 0 28px;
  color: #667085;
  line-height: 1.6;
}
</style>
