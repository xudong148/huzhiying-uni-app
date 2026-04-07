import { defineStore } from 'pinia';
import { ref } from 'vue';

export const useMapStore = defineStore('useMapStore', () => {
  const amapKey = 'b7b0f19106ac788dd2d001ce141dfffe'; // 你的高德Key
  
  const mapInfo = ref({
    name: '定位中...',
    district: '', 
    latitude: '',
    longitude: '',
    address: '',
  });

  /**
   * 初始化定位入口
   */
  async function initGetMapInfo() {
    // #ifdef MP-WEIXIN
    const setting = await uni.getSetting();
    const authStatus = setting.authSetting['scope.userLocation'];
    if (authStatus === false) return showAuthModal();
    // #endif

    executeLocation();
  }

  /**
   * 核心定位逻辑
   */
  function executeLocation() {
    uni.showLoading({ title: '定位中...', mask: true });

    uni.getLocation({
      type: 'gcj02',
      geocode: true, // 仅 App 端有效
      success: async (res) => {
        mapInfo.value.latitude = res.latitude;
        mapInfo.value.longitude = res.longitude;

        // #ifdef APP-PLUS
        // 1. App 端直接处理
        if (res.address) {
          mapInfo.value.district = res.address.district || '';
          mapInfo.value.name = res.address.district || '已定位';
          mapInfo.value.address = res.address.city + res.address.district + res.address.street;
          uni.hideLoading();
        } else {
          // 有些App打包配置没生效时，走下面的API解析
          await getDistrictByAmap(res.longitude, res.latitude);
        }
        // #endif

        // #ifdef MP-WEIXIN || H5
        // 2. 小程序和 H5 通过高德 API 解析
        await getDistrictByAmap(res.longitude, res.latitude);
        // #endif
      },
      fail: (err) => {
        uni.hideLoading();
        handleFail(err);
      },
    });
  }

  /**
   * 高德地图逆地理编码 (获取区/县)
   */
  function getDistrictByAmap(lng, lat) {
    return new Promise((resolve) => {
      uni.request({
        url: 'https://restapi.amap.com/v3/geocode/regeo',
        data: {
          key: amapKey,
          location: `${lng},${lat}`,
          extensions: 'base'
        },
        success: (regeo) => {
          if (regeo.data.status === '1') {
            const component = regeo.data.regcodes ? null : regeo.data.regeoCode.addressComponent; 
            // 注意：高德接口返回结构可能因版本微调，通常如下：
            const addressData = regeo.data.regeo.addressComponent;
            mapInfo.value.district = addressData.district || '';
            mapInfo.value.name = addressData.district || '未知位置';
            mapInfo.value.address = regeo.data.regeo.formatted_address;
          }
          resolve();
        },
        fail: () => {
          mapInfo.value.name = '获取地址失败';
          resolve();
        },
        complete: () => {
          uni.hideLoading();
        }
      });
    });
  }

  /**
   * 失败处理
   */
  function handleFail(err) {
    // #ifdef MP-WEIXIN
    if (err.errMsg.includes('auth deny')) {
      showAuthModal();
    } else {
      uni.showToast({ title: '请开启手机GPS', icon: 'none' });
    }
    // #endif

    // #ifdef APP-PLUS
    uni.showModal({
      title: '定位失败',
      content: '请确认手机定位开启并授予App权限',
      showCancel: false,
    });
    // #endif

    // #ifdef H5
    uni.showToast({ title: '浏览器定位失败', icon: 'none' });
    // #endif
  }

  /**
   * 引导授权（仅小程序）
   */
  function showAuthModal() {
    uni.showModal({
      title: '提示',
      content: '需要位置信息提供附近服务，请前往设置开启',
      confirmText: '去设置',
      success: (res) => {
        if (res.confirm) {
          uni.openSetting({
            success: (setRes) => {
              if (setRes.authSetting['scope.userLocation']) executeLocation();
            },
          });
        }
      },
    });
  }

  /**
   * 手动选择
   */
  function chooseManual() {
    uni.chooseLocation({
      success: (res) => {
        mapInfo.value.latitude = res.latitude;
        mapInfo.value.longitude = res.longitude;
        mapInfo.value.address = res.address;
        const reg = /.+?(省|自治区|直辖市).+?(市|自治州).+?(.+?(区|县|旗|市))/;
        const match = res.address.match(reg);
        mapInfo.value.district = match ? match[3] : res.name;
        mapInfo.value.name = mapInfo.value.district;
      },
    });
  }

  return { mapInfo, initGetMapInfo, chooseManual };
});