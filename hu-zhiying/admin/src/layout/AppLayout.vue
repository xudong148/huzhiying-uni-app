<template>
  <el-container class="layout">
    <el-aside width="240px" class="layout__aside">
      <div class="layout__brand">呼之应后台</div>
      <el-menu :default-active="route.path" router class="layout__menu">
        <el-menu-item v-for="item in store.menuItems" :key="item.id || item.path" :index="item.path">
          {{ item.name }}
        </el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header class="layout__header">
        <div>
          <div class="layout__title">{{ route.meta.title || currentMenuTitle }}</div>
          <div class="layout__subtitle">订单、配置、财务和风控统一工作台</div>
        </div>
        <div class="layout__actions">
          <div class="layout__user">
            <div class="layout__user-main">
              <div class="layout__user-name">{{ store.profile.name }}</div>
              <div v-if="store.profile.username" class="layout__user-code">@{{ store.profile.username }}</div>
            </div>
            <el-tag type="primary">{{ store.profile.roleName }}</el-tag>
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
import { computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useAdminUserStore } from '../stores/user';

const route = useRoute();
const router = useRouter();
const store = useAdminUserStore();

const currentMenuTitle = computed(() => {
  const current = store.menuItems.find((item) => item.path === route.path);
  return current?.name || '后台工作台';
});

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

.layout__user-main {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  gap: 2px;
}

.layout__user-name {
  font-size: 14px;
  font-weight: 600;
  color: #344054;
}

.layout__user-code {
  font-size: 12px;
  color: #667085;
}

.layout__main {
  padding: 24px;
}
</style>
