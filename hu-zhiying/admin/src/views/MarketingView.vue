<template>
  <div>
    <!-- 优惠券配置 -->
    <crud-console
      title="优惠券"
      description="维护新客券、活动券和门槛规则。"
      resource="/api/admin/marketing/coupons"
      :columns="couponColumns"
      :fields="couponFields"
    />

    <!-- 会员等级配置 -->
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
/**
 * 营销与会员页面。
 * 优惠券和会员等级都走真实配置表。
 */
import CrudConsole from '../components/CrudConsole.vue';

// 优惠券表格与表单配置。
const couponColumns = [
  { prop: 'title', label: '优惠券名称', minWidth: 220 },
  { prop: 'amount', label: '面额', width: 120, formatter: (row) => `¥${Number(row.amount || 0).toFixed(2)}` },
  { prop: 'thresholdText', label: '使用门槛', minWidth: 180 },
  { prop: 'expireAt', label: '过期时间', width: 140 },
  { prop: 'enabled', label: '状态', width: 100, type: 'switch' },
];

const couponFields = [
  { prop: 'title', label: '优惠券名称', type: 'text' },
  { prop: 'amount', label: '面额', type: 'number', precision: 2, min: 0 },
  { prop: 'thresholdText', label: '门槛说明', type: 'text' },
  { prop: 'expireAt', label: '过期时间', type: 'text', placeholder: '例如 2026-06-30' },
  { prop: 'enabled', label: '启用', type: 'switch', default: true },
];

// 会员等级表格与表单配置。
const memberColumns = [
  { prop: 'name', label: '等级名称', minWidth: 180 },
  { prop: 'pointsRequired', label: '所需积分', width: 120 },
  { prop: 'benefitText', label: '权益说明', minWidth: 280 },
  { prop: 'enabled', label: '状态', width: 100, type: 'switch' },
];

const memberFields = [
  { prop: 'name', label: '等级名称', type: 'text' },
  { prop: 'pointsRequired', label: '所需积分', type: 'number', min: 0 },
  { prop: 'benefitText', label: '权益说明', type: 'textarea', rows: 5 },
  { prop: 'enabled', label: '启用', type: 'switch', default: true },
];
</script>

<style scoped>
/* 当前页使用通用 CRUD 面板布局，这里保留样式区块注释以统一页面结构。 */
</style>
