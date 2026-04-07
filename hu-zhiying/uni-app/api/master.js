import { masterOrders, walletData } from '../mock/data';

const clone = (payload) => JSON.parse(JSON.stringify(payload));

export function getDispatchOrders() {
  return Promise.resolve({
    data: clone(masterOrders),
  });
}

export function getWalletData() {
  return Promise.resolve({
    data: clone(walletData),
  });
}
