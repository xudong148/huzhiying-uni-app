<template>
  <div class="page-panel">
    <h2 class="page-title">内容运营</h2>
    <p class="page-desc">首页 Banner、公告和活动位仍保留 mock 配置入口。</p>
    <el-table :data="rows">
      <el-table-column prop="name" label="内容名称" min-width="220" />
      <el-table-column prop="type" label="类型" width="120" />
      <el-table-column prop="status" label="状态" width="120" />
    </el-table>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import { getAdminMock } from '../api/request';

const rows = ref([]);

onMounted(async () => {
  rows.value = await getAdminMock('contentRows');
});
</script>
