import { request } from '../utils/request';

function normalizeServiceCity(item = {}) {
  return {
    id: item.id,
    name: item.city || item.cityName || '',
    district: item.district || item.districtName || '',
    hot: Boolean(item.hot),
  };
}

/**
 * 查询平台支持服务的城市和区域。
 * @returns {Promise<{data: Array<{id:number,name:string,district:string,hot:boolean}>}>}
 */
export async function getServiceCities() {
  const response = await request({
    url: '/api/map/service-cities',
  });
  return {
    data: (response.data || []).map(normalizeServiceCity),
  };
}

/**
 * 根据经纬度查询当前城市、区县和标准化地址。
 * @param {{latitude:number, longitude:number}} payload
 */
export async function reverseGeocode(payload) {
  const response = await request({
    url: '/api/map/regeo',
    method: 'POST',
    data: payload,
  });
  return {
    data: {
      city: response.data?.city || '',
      district: response.data?.district || '',
      address: response.data?.address || '',
      serviceable: Boolean(response.data?.serviceable),
    },
  };
}

/**
 * 校验城市和区域是否在平台服务范围内。
 * @param {{city:string, district:string}} payload
 */
export async function checkServiceArea(payload) {
  const response = await request({
    url: '/api/map/geofence/check',
    method: 'POST',
    data: payload,
  });
  return {
    data: {
      serviceable: Boolean(response.data?.serviceable),
      matchedZone: response.data?.matchedZone || '',
    },
  };
}

/**
 * 查询订单 ETA 和距离提示。
 * @param {string} orderId
 */
export async function getOrderEta(orderId) {
  const response = await request({
    url: '/api/map/eta',
    method: 'POST',
    data: { orderId },
  });
  return {
    data: {
      eta: response.data?.eta || '',
      distance: response.data?.distance || '',
    },
  };
}
