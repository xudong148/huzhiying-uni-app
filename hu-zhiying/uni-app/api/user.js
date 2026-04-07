import { addressList, chatMessages, cityList, couponList } from '../mock/data';

const clone = (payload) => JSON.parse(JSON.stringify(payload));

export function getAddressList() {
  return Promise.resolve({
    data: clone(addressList),
  });
}

export function getCouponList() {
  return Promise.resolve({
    data: clone(couponList),
  });
}

export function getCityList() {
  return Promise.resolve({
    data: clone(cityList),
  });
}

export function getChatMessages() {
  return Promise.resolve({
    data: clone(chatMessages),
  });
}
