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

function normalizeMessageSessions(list = []) {
  return list.map((item) => ({
    id: item.id || item.sessionId,
    sessionId: item.sessionId || item.id,
    orderId: item.orderId || '',
    title: item.title || '在线沟通',
    participant: item.participant || '平台客服',
    latestMessage: item.latestMessage || '',
    latestTime: item.latestTime || '',
    unreadCount: Number(item.unreadCount || 0),
    messageCount: Number(item.messageCount || 0),
  }));
}

export function loginWithRole(role = 'user') {
  const api = role === 'user' ? '/api/auth/wechat-login' : '/api/auth/sms-login';
  return request({
    url: api,
    method: 'POST',
    data: { role },
  });
}

export function getCurrentUser() {
  return request({ url: '/api/users/me' });
}

export function updateProfile(payload) {
  return request({
    url: '/api/users/me',
    method: 'PUT',
    data: payload,
  });
}

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

export function deleteAddress(id) {
  return request({
    url: `/api/addresses/${id}`,
    method: 'DELETE',
  });
}

export function getCouponList() {
  return request({ url: '/api/coupons' });
}

export function getCityList() {
  return request({ url: '/api/map/service-cities' });
}

export async function getMessageSessions() {
  const response = await request({ url: '/api/messages/sessions' });
  return {
    data: normalizeMessageSessions(response.data || []),
  };
}

export async function getChatMessages(sessionId = 'MS-001') {
  const response = await request({ url: `/api/messages/${sessionId}/items` });
  return {
    data: normalizeMessages(response.data || []),
  };
}

export function markChatSessionRead(sessionId) {
  return request({
    url: `/api/messages/${sessionId}/read`,
    method: 'POST',
    showErrorToast: false,
  });
}

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
