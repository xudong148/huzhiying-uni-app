import { defineStore } from 'pinia';
import { computed, ref } from 'vue';
import { clearAdminSession, getAdminSession, saveAdminSession } from '../api/request';

function defaultProfile(role = 'admin') {
  return {
    name: '呼之应运营后台',
    role: role === 'admin' ? '超级管理员' : '运营人员',
  };
}

export const useAdminUserStore = defineStore('admin-user', () => {
  const initial = getAdminSession();
  const token = ref(initial.token || '');
  const refreshToken = ref(initial.refreshToken || '');
  const profile = ref(initial.profile || defaultProfile());

  const isLoggedIn = computed(() => Boolean(token.value));

  function persist() {
    saveAdminSession({
      token: token.value,
      refreshToken: refreshToken.value,
      profile: profile.value,
    });
  }

  function login(payload) {
    token.value = payload.token;
    refreshToken.value = payload.refreshToken;
    profile.value = payload.profile || defaultProfile(payload.role);
    persist();
  }

  function setProfile(payload) {
    profile.value = {
      ...profile.value,
      ...payload,
    };
    persist();
  }

  function logout() {
    token.value = '';
    refreshToken.value = '';
    profile.value = defaultProfile();
    clearAdminSession();
  }

  return {
    token,
    refreshToken,
    profile,
    isLoggedIn,
    login,
    setProfile,
    logout,
  };
});
