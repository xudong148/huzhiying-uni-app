import { defineStore } from 'pinia';
import { computed, ref } from 'vue';
import { clearAdminSession, getAdminSession, saveAdminSession } from '../api/request';

function defaultProfile() {
  return {
    id: null,
    username: '',
    name: '后台账号',
    mobile: '',
    roleCode: '',
    roleName: '未登录',
  };
}

export const useAdminUserStore = defineStore('admin-user', () => {
  const initial = getAdminSession();
  const token = ref(initial.token || '');
  const refreshToken = ref(initial.refreshToken || '');
  const accessExpiresAt = ref(initial.accessExpiresAt || '');
  const refreshExpiresAt = ref(initial.refreshExpiresAt || '');
  const profile = ref(initial.profile || defaultProfile());
  const menus = ref(Array.isArray(initial.menus) ? initial.menus : []);
  const permissions = ref(Array.isArray(initial.permissions) ? initial.permissions : []);

  const isLoggedIn = computed(() => Boolean(token.value));
  const menuItems = computed(() => menus.value || []);
  const allowedPaths = computed(() => menuItems.value.map((item) => item.path));
  const firstAllowedPath = computed(() => allowedPaths.value[0] || '/dashboard');

  function persist() {
    saveAdminSession({
      token: token.value,
      refreshToken: refreshToken.value,
      accessExpiresAt: accessExpiresAt.value,
      refreshExpiresAt: refreshExpiresAt.value,
      profile: profile.value,
      menus: menus.value,
      permissions: permissions.value,
    });
  }

  function applySession(payload = {}) {
    token.value = payload.token || '';
    refreshToken.value = payload.refreshToken || '';
    accessExpiresAt.value = payload.accessExpiresAt || '';
    refreshExpiresAt.value = payload.refreshExpiresAt || '';
    profile.value = payload.profile || defaultProfile();
    menus.value = Array.isArray(payload.menus) ? payload.menus : [];
    permissions.value = Array.isArray(payload.permissions) ? payload.permissions : [];
    persist();
  }

  function login(payload) {
    applySession(payload);
  }

  function setProfile(payload) {
    profile.value = {
      ...profile.value,
      ...payload,
    };
    persist();
  }

  function setGrants(payload) {
    menus.value = Array.isArray(payload.menus) ? payload.menus : menus.value;
    permissions.value = Array.isArray(payload.permissions) ? payload.permissions : permissions.value;
    persist();
  }

  function can(permissionCode) {
    return permissions.value.includes(permissionCode);
  }

  function logout() {
    token.value = '';
    refreshToken.value = '';
    accessExpiresAt.value = '';
    refreshExpiresAt.value = '';
    profile.value = defaultProfile();
    menus.value = [];
    permissions.value = [];
    clearAdminSession();
  }

  return {
    token,
    refreshToken,
    accessExpiresAt,
    refreshExpiresAt,
    profile,
    menus,
    permissions,
    isLoggedIn,
    menuItems,
    allowedPaths,
    firstAllowedPath,
    login,
    applySession,
    setProfile,
    setGrants,
    can,
    logout,
  };
});
