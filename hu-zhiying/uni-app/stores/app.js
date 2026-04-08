import { defineStore } from 'pinia';
import { computed, ref } from 'vue';

export const useAppStore = defineStore('app', () => {
  const unreadCount = ref(3);
  const noticeList = ref([
    {
      id: 1,
      title: '夜间服务费：22:00 后自动加收 30%',
      level: 'warning',
    },
    {
      id: 2,
      title: '新用户组合券已到账',
      level: 'promo',
    },
  ]);

  const bannerList = ref([
    {
      id: 1,
      title: '家电维修专场',
      subtitle: '不修不收费，30 天质保',
      image: '/static/home.png',
    },
    {
      id: 2,
      title: '智能锁安装季',
      subtitle: '含调试与新用户上门券',
      image: '/static/goods.png',
    },
  ]);

  const topNotice = computed(() => noticeList.value[0] || null);

  function setAppContent(payload = {}) {
    if (Array.isArray(payload.notices) && payload.notices.length) {
      noticeList.value = payload.notices;
    }
    if (Array.isArray(payload.banners) && payload.banners.length) {
      bannerList.value = payload.banners;
    }
  }

  return {
    unreadCount,
    noticeList,
    bannerList,
    topNotice,
    setAppContent,
  };
});
