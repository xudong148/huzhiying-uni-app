<template>
  <div class="page-panel">
    <h2 class="page-title">内容运营</h2>
    <p class="page-desc">轮播、公告、协议与活动内容统一管理。</p>
    <el-table :data="rows">
      <el-table-column prop="name" label="内容名称" />
      <el-table-column prop="type" label="类型" />
      <el-table-column prop="status" label="状态" />
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
