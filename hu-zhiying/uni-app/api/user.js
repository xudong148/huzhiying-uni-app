import { request } from '../utils/request';

function normalizeMessages(list = []) {
  return list.map((item) => ({
    id: item.id,
    sender: item.sender || item.senderCode || 'system',
    type: item.type || item.messageType || 'text',
    content: item.content,
    time: item.time || '刚刚',
  }));
}

/**
 * 用户或师傅登录。
 * @param {'user'|'master'} role
 */
export function loginWithRole(role = 'user') {
  const api = role === 'user' ? '/api/auth/wechat-login' : '/api/auth/sms-login';
  return request({
    url: api,
    method: 'POST',
    data: { role },
  });
}

/**
 * 查询当前登录用户信息。
 */
export function getCurrentUser() {
  return request({ url: '/api/users/me' });
}

/**
 * 更新用户资料。
 * @param {{nickname:string,mobile:string}} payload
 */
export function updateProfile(payload) {
  return request({
    url: '/api/users/me',
    method: 'PUT',
    data: payload,
  });
}

/**
 * 查询地址列表。
 */
export async function getAddressList() {
  const response = await request({ url: '/api/addresses' });
  return {
    data: (response.data || []).map((item) => ({
      ...item,
      address: item.detail,
      default: item.isDefault,
    })),
  };
}

/**
 * 新增或更新地址。
 * @param {{id?:number|string,tag:string,name:string,mobile:string,address:string,default:boolean}} payload
 */
export function saveAddress(payload) {
  return request({
    url: '/api/addresses',
    method: 'POST',
    data: {
      id: payload.id,
      tag: payload.tag,
      name: payload.name,
      mobile: payload.mobile,
      detailAddress: payload.address,
      isDefault: payload.default,
    },
  });
}

/**
 * 删除地址。
 * @param {number|string} id
 */
export function deleteAddress(id) {
  return request({
    url: `/api/addresses/${id}`,
    method: 'DELETE',
  });
}

/**
 * 查询优惠券列表。
 */
export function getCouponList() {
  return request({ url: '/api/coupons' });
}

/**
 * 兼容旧页面查询可服务城市。
 */
export function getCityList() {
  return request({ url: '/api/map/service-cities' });
}

/**
 * 查询聊天消息列表。
 * @param {string} sessionId
 * @returns {Promise<{data:Array}>}
 */
export async function getChatMessages(sessionId = 'MS-001') {
  const response = await request({ url: `/api/messages/${sessionId}/items` });
  return {
    data: normalizeMessages(response.data || []),
  };
}

/**
 * 发送聊天消息。
 * @param {string} sessionId
 * @param {{senderCode:string,messageType:string,content:string}} payload
 * @returns {Promise<{data:object}>}
 */
export async function sendChatMessage(sessionId, payload) {
  const response = await request({
    url: `/api/messages/${sessionId}/items`,
    method: 'POST',
    data: payload,
  });
  return {
    data: normalizeMessages([response.data])[0],
  };
}
