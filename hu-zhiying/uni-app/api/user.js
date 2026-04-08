import { addressList, chatMessages, cityList, couponList } from '../mock/data';
import { request, shouldUseMock } from '../utils/request';

const clone = (payload) => JSON.parse(JSON.stringify(payload));

function normalizeMessages(list = []) {
  return list.map((item) => ({
    id: item.id,
    type: item.type || item.sender || 'system',
    content: item.content,
    time: item.time || '刚刚',
  }));
}

export async function loginWithRole(role = 'user') {
  if (shouldUseMock()) {
    return {
      data: {
        token: `mock-token-${role}`,
        refreshToken: `mock-refresh-${role}`,
        role,
      },
    };
  }

  const api = role === 'user' ? '/api/auth/wechat-login' : '/api/auth/sms-login';
  return request({
    url: api,
    method: 'POST',
    data: { role },
  });
}

export async function getCurrentUser() {
  if (shouldUseMock()) {
    return {
      data: {
        profile: {
          id: 10001,
          nickname: '周女士',
          mobile: '138****5288',
          avatar: '/static/user.png',
          level: 'SVIP 预备用户',
        },
        notices: [],
        banners: [],
      },
    };
  }
  return request({ url: '/api/users/me' });
}

export async function updateProfile(payload) {
  if (shouldUseMock()) {
    return {
      data: payload,
    };
  }
  return request({
    url: '/api/users/me',
    method: 'PUT',
    data: payload,
  });
}

export async function getAddressList() {
  if (shouldUseMock()) {
    return { data: clone(addressList) };
  }
  const response = await request({ url: '/api/addresses' });
  return {
    data: (response.data || []).map((item) => ({
      ...item,
      address: item.detail,
      default: item.isDefault,
    })),
  };
}

export async function saveAddress(payload) {
  if (shouldUseMock()) {
    return { data: payload };
  }
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

export async function getCouponList() {
  if (shouldUseMock()) {
    return { data: clone(couponList) };
  }
  return request({ url: '/api/coupons' });
}

export async function getCityList() {
  return { data: clone(cityList) };
}

export async function getChatMessages(sessionId = 'MS-001') {
  if (shouldUseMock()) {
    return { data: normalizeMessages(clone(chatMessages)) };
  }
  const response = await request({ url: `/api/messages/${sessionId}/items` });
  return {
    data: normalizeMessages(response.data || []),
  };
}

export async function sendChatMessage(sessionId, payload) {
  if (shouldUseMock()) {
    return {
      data: {
        id: Date.now(),
        type: payload.senderCode || 'user',
        content: payload.content,
        time: '刚刚',
      },
    };
  }
  const response = await request({
    url: `/api/messages/${sessionId}/items`,
    method: 'POST',
    data: payload,
  });
  return {
    data: normalizeMessages([response.data])[0],
  };
}
