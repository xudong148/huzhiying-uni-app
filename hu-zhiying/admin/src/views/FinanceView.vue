<template>
  <div class="page-panel">
    <!-- 页面标题 -->
    <h2 class="page-title">财务结算</h2>
    <p class="page-desc">查看师傅结算流水和退款处理状态。</p>

    <!-- 财务表格 -->
    <el-table :data="rows">
      <el-table-column prop="billNo" label="单号" min-width="180" />
      <el-table-column prop="type" label="类型" width="120" />
      <el-table-column label="金额" width="120">
        <template #default="{ row }">¥{{ Number(row.amount || 0).toFixed(2) }}</template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="120" />
    </el-table>
  </div>
</template>

<script setup>
/**
 * 财务结算页面。
 * 后台读取真实流水和退款状态数据。
 */
import { onMounted, ref } from 'vue';
import { fetchAdminFinance } from '../api/request';

const rows = ref([]);

onMounted(async () => {
  rows.value = await fetchAdminFinance();
});
</script>

<style scoped>
/* 当前页依赖全局表格样式，这里保留样式区块注释以统一页面结构。 */
</style>
