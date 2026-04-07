import { defineStore } from 'pinia';
import { ref } from 'vue';

const DEFAULT_CITY = {
  city: '上海',
  district: '浦东新区',
  address: '张江高科园区 88 号',
  latitude: 31.2253,
  longitude: 121.5443,
};

export const useLocationStore = defineStore('location', () => {
  const amapKey = 'b7b0f19106ac788dd2d001ce141dfffe';
  const current = ref({
    ...DEFAULT_CITY,
  });

  async function locate() {
    return new Promise((resolve) => {
      uni.getLocation({
        type: 'gcj02',
        success: async (res) => {
          const next = {
            latitude: res.latitude,
            longitude: res.longitude,
            city: current.value.city,
            district: current.value.district,
            address: current.value.address,
          };

          try {
            const geoRes = await uni.request({
              url: 'https://restapi.amap.com/v3/geocode/regeo',
              data: {
                key: amapKey,
                location: `${res.longitude},${res.latitude}`,
                extensions: 'base',
              },
            });
            const component = geoRes.data?.regeocode?.addressComponent || {};
            next.city = component.city || component.province || DEFAULT_CITY.city;
            next.district = component.district || DEFAULT_CITY.district;
            next.address = geoRes.data?.regeocode?.formatted_address || DEFAULT_CITY.address;
          } catch (error) {
            console.log('地图反查失败', error);
          }

          current.value = next;
          resolve(current.value);
        },
        fail: () => {
          resolve(current.value);
        },
      });
    });
  }

  function setLocation(payload) {
    current.value = {
      ...current.value,
      ...payload,
    };
  }

  return {
    amapKey,
    current,
    locate,
    setLocation,
  };
});
