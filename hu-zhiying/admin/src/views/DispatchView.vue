<template>
  <div class="page-panel">
    <!-- 页面标题区 -->
    <h2 class="page-title">订单调度中心</h2>
    <p class="page-desc">查看待派单和已派单任务，支持详情、指派师傅和强制取消订单。</p>

    <!-- 调度列表区 -->
    <el-table :data="rows">
      <el-table-column prop="id" label="订单号" min-width="180" />
      <el-table-column prop="type" label="类型" width="100" />
      <el-table-column prop="area" label="区域" width="120" />
      <el-table-column prop="status" label="状态" width="120" />
      <el-table-column prop="master" label="当前师傅" width="140" />
      <el-table-column label="金额" width="120">
        <template #default="{ row }">¥{{ Number(row.amount || 0).toFixed(2) }}</template>
      </el-table-column>
      <el-table-column label="操作" width="260" fixed="right">
        <template #default="{ row }">
          <div class="action-row">
            <el-button size="small" @click="openDetail(row.taskId)">详情</el-button>
            <el-button size="small" type="primary" @click="openAssignDialog(row)">指派</el-button>
            <el-button size="small" type="danger" @click="handleCancel(row)">取消订单</el-button>
          </div>
        </template>
      </el-table-column>
    </el-table>

    <!-- 详情抽屉 -->
    <el-drawer v-model="detailVisible" title="调度详情" size="560px">
      <template v-if="detail">
        <el-descriptions :column="1" border>
          <el-descriptions-item label="任务号">{{ detail.taskId }}</el-descriptions-item>
          <el-descriptions-item label="订单号">{{ detail.orderId }}</el-descriptions-item>
          <el-descriptions-item label="标题">{{ detail.title }}</el-descriptions-item>
          <el-descriptions-item label="类型">{{ detail.orderType }}</el-descriptions-item>
          <el-descriptions-item label="状态">{{ detail.taskStatus }}</el-descriptions-item>
          <el-descriptions-item label="派单方式">{{ detail.dispatchMode || '—' }}</el-descriptions-item>
          <el-descriptions-item label="当前师傅">{{ detail.masterName || '待分配' }}</el-descriptions-item>
          <el-descriptions-item label="预约时间">{{ detail.appointment || '—' }}</el-descriptions-item>
          <el-descriptions-item label="服务地址">{{ detail.address || '—' }}</el-descriptions-item>
          <el-descriptions-item label="服务区域">{{ detail.area || '—' }}</el-descriptions-item>
          <el-descriptions-item label="金额">¥{{ Number(detail.amount || 0).toFixed(2) }}</el-descriptions-item>
          <el-descriptions-item label="标签">
            <el-tag v-for="tag in detail.tags || []" :key="tag" class="tag-item">{{ tag }}</el-tag>
          </el-descriptions-item>
        </el-descriptions>

        <div class="drawer-section">
          <h3 class="drawer-title">履约时间轴</h3>
          <el-timeline>
            <el-timeline-item
              v-for="item in detail.timeline || []"
              :key="`${item.stepKey}-${item.time}`"
              :timestamp="item.time || ''"
              :type="item.done ? 'primary' : 'info'"
            >
              <div class="timeline-label">{{ item.label }}</div>
              <div class="timeline-desc">{{ item.description }}</div>
            </el-timeline-item>
          </el-timeline>
        </div>
      </template>
    </el-drawer>

    <!-- 指派弹窗 -->
    <el-dialog v-model="assignVisible" title="指派师傅" width="420px">
      <el-form label-width="100px">
        <el-form-item label="师傅姓名">
          <el-input v-model="assignForm.masterName" placeholder="请输入师傅姓名" />
        </el-form-item>
        <el-form-item label="师傅用户 ID">
          <el-input v-model.number="assignForm.masterUserId" placeholder="可选，优先按姓名指派" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="assignVisible = false">取消</el-button>
        <el-button type="primary" :loading="assigning" @click="submitAssign">确认指派</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
/**
 * 调度中心页面。
 * 1. 列表从真实后端接口读取。
 * 2. 详情、指派、取消动作都走后台业务接口。
 * 3. 通过调度 WebSocket 自动刷新列表与详情。
 */
import { onBeforeUnmount, onMounted, reactive, ref } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import {
  assignDispatchTask,
  buildAdminWsUrl,
  cancelDispatchOrder,
  fetchAdminDispatch,
  fetchDispatchDetail,
} from '../api/request';

const rows = ref([]);
const detailVisible = ref(false);
const detail = ref(null);
const assignVisible = ref(false);
const assigning = ref(false);
const currentTaskId = ref('');
const assignForm = reactive({
  masterName: '',
  masterUserId: null,
});

let socket = null;

async function loadRows() {
  rows.value = await fetchAdminDispatch();
}

async function openDetail(taskId) {
  detail.value = await fetchDispatchDetail(taskId);
  detailVisible.value = true;
}

function openAssignDialog(row) {
  currentTaskId.value = row.taskId;
  assignForm.masterName = row.master === '待接单' ? '' : row.master;
  assignForm.masterUserId = null;
  assignVisible.value = true;
}

async function submitAssign() {
  assigning.value = true;
  try {
    await assignDispatchTask(currentTaskId.value, {
      masterName: assignForm.masterName || null,
      masterUserId: assignForm.masterUserId || null,
    });
    assignVisible.value = false;
    ElMessage.success('指派成功');
    await loadRows();
    if (detailVisible.value && currentTaskId.value) {
      detail.value = await fetchDispatchDetail(currentTaskId.value);
    }
  } catch (error) {
    ElMessage.error(error.message || '指派失败');
  } finally {
    assigning.value = false;
  }
}

async function handleCancel(row) {
  try {
    const { value } = await ElMessageBox.prompt('请输入取消原因', '取消订单', {
      confirmButtonText: '确认取消',
      cancelButtonText: '返回',
      inputPlaceholder: '例如：用户要求取消',
    });
    await cancelDispatchOrder(row.taskId, value);
    ElMessage.success('订单已取消');
    await loadRows();
    if (detailVisible.value && detail.value?.taskId === row.taskId) {
      detail.value = await fetchDispatchDetail(row.taskId);
    }
  } catch (error) {
    if (error === 'cancel' || error?.message === 'cancel') {
      return;
    }
    ElMessage.error(error.message || '取消订单失败');
  }
}

function connectSocket() {
  if (socket) {
    return;
  }
  socket = new WebSocket(buildAdminWsUrl('/ws/dispatch'));
  socket.addEventListener('message', async () => {
    await loadRows();
    if (detailVisible.value && detail.value?.taskId) {
      detail.value = await fetchDispatchDetail(detail.value.taskId);
    }
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

onMounted(async () => {
  await loadRows();
  connectSocket();
});

onBeforeUnmount(closeSocket);
</script>

<style scoped>
/* 页面局部样式 */
.action-row {
  display: flex;
  gap: 8px;
}

.drawer-section {
  margin-top: 20px;
}

.drawer-title {
  margin: 0 0 12px;
  font-size: 16px;
}

.tag-item {
  margin-right: 8px;
  margin-bottom: 8px;
}

.timeline-label {
  font-weight: 600;
}

.timeline-desc {
  margin-top: 4px;
  color: #6b7280;
}
</style>
