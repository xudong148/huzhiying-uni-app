<template>
  <div class="page-panel">
    <h2 class="page-title">师傅管理</h2>
    <p class="page-desc">查看技能标签、服务区域、在线状态与保证金情况。</p>
    <el-table :data="rows">
      <el-table-column prop="name" label="师傅" width="120" />
      <el-table-column prop="skills" label="技能标签" min-width="220" />
      <el-table-column prop="area" label="服务区域" width="140" />
      <el-table-column prop="status" label="在线状态" width="120" />
      <el-table-column prop="score" label="信用分" width="100" />
      <el-table-column prop="credit" label="信用等级" width="100" />
      <el-table-column label="保证金" width="120">
        <template #default="{ row }">¥{{ Number(row.deposit || 0).toFixed(0) }}</template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import { fetchAdminMasters } from '../api/request';

const rows = ref([]);

onMounted(async () => {
  rows.value = await fetchAdminMasters();
});
</script>
