<template>
  <div class="page-panel">
    <!-- 页面标题 -->
    <h2 class="page-title">订单管理</h2>
    <p class="page-desc">统一查看服务单和商品单的状态、用户和金额。</p>

    <!-- 订单表格 -->
    <el-table :data="rows">
      <el-table-column prop="id" label="订单号" min-width="180" />
      <el-table-column prop="category" label="品类" min-width="220" />
      <el-table-column prop="status" label="状态" width="120" />
      <el-table-column prop="user" label="用户" width="120" />
      <el-table-column label="金额" width="120">
        <template #default="{ row }">¥{{ Number(row.amount || 0).toFixed(2) }}</template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
/**
 * 订单管理页面。
 * 后台统一读取真实订单列表，用于运维和客服排查。
 */
import { onMounted, ref } from 'vue';
import { fetchAdminOrders } from '../api/request';

const rows = ref([]);

onMounted(async () => {
  rows.value = await fetchAdminOrders();
});
</script>

<style scoped>
/* 当前页依赖全局表格样式，这里保留样式区块注释以统一页面结构。 */
</style>
