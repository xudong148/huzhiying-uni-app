<template>
  <div class="page-panel">
    <h2 class="page-title">订单管理</h2>
    <p class="page-desc">同时覆盖服务订单与商城订单生命周期。</p>
    <el-table :data="rows">
      <el-table-column prop="id" label="订单号" />
      <el-table-column prop="category" label="品类" />
      <el-table-column prop="status" label="状态" />
      <el-table-column prop="user" label="用户" />
      <el-table-column prop="amount" label="金额" />
    </el-table>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import { getAdminMock } from '../api/request';

const rows = ref([]);

onMounted(async () => {
  rows.value = await getAdminMock('orderRows');
});
</script>
