import { defineStore } from 'pinia';
import { computed, ref } from 'vue';

export const useAppStore = defineStore('app', () => {
  const unreadCount = ref(3);
  const noticeList = ref([
    {
      id: 1,
      title: '夜间服务费 22:00 后自动加收 30%',
      level: 'warning',
    },
    {
      id: 2,
      title: '新用户领取 80 元组合券包',
      level: 'promo',
    },
  ]);

  const bannerList = ref([
    {
      id: 1,
      title: '家电维修专场',
      subtitle: '不修不收，90 天质保',
      image: 'https://picsum.photos/960/520?random=31',
    },
    {
      id: 2,
      title: '智能锁安装季',
      subtitle: '含调试与新用户上门券',
      image: 'https://picsum.photos/960/520?random=32',
    },
  ]);

  const topNotice = computed(() => noticeList.value[0] || null);

  return {
    unreadCount,
    noticeList,
    bannerList,
    topNotice,
  };
});
