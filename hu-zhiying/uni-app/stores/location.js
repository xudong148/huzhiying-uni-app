import { defineStore } from 'pinia';
import { ref } from 'vue';
import { reverseGeocode } from '../api/map';

const DEFAULT_CITY = {
  city: '上海',
  district: '浦东新区',
  address: '上海市浦东新区张江高科技园区 88 号',
  latitude: 31.2253,
  longitude: 121.5443,
};

export const useLocationStore = defineStore('location', () => {
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
            const geoRes = await reverseGeocode({
              latitude: res.latitude,
              longitude: res.longitude,
            });
            next.city = geoRes.data?.city || DEFAULT_CITY.city;
            next.district = geoRes.data?.district || DEFAULT_CITY.district;
            next.address = geoRes.data?.address || DEFAULT_CITY.address;
          } catch (error) {
            console.log('地理反查失败', error);
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
    current,
    locate,
    setLocation,
  };
});
