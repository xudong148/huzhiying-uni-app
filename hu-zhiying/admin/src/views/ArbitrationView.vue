<template>
  <div class="page-panel">
    <!-- 页面标题区：说明当前页负责仲裁工单与裁决 -->
    <h2 class="page-title">仲裁中心</h2>
    <p class="page-desc">查看用户投诉与履约争议，支持打开详情并提交裁决结果。</p>

    <!-- 仲裁列表区：展示仲裁工单与常用动作 -->
    <el-table :data="rows">
      <el-table-column prop="id" label="仲裁号" min-width="160" />
      <el-table-column prop="orderId" label="关联订单" min-width="180" />
      <el-table-column prop="reason" label="原因" min-width="220" />
      <el-table-column prop="status" label="状态" width="120" />
      <el-table-column label="操作" width="180" fixed="right">
        <template #default="{ row }">
          <div class="action-row">
            <el-button size="small" @click="openDetail(row.id)">详情</el-button>
            <el-button size="small" type="primary" @click="openResolve(row.id)">裁决</el-button>
          </div>
        </template>
      </el-table-column>
    </el-table>

    <!-- 详情抽屉区：展示关联订单、轨迹和媒体摘要 -->
    <el-drawer v-model="detailVisible" title="仲裁详情" size="620px">
      <template v-if="detail">
        <el-descriptions :column="1" border>
          <el-descriptions-item label="仲裁号">{{ detail.id }}</el-descriptions-item>
          <el-descriptions-item label="关联订单">{{ detail.orderId }}</el-descriptions-item>
          <el-descriptions-item label="订单标题">{{ detail.orderTitle || '—' }}</el-descriptions-item>
          <el-descriptions-item label="仲裁原因">{{ detail.reason }}</el-descriptions-item>
          <el-descriptions-item label="当前状态">{{ detail.status }}</el-descriptions-item>
          <el-descriptions-item label="处理结果">{{ detail.result || '—' }}</el-descriptions-item>
          <el-descriptions-item label="聊天消息数">{{ detail.messageCount }}</el-descriptions-item>
        </el-descriptions>

        <div v-if="detail.trackPoints?.length" class="drawer-section">
          <h3 class="drawer-title">轨迹摘要</h3>
          <el-table :data="detail.trackPoints" size="small" class="sub-table">
            <el-table-column prop="label" label="节点" width="120" />
            <el-table-column prop="description" label="说明" min-width="220" />
            <el-table-column prop="time" label="时间" width="180" />
          </el-table>
        </div>

        <div v-if="detail.mediaItems?.length" class="drawer-section">
          <h3 class="drawer-title">媒体摘要</h3>
          <el-table :data="detail.mediaItems" size="small" class="sub-table">
            <el-table-column prop="bizType" label="业务类型" width="160" />
            <el-table-column prop="originalName" label="文件名" min-width="220" />
            <el-table-column prop="url" label="访问地址" min-width="220" />
          </el-table>
        </div>
      </template>
    </el-drawer>

    <!-- 裁决弹窗区：填写裁决状态和结果说明 -->
    <el-dialog v-model="resolveVisible" title="提交裁决" width="420px">
      <el-form label-width="100px">
        <el-form-item label="状态文案">
          <el-input v-model="resolveForm.statusText" placeholder="例如：已裁决" />
        </el-form-item>
        <el-form-item label="裁决结果">
          <el-input
            v-model="resolveForm.resultText"
            type="textarea"
            :rows="4"
            placeholder="例如：判定退还增项费用"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="resolveVisible = false">取消</el-button>
        <el-button type="primary" :loading="resolving" @click="submitResolve">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
/**
 * 仲裁中心页面。
 * 1. 列表读取后台仲裁总表。
 * 2. 详情抽屉读取后台仲裁详情接口，展示轨迹和媒体摘要。
 * 3. 裁决表单调用后台裁决接口并刷新列表与详情。
 */
import { onMounted, reactive, ref } from 'vue';
import { ElMessage } from 'element-plus';
import {
  fetchAdminArbitrations,
  fetchArbitrationDetail,
  resolveArbitrationCase,
} from '../api/request';

const rows = ref([]);
const detailVisible = ref(false);
const detail = ref(null);
const resolveVisible = ref(false);
const resolving = ref(false);
const currentId = ref('');
const resolveForm = reactive({
  statusText: '已裁决',
  resultText: '',
});

async function loadRows() {
  rows.value = await fetchAdminArbitrations();
}

async function openDetail(id) {
  currentId.value = id;
  detail.value = await fetchArbitrationDetail(id);
  detailVisible.value = true;
}

async function openResolve(id) {
  const payload = await fetchArbitrationDetail(id);
  currentId.value = id;
  resolveForm.statusText = payload.status === '待裁决' ? '已裁决' : payload.status;
  resolveForm.resultText = payload.result || '';
  resolveVisible.value = true;
}

async function refreshDetailIfNeeded(id) {
  if (detailVisible.value && detail.value?.id === id) {
    detail.value = await fetchArbitrationDetail(id);
  }
}

async function submitResolve() {
  resolving.value = true;
  try {
    await resolveArbitrationCase(currentId.value, {
      statusText: resolveForm.statusText,
      resultText: resolveForm.resultText,
    });
    resolveVisible.value = false;
    ElMessage.success('仲裁结果已保存');
    await loadRows();
    await refreshDetailIfNeeded(currentId.value);
  } catch (error) {
    ElMessage.error(error.message || '保存裁决失败');
  } finally {
    resolving.value = false;
  }
}

onMounted(loadRows);
</script>

<style scoped>
/* 页面局部样式：补充操作区和详情内容布局 */
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

.sub-table {
  margin-top: 12px;
}
</style>
