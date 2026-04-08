<template>
  <div class="login-page">
    <div class="login-card">
      <div class="login-card__eyebrow">Dispatch Console</div>
      <h1>呼之应运营后台</h1>
      <p>后台账号改为真实登录，菜单和权限随角色返回，不再使用静态演示会话。</p>

      <el-form ref="formRef" :model="form" :rules="rules" label-position="top" class="login-form">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" autocomplete="username" placeholder="请输入后台用户名" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input
            v-model="form.password"
            type="password"
            show-password
            autocomplete="current-password"
            placeholder="请输入后台密码"
            @keyup.enter="handleLogin"
          />
        </el-form-item>
        <div class="login-card__hint">默认种子账号：`admin / Admin@123456`</div>
        <el-button type="primary" size="large" :loading="loading" class="login-card__submit" @click="handleLogin">
          登录后台
        </el-button>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import { loginAsAdmin } from '../api/request';
import { useAdminUserStore } from '../stores/user';

const router = useRouter();
const store = useAdminUserStore();
const loading = ref(false);
const formRef = ref(null);
const form = ref({
  username: 'admin',
  password: 'Admin@123456',
});

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
};

async function handleLogin() {
  const valid = await formRef.value?.validate().catch(() => false);
  if (!valid) {
    return;
  }

  loading.value = true;
  try {
    const session = await loginAsAdmin(form.value);
    store.login(session);
    router.replace(store.firstAllowedPath);
  } catch (error) {
    ElMessage.error(error.message || '登录失败');
  } finally {
    loading.value = false;
  }
}
</script>

<style scoped>
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
  width: 460px;
  padding: 40px;
  border-radius: 32px;
  background: rgba(255, 255, 255, 0.92);
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

.login-form {
  margin-top: 8px;
}

.login-card__hint {
  margin: 4px 0 18px;
  color: #667085;
  font-size: 12px;
}

.login-card__submit {
  width: 100%;
}
</style>
