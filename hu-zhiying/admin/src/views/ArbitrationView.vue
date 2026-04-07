<template>
  <div class="page-panel">
    <h2 class="page-title">仲裁中心</h2>
    <p class="page-desc">查看客诉、聊天记录和凭证材料，进行责任归属裁定。</p>
    <el-table :data="rows">
      <el-table-column prop="id" label="仲裁号" />
      <el-table-column prop="orderId" label="关联订单" />
      <el-table-column prop="reason" label="原因" />
      <el-table-column prop="status" label="状态" />
    </el-table>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import { getAdminMock } from '../api/request';

const rows = ref([]);

onMounted(async () => {
  rows.value = await getAdminMock('arbitrationRows');
});
</script>
