<template>
  <div class="page-panel">
    <h2 class="page-title">优惠券与会员</h2>
    <p class="page-desc">管理券包、积分、会员等级和营销规则。</p>
    <el-table :data="rows">
      <el-table-column prop="name" label="名称" />
      <el-table-column prop="type" label="类型" />
      <el-table-column prop="rule" label="规则" />
      <el-table-column prop="status" label="状态" />
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
