<template>
  <div class="page-panel">
    <h2 class="page-title">定价与类目</h2>
    <p class="page-desc">维护基础价、指导价和特殊时段系数。</p>
    <el-table :data="rows">
      <el-table-column prop="category" label="类目" min-width="180" />
      <el-table-column label="基础价" width="120">
        <template #default="{ row }">¥{{ Number(row.basePrice || 0).toFixed(2) }}</template>
      </el-table-column>
      <el-table-column prop="guidePrice" label="指导价" width="140" />
      <el-table-column prop="coefficient" label="系数规则" min-width="160" />
    </el-table>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import { fetchAdminPricing } from '../api/request';

const rows = ref([]);

onMounted(async () => {
  rows.value = await fetchAdminPricing();
});
</script>
