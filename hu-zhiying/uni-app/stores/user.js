import { defineStore } from 'pinia';
import { computed, ref } from 'vue';

const STORAGE_KEY = 'hzy-user-store';

function loadUserState() {
  try {
    return uni.getStorageSync(STORAGE_KEY) || {};
  } catch (error) {
    return {};
  }
}

export const useUserStore = defineStore('user', () => {
  const initial = loadUserState();
  const token = ref(initial.token || '');
  const refreshToken = ref(initial.refreshToken || '');
  const role = ref(initial.role || 'user');
  const profile = ref(initial.profile || {
    id: 10001,
    nickname: '周女士',
    mobile: '138****5288',
    avatar: '/static/user.png',
    level: 'SVIP预备用户',
  });

  const isLoggedIn = computed(() => Boolean(token.value));

  function persist() {
    uni.setStorageSync(STORAGE_KEY, {
      token: token.value,
      refreshToken: refreshToken.value,
      role: role.value,
      profile: profile.value,
    });
  }

  function login(payload) {
    token.value = payload.token;
    refreshToken.value = payload.refreshToken;
    role.value = payload.role || 'user';
    profile.value = payload.profile || profile.value;
    persist();
  }

  function updateProfile(payload) {
    profile.value = {
      ...profile.value,
      ...payload,
    };
    persist();
  }

  function switchRole(nextRole) {
    role.value = nextRole;
    persist();
  }

  function logout() {
    token.value = '';
    refreshToken.value = '';
    role.value = 'user';
    persist();
  }

  return {
    token,
    refreshToken,
    role,
    profile,
    isLoggedIn,
    login,
    updateProfile,
    switchRole,
    logout,
  };
});
