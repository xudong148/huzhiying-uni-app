<template>
  <div class="page-panel">
    <h2 class="page-title">优惠券与会员</h2>
    <p class="page-desc">当前阶段保留配置占位，主链路不依赖这里的 mock 数据。</p>
    <el-table :data="rows">
      <el-table-column prop="name" label="名称" min-width="180" />
      <el-table-column prop="type" label="类型" width="120" />
      <el-table-column prop="rule" label="规则" min-width="180" />
      <el-table-column prop="status" label="状态" width="120" />
    </el-table>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import { getAdminMock } from '../api/request';

const rows = ref([]);

onMounted(async () => {
  rows.value = await getAdminMock('marketingRows');
});
</script>
