<template>
  <div>
    <crud-console
      title="优惠券"
      description="维护新客券、活动券和门槛规则。"
      resource="/api/admin/marketing/coupons"
      :columns="couponColumns"
      :fields="couponFields"
    />

    <crud-console
      title="会员等级"
      description="维护会员等级门槛、权益文案和启用状态。"
      resource="/api/admin/marketing/member-levels"
      :columns="memberColumns"
      :fields="memberFields"
      width="820px"
    />
  </div>
</template>

<script setup>
import CrudConsole from '../components/CrudConsole.vue';

const couponColumns = [
  { prop: 'title', label: '优惠券名称', minWidth: 220 },
  { prop: 'amount', label: '面额', width: 120, formatter: (row) => `¥${Number(row.amount || 0).toFixed(2)}` },
  { prop: 'thresholdText', label: '使用门槛', minWidth: 180 },
  { prop: 'expireAt', label: '过期时间', width: 140 },
  { prop: 'enabled', label: '状态', width: 100, type: 'switch' },
];

const couponFields = [
  { prop: 'title', label: '优惠券名称', type: 'text', required: true },
  { prop: 'amount', label: '面额', type: 'number', precision: 2, min: 0, required: true },
  { prop: 'thresholdText', label: '门槛说明', type: 'text', required: true },
  {
    prop: 'expireAt',
    label: '过期时间',
    type: 'date',
    required: true,
    hint: '服务端固定按 yyyy-MM-dd 校验。',
  },
  { prop: 'enabled', label: '启用', type: 'switch', default: true },
];

const memberColumns = [
  { prop: 'name', label: '等级名称', minWidth: 180 },
  { prop: 'pointsRequired', label: '所需积分', width: 120 },
  { prop: 'benefitText', label: '权益说明', minWidth: 280 },
  { prop: 'enabled', label: '状态', width: 100, type: 'switch' },
];

const memberFields = [
  { prop: 'name', label: '等级名称', type: 'text', required: true },
  { prop: 'pointsRequired', label: '所需积分', type: 'number', min: 0, required: true },
  { prop: 'benefitText', label: '权益说明', type: 'textarea', rows: 5, required: true },
  { prop: 'enabled', label: '启用', type: 'switch', default: true },
];
</script>

<style scoped>
</style>
