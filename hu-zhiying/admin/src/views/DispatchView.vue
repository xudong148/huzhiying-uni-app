<template>
  <div class="page-panel">
    <h2 class="page-title">订单调度中心</h2>
    <p class="page-desc">地图调度、抢单状态与人工改派统一管理。</p>
    <el-table :data="rows">
      <el-table-column prop="id" label="订单号" />
      <el-table-column prop="type" label="类型" />
      <el-table-column prop="area" label="区域" />
      <el-table-column prop="status" label="状态" />
      <el-table-column prop="master" label="当前师傅" />
      <el-table-column prop="amount" label="金额" />
    </el-table>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import { getAdminMock } from '../api/request';

const rows = ref([]);

onMounted(async () => {
  rows.value = await getAdminMock('dispatchRows');
});
</script>
