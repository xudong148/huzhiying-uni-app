import { useUserStore } from '../stores/user';

const BASE_URL = 'http://localhost:8080';

function redirectToLogin() {
  const pages = getCurrentPages();
  const current = pages[pages.length - 1];
  const redirect = current?.route ? `/${current.route}` : '/pages/index/index';
  uni.navigateTo({
    url: `/pages/auth/login?redirect=${encodeURIComponent(redirect)}`,
  });
}

export function request(options) {
  const userStore = useUserStore();

  return new Promise((resolve, reject) => {
    uni.request({
      ...options,
      url: `${BASE_URL}${options.url}`,
      header: {
        'Content-Type': 'application/json',
        ...(options.header || {}),
        Authorization: userStore.token ? `Bearer ${userStore.token}` : '',
      },
      success: async (res) => {
        if (res.statusCode === 401 && userStore.refreshToken) {
          try {
            const refreshRes = await uni.request({
              url: `${BASE_URL}/api/auth/refresh`,
              method: 'POST',
              data: {
                refreshToken: userStore.refreshToken,
              },
            });
            if (refreshRes.data?.data?.token) {
              userStore.login({
                token: refreshRes.data.data.token,
                refreshToken: refreshRes.data.data.refreshToken,
                role: userStore.role,
                profile: userStore.profile,
              });
              const retried = await request(options);
              resolve(retried);
              return;
            }
          } catch (error) {
            userStore.logout();
            redirectToLogin();
          }
        }

        if (res.statusCode >= 400) {
          reject(res);
          return;
        }

        resolve(res.data);
      },
      fail: reject,
    });
  });
}
