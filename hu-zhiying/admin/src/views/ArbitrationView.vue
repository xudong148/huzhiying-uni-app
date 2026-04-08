<template>
  <div class="page-panel">
    <h2 class="page-title">仲裁中心</h2>
    <p class="page-desc">查看用户投诉、责任判定和售后争议处理进度。</p>
    <el-table :data="rows">
      <el-table-column prop="id" label="仲裁号" min-width="160" />
      <el-table-column prop="orderId" label="关联订单" min-width="180" />
      <el-table-column prop="reason" label="原因" min-width="220" />
      <el-table-column prop="status" label="状态" width="120" />
    </el-table>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import { fetchAdminArbitrations } from '../api/request';

const rows = ref([]);

onMounted(async () => {
  rows.value = await fetchAdminArbitrations();
});
</script>
