import { defineStore } from 'pinia';
import { ref } from 'vue';

export const useAdminUserStore = defineStore('admin-user', () => {
  const token = ref('admin-token');
  const profile = ref({
    name: '运营后台',
    role: '超级管理员',
  });

  function login() {
    token.value = `admin-token-${Date.now()}`;
  }

  function logout() {
    token.value = '';
  }

  return {
    token,
    profile,
    login,
    logout,
  };
});
