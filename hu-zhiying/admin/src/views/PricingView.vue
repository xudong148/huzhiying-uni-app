<template>
  <div class="page-panel">
    <h2 class="page-title">定价与类目</h2>
    <p class="page-desc">维护基础价、指导价和时段系数。</p>
    <el-table :data="rows">
      <el-table-column prop="category" label="类目" />
      <el-table-column prop="basePrice" label="基础价" />
      <el-table-column prop="guidePrice" label="指导价" />
      <el-table-column prop="coefficient" label="时间系数" />
    </el-table>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import { getAdminMock } from '../api/request';

const rows = ref([]);

onMounted(async () => {
  rows.value = await getAdminMock('pricingRows');
});
</script>
