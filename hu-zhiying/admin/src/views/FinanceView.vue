<template>
  <div class="page-panel">
    <h2 class="page-title">财务结算</h2>
    <p class="page-desc">师傅结算、退款审核和资金流水统一处理。</p>
    <el-table :data="rows">
      <el-table-column prop="billNo" label="单号" />
      <el-table-column prop="type" label="类型" />
      <el-table-column prop="amount" label="金额" />
      <el-table-column prop="status" label="状态" />
    </el-table>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import { getAdminMock } from '../api/request';

const rows = ref([]);

onMounted(async () => {
  rows.value = await getAdminMock('financeRows');
});
</script>
