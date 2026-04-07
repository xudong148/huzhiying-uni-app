import { orderList, orderTimeline, quotationDetail, timeSlots } from '../mock/data';

const clone = (payload) => JSON.parse(JSON.stringify(payload));

export function getOrderList() {
  return Promise.resolve({
    data: clone(orderList),
  });
}

export function getOrderDetail(orderId) {
  const order = orderList.find((item) => item.id === orderId) || orderList[0];
  return Promise.resolve({
    data: {
      ...clone(order),
      timeline: clone(orderTimeline),
      quotation: clone(quotationDetail),
      eta: '26 分钟',
      masterName: '张师傅',
      masterMobile: '170****8899',
    },
  });
}

export function getTimeSlots() {
  return Promise.resolve({
    data: clone(timeSlots),
  });
}
