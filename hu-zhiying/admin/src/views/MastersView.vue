<template>
  <div class="page-panel">
    <h2 class="page-title">师傅管理</h2>
    <p class="page-desc">资质、信用分、在线状态和技能标签统一维护。</p>
    <el-table :data="rows">
      <el-table-column prop="name" label="师傅" />
      <el-table-column prop="skills" label="技能标签" />
      <el-table-column prop="score" label="评分" />
      <el-table-column prop="status" label="在线状态" />
      <el-table-column prop="credit" label="信用等级" />
    </el-table>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import { getAdminMock } from '../api/request';

const rows = ref([]);

onMounted(async () => {
  rows.value = await getAdminMock('masterRows');
});
</script>
