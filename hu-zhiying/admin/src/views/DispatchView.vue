<template>
  <div class="page-panel">
    <h2 class="page-title">订单调度中心</h2>
    <p class="page-desc">查看派单状态、服务区域和当前师傅归属，支持一键强派。</p>
    <el-table :data="rows">
      <el-table-column prop="id" label="订单号" min-width="180" />
      <el-table-column prop="type" label="类型" width="100" />
      <el-table-column prop="area" label="区域" width="120" />
      <el-table-column prop="status" label="状态" width="120" />
      <el-table-column prop="master" label="当前师傅" width="140" />
      <el-table-column label="金额" width="120">
        <template #default="{ row }">¥{{ Number(row.amount || 0).toFixed(2) }}</template>
      </el-table-column>
      <el-table-column label="操作" width="180" fixed="right">
        <template #default="{ row }">
          <el-button size="small" type="primary" :loading="loadingTaskId === row.taskId" @click="handleAssign(row)">
            强派给张师傅
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { onBeforeUnmount, onMounted, ref } from 'vue';
import { ElMessage } from 'element-plus';
import { buildAdminWsUrl, fetchAdminDispatch, forceAssignDispatch } from '../api/request';

const rows = ref([]);
const loadingTaskId = ref('');
let socket = null;

async function loadRows() {
  rows.value = await fetchAdminDispatch();
}

function connectSocket() {
  if (socket) {
    return;
  }
  socket = new WebSocket(buildAdminWsUrl('/ws/dispatch'));
  socket.addEventListener('message', () => {
    loadRows();
  });
  socket.addEventListener('close', () => {
    socket = null;
  });
}

function closeSocket() {
  if (socket) {
    socket.close();
    socket = null;
  }
}

async function handleAssign(row) {
  loadingTaskId.value = row.taskId;
  try {
    await forceAssignDispatch(row.taskId, '张师傅');
    ElMessage.success('强派成功');
    await loadRows();
  } catch (error) {
    ElMessage.error(error.message || '强派失败');
  } finally {
    loadingTaskId.value = '';
  }
}

onMounted(async () => {
  await loadRows();
  connectSocket();
});

onBeforeUnmount(closeSocket);
</script>
