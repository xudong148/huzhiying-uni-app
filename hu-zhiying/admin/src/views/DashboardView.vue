<template>
  <div>
    <!-- 核心指标 -->
    <div class="page-panel">
      <h2 class="page-title">平台总览</h2>
      <p class="page-desc">实时查看 GMV、订单体量、在线师傅与异常预警。</p>
      <div class="dashboard-grid">
        <kpi-card label="GMV" :value="`¥${Number(data.gmv || 0).toLocaleString()}`" />
        <kpi-card label="服务订单" :value="data.serviceOrders" />
        <kpi-card label="商品订单" :value="data.productOrders" />
        <kpi-card label="在线师傅" :value="data.onlineMasters" />
      </div>
    </div>

    <!-- 风险提醒 -->
    <div class="page-panel dashboard-alert">
      <div>
        <h3 class="page-title">风险提醒</h3>
        <p class="page-desc">当前共有 {{ data.warning }} 条仲裁或退款异常待处理。</p>
      </div>
      <router-link class="dashboard-alert__link" to="/arbitration">进入仲裁中心</router-link>
    </div>

    <!-- 通知任务 -->
    <div class="page-panel dashboard-notifications">
      <div class="dashboard-notifications__header">
        <div>
          <h3 class="page-title">通知任务</h3>
          <p class="page-desc">查看待发送通知并支持手动触发派发，覆盖结算、退款和订单状态更新。</p>
        </div>
        <div class="dashboard-notifications__actions">
          <el-tag type="info">待发送 {{ notificationSummary.pending }}</el-tag>
          <el-tag type="success">已发送 {{ notificationSummary.sent }}</el-tag>
          <el-tag type="danger">失败 {{ notificationSummary.failed }}</el-tag>
          <el-button type="primary" :loading="dispatching" @click="handleDispatch">
            立即派发
          </el-button>
        </div>
      </div>

      <el-table v-if="notificationTasks.length" :data="notificationTasks">
        <el-table-column prop="bizNo" label="业务号" min-width="220" />
        <el-table-column prop="templateCode" label="模板" min-width="180" />
        <el-table-column prop="targetRole" label="目标角色" width="120" />
        <el-table-column prop="status" label="状态" width="120" />
        <el-table-column prop="updatedAt" label="最近更新时间" min-width="180" />
      </el-table>
      <el-empty v-else description="当前没有待关注的通知任务" />
    </div>
  </div>
</template>

<script setup>
/**
 * 仪表盘页面。
 * 展示后台核心经营指标、待处理风险和通知任务执行情况。
 */
import { computed, onMounted, reactive, ref } from 'vue';
import { ElMessage } from 'element-plus';
import KpiCard from '../components/KpiCard.vue';
import {
  dispatchAdminNotifications,
  fetchAdminDashboard,
  fetchAdminNotifications,
} from '../api/request';

const data = reactive({
  gmv: 0,
  serviceOrders: 0,
  productOrders: 0,
  onlineMasters: 0,
  warning: 0,
});
const notificationTasks = ref([]);
const dispatching = ref(false);

const notificationSummary = computed(() => notificationTasks.value.reduce((summary, item) => {
  const status = String(item.status || '').toUpperCase();
  if (status === 'SENT') {
    summary.sent += 1;
  } else if (status === 'FAILED') {
    summary.failed += 1;
  } else {
    summary.pending += 1;
  }
  return summary;
}, {
  pending: 0,
  sent: 0,
  failed: 0,
}));

async function loadDashboard() {
  const [dashboard, tasks] = await Promise.all([
    fetchAdminDashboard(),
    fetchAdminNotifications(),
  ]);
  Object.assign(data, dashboard);
  notificationTasks.value = tasks;
}

async function handleDispatch() {
  dispatching.value = true;
  try {
    const result = await dispatchAdminNotifications();
    await loadDashboard();
    ElMessage.success(`已处理 ${result.processedCount} 条通知任务`);
  } catch (error) {
    ElMessage.error(error.message || '通知派发失败');
  } finally {
    dispatching.value = false;
  }
}

onMounted(async () => {
  await loadDashboard();
});
</script>

<style scoped>
/* 指标与提醒布局 */
.dashboard-grid {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 16px;
}

.dashboard-alert {
  margin-top: 20px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 24px;
}

.dashboard-alert__link {
  color: #2b5cff;
  font-weight: 700;
  text-decoration: none;
}

.dashboard-notifications {
  margin-top: 20px;
}

.dashboard-notifications__header {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 20px;
  margin-bottom: 16px;
}

.dashboard-notifications__actions {
  display: flex;
  align-items: center;
  gap: 8px;
  flex-wrap: wrap;
  justify-content: flex-end;
}
</style>
