<template>
  <el-container class="layout">
    <el-aside width="240px" class="layout__aside">
      <div class="layout__brand">呼之应后台</div>
      <el-menu :default-active="route.path" router class="layout__menu">
        <el-menu-item index="/dashboard">仪表盘</el-menu-item>
        <el-menu-item index="/dispatch">订单调度中心</el-menu-item>
        <el-menu-item index="/orders">订单管理</el-menu-item>
        <el-menu-item index="/masters">师傅管理</el-menu-item>
        <el-menu-item index="/pricing">定价与类目</el-menu-item>
        <el-menu-item index="/finance">财务结算</el-menu-item>
        <el-menu-item index="/arbitration">仲裁中心</el-menu-item>
        <el-menu-item index="/marketing">优惠券与会员</el-menu-item>
        <el-menu-item index="/content">内容运营</el-menu-item>
        <el-menu-item index="/system">系统权限</el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header class="layout__header">
        <div>
          <div class="layout__title">{{ route.meta.title }}</div>
          <div class="layout__subtitle">统一处理调度、履约、财务、仲裁与运营配置</div>
        </div>
        <div class="layout__actions">
          <div class="layout__user">
            <div class="layout__user-name">{{ store.profile.name }}</div>
            <el-tag type="primary">{{ store.profile.role }}</el-tag>
          </div>
          <el-button plain @click="handleLogout">退出登录</el-button>
        </div>
      </el-header>
      <el-main class="layout__main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { useRoute, useRouter } from 'vue-router';
import { useAdminUserStore } from '../stores/user';

const route = useRoute();
const router = useRouter();
const store = useAdminUserStore();

function handleLogout() {
  store.logout();
  router.replace('/login');
}
</script>

<style scoped>
.layout {
  min-height: 100vh;
}

.layout__aside {
  background: linear-gradient(180deg, #1c1e26 0%, #28324a 100%);
  color: #ffffff;
}

.layout__brand {
  padding: 28px 24px;
  font-size: 24px;
  font-weight: 800;
}

.layout__menu {
  border-right: none;
  background: transparent;
}

.layout__menu :deep(.el-menu-item) {
  color: rgba(255, 255, 255, 0.82);
}

.layout__menu :deep(.el-menu-item.is-active) {
  background: rgba(255, 255, 255, 0.1);
  color: #ffffff;
}

.layout__header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 24px;
  height: 88px;
  padding: 0 32px;
  background: rgba(255, 255, 255, 0.72);
  backdrop-filter: blur(18px);
}

.layout__title {
  font-size: 28px;
  font-weight: 800;
}

.layout__subtitle {
  margin-top: 6px;
  font-size: 14px;
  color: #667085;
}

.layout__actions {
  display: flex;
  align-items: center;
  gap: 16px;
}

.layout__user {
  display: flex;
  align-items: center;
  gap: 12px;
}

.layout__user-name {
  font-size: 14px;
  font-weight: 600;
  color: #344054;
}

.layout__main {
  padding: 24px;
}
</style>
