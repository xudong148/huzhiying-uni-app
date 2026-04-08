<template>
  <div class="page-panel">
    <h2 class="page-title">系统权限</h2>
    <p class="page-desc">角色菜单和敏感操作边界当前按 mock 配置展示。</p>
    <el-table :data="rows">
      <el-table-column prop="role" label="角色" width="180" />
      <el-table-column prop="scope" label="权限范围" min-width="280" />
    </el-table>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import { getAdminMock } from '../api/request';

const rows = ref([]);

onMounted(async () => {
  rows.value = await getAdminMock('systemRows');
});
</script>
