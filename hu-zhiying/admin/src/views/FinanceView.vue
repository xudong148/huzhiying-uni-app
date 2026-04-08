<template>
  <div class="page-panel">
    <!-- 页面标题区 -->
    <h2 class="page-title">财务结算</h2>
    <p class="page-desc">查看师傅结算单和退款单，支持打开详情并执行审核动作。</p>

    <!-- 财务列表区 -->
    <el-table :data="rows">
      <el-table-column prop="billNo" label="账单号" min-width="180" />
      <el-table-column prop="type" label="类型" width="120" />
      <el-table-column label="金额" width="120">
        <template #default="{ row }">¥{{ Number(row.amount || 0).toFixed(2) }}</template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="120" />
      <el-table-column label="操作" width="180" fixed="right">
        <template #default="{ row }">
          <div class="action-row">
            <el-button size="small" @click="openDetail(row.billNo)">详情</el-button>
            <el-button size="small" type="primary" @click="handleApprove(row.billNo)">审核</el-button>
          </div>
        </template>
      </el-table-column>
    </el-table>

    <!-- 详情抽屉 -->
    <el-drawer v-model="detailVisible" title="财务单详情" size="520px">
      <template v-if="detail">
        <el-descriptions :column="1" border>
          <el-descriptions-item label="账单号">{{ detail.billNo }}</el-descriptions-item>
          <el-descriptions-item label="账单类型">{{ detail.type }}</el-descriptions-item>
          <el-descriptions-item label="当前状态">{{ detail.status }}</el-descriptions-item>
          <el-descriptions-item label="金额">¥{{ Number(detail.amount || 0).toFixed(2) }}</el-descriptions-item>
          <el-descriptions-item label="关联订单">{{ detail.orderId || '—' }}</el-descriptions-item>
          <el-descriptions-item label="账单标题">{{ detail.title || '—' }}</el-descriptions-item>
          <el-descriptions-item label="关联师傅">{{ detail.masterName || '—' }}</el-descriptions-item>
          <el-descriptions-item label="业务时间">{{ detail.transactionTime || '—' }}</el-descriptions-item>
          <el-descriptions-item label="审核备注">{{ detail.remark || '—' }}</el-descriptions-item>
        </el-descriptions>
      </template>
    </el-drawer>
  </div>
</template>

<script setup>
/**
 * 财务结算页面。
 * 1. 列表从后台财务接口读取。
 * 2. 详情抽屉读取财务单详情接口。
 * 3. 审核动作调用后台真实审核接口并刷新列表与详情。
 */
import { onMounted, ref } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import {
  approveFinanceBill,
  fetchAdminFinance,
  fetchFinanceDetail,
} from '../api/request';

const rows = ref([]);
const detailVisible = ref(false);
const detail = ref(null);

async function loadRows() {
  rows.value = await fetchAdminFinance();
}

async function openDetail(billNo) {
  detail.value = await fetchFinanceDetail(billNo);
  detailVisible.value = true;
}

async function refreshDetailIfNeeded(billNo) {
  if (detailVisible.value && detail.value?.billNo === billNo) {
    detail.value = await fetchFinanceDetail(billNo);
  }
}

async function handleApprove(billNo) {
  try {
    const { value } = await ElMessageBox.prompt('请输入审核备注', '财务审核', {
      confirmButtonText: '确认审核',
      cancelButtonText: '返回',
      inputPlaceholder: '例如：财务审核通过',
    });
    await approveFinanceBill(billNo, value);
    ElMessage.success('财务单已审核');
    await loadRows();
    await refreshDetailIfNeeded(billNo);
  } catch (error) {
    if (error === 'cancel' || error?.message === 'cancel') {
      return;
    }
    ElMessage.error(error.message || '审核失败');
  }
}

onMounted(loadRows);
</script>

<style scoped>
/* 页面局部样式 */
.action-row {
  display: flex;
  gap: 8px;
}
</style>
