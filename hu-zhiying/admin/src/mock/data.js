export const dashboard = {
  gmv: '256800',
  serviceOrders: 328,
  productOrders: 96,
  onlineMasters: 56,
  warning: 12,
};

export const dispatchRows = [
  { taskId: 'DISP-001', id: 'SO20260408001', type: '维修', area: '浦东新区', status: '抢单中', master: '待接单', amount: 88 },
  { taskId: 'DISP-002', id: 'SO20260408008', type: '安装', area: '徐汇区', status: '待上门', master: '张师傅', amount: 258 },
];

export const orderRows = [
  { id: 'SO20260408001', category: '空调上门维修', status: '待接单', user: '周女士', amount: 88 },
  { id: 'PO20260406018', category: '智能锁 Pro 套装', status: '待发货', user: '林先生', amount: 1699 },
];

export const masterRows = [
  { name: '张师傅', skills: '空调维修 / 智能锁安装', score: 98, status: '在线', credit: 'A', area: '浦东新区', deposit: 3000 },
  { name: '陈师傅', skills: '保洁 / 收纳', score: 92, status: '休息', credit: 'A-', area: '徐汇区', deposit: 3000 },
];

export const pricingRows = [
  { category: '空调维修', basePrice: 58, guidePrice: '58-299', coefficient: '夜间 +30%' },
  { category: '智能锁安装', basePrice: 128, guidePrice: '128-388', coefficient: '节假日 +20%' },
];

export const financeRows = [
  { billNo: 'SETTLE-001', type: '师傅结算', amount: 268, status: '待结算' },
  { billNo: 'REFUND-003', type: '退款单', amount: 88, status: '处理中' },
];

export const arbitrationRows = [
  { id: 'ARB-001', orderId: 'SO20260407009', reason: '乱收费', status: '待裁决' },
  { id: 'ARB-002', orderId: 'SO20260406016', reason: '服务未达标', status: '已赔付' },
];

export const marketingRows = [
  { name: '新客上门券', type: '优惠券', rule: '满99减30', status: '生效中' },
  { name: 'SVIP 金卡', type: '会员', rule: '服务 9 折', status: '生效中' },
];

export const contentRows = [
  { name: '首页轮播-家电维修专场', type: 'Banner', status: '已上线' },
  { name: '夜间服务费公告', type: '公告', status: '已上线' },
];

export const systemRows = [
  { role: '超级管理员', scope: '全量菜单 / 审核 / 财务 / 仲裁' },
  { role: '客服运营', scope: '订单 / 仲裁 / 内容 / 用户沟通' },
];
