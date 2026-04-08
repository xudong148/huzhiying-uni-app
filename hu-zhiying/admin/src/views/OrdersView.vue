<template>
  <div class="page-panel">
    <!-- 页面标题区：说明订单页同时管理服务单和商品单 -->
    <h2 class="page-title">订单管理</h2>
    <p class="page-desc">统一查看服务单和商品单，支持打开详情、取消服务单和发起退款。</p>

    <!-- 订单列表区：展示后台订单总表和操作入口 -->
    <el-table :data="rows">
      <el-table-column prop="id" label="订单号" min-width="180" />
      <el-table-column prop="category" label="标题" min-width="220" />
      <el-table-column prop="status" label="业务状态" width="120" />
      <el-table-column prop="user" label="用户" width="120" />
      <el-table-column label="金额" width="120">
        <template #default="{ row }">¥{{ Number(row.amount || 0).toFixed(2) }}</template>
      </el-table-column>
      <el-table-column label="操作" width="240" fixed="right">
        <template #default="{ row }">
          <div class="action-row">
            <el-button size="small" @click="openDetail(row.id)">详情</el-button>
            <el-button size="small" type="warning" @click="handleCancel(row.id)">取消</el-button>
            <el-button size="small" type="danger" @click="handleRefund(row.id)">退款</el-button>
          </div>
        </template>
      </el-table-column>
    </el-table>

    <!-- 详情抽屉区：展示订单状态、报价、时间轴和轨迹 -->
    <el-drawer v-model="detailVisible" title="订单详情" size="640px">
      <template v-if="detail">
        <el-descriptions :column="1" border>
          <el-descriptions-item label="订单号">{{ detail.orderId }}</el-descriptions-item>
          <el-descriptions-item label="订单类型">{{ detail.orderType }}</el-descriptions-item>
          <el-descriptions-item label="订单标题">{{ detail.title }}</el-descriptions-item>
          <el-descriptions-item label="业务状态">{{ detail.status }}</el-descriptions-item>
          <el-descriptions-item label="支付状态">{{ detail.paymentStatus }}</el-descriptions-item>
          <el-descriptions-item label="用户">{{ detail.userName || '—' }}</el-descriptions-item>
          <el-descriptions-item label="服务地址">{{ detail.address || '—' }}</el-descriptions-item>
          <el-descriptions-item label="预约时间">{{ detail.appointment || '—' }}</el-descriptions-item>
          <el-descriptions-item label="ETA">{{ detail.eta || '—' }}</el-descriptions-item>
          <el-descriptions-item label="订单金额">¥{{ Number(detail.amount || 0).toFixed(2) }}</el-descriptions-item>
          <el-descriptions-item v-if="detail.installServiceOrderId" label="安装工单">
            {{ detail.installServiceOrderId }}
          </el-descriptions-item>
        </el-descriptions>

        <div v-if="detail.quotation" class="drawer-section">
          <h3 class="drawer-title">报价单</h3>
          <el-descriptions :column="1" border>
            <el-descriptions-item label="报价单号">{{ detail.quotation.quotationId }}</el-descriptions-item>
            <el-descriptions-item label="报价状态">{{ detail.quotation.status }}</el-descriptions-item>
            <el-descriptions-item label="报价备注">{{ detail.quotation.remark || '—' }}</el-descriptions-item>
            <el-descriptions-item label="报价总额">¥{{ Number(detail.quotation.totalAmount || 0).toFixed(2) }}</el-descriptions-item>
          </el-descriptions>
          <el-table :data="detail.quotation.items || []" size="small" class="sub-table">
            <el-table-column prop="name" label="项目" min-width="180" />
            <el-table-column label="金额" width="120">
              <template #default="{ row }">¥{{ Number(row.amount || 0).toFixed(2) }}</template>
            </el-table-column>
          </el-table>
        </div>

        <div v-if="detail.timeline?.length" class="drawer-section">
          <h3 class="drawer-title">履约时间轴</h3>
          <el-timeline>
            <el-timeline-item
              v-for="item in detail.timeline"
              :key="`${item.stepKey}-${item.time}`"
              :timestamp="item.time || ''"
              :type="item.done ? 'primary' : 'info'"
            >
              <div class="timeline-label">{{ item.label }}</div>
              <div class="timeline-desc">{{ item.description }}</div>
            </el-timeline-item>
          </el-timeline>
        </div>

        <div v-if="detail.trackPoints?.length" class="drawer-section">
          <h3 class="drawer-title">轨迹节点</h3>
          <el-table :data="detail.trackPoints" size="small" class="sub-table">
            <el-table-column prop="label" label="节点" width="120" />
            <el-table-column prop="description" label="说明" min-width="220" />
            <el-table-column prop="time" label="时间" width="180" />
          </el-table>
        </div>
      </template>
    </el-drawer>
  </div>
</template>

<script setup>
/**
 * 订单管理页面。
 * 1. 列表接口读取后台订单汇总。
 * 2. 详情抽屉读取后台详情接口，展示报价、时间轴和轨迹。
 * 3. 取消和退款动作统一走后台业务接口，并在动作后刷新列表与详情。
 */
import { onMounted, ref } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import {
  cancelAdminOrder,
  fetchAdminOrderDetail,
  fetchAdminOrders,
  refundAdminOrder,
} from '../api/request';

const rows = ref([]);
const detailVisible = ref(false);
const detail = ref(null);

async function loadRows() {
  rows.value = await fetchAdminOrders();
}

async function openDetail(orderId) {
  detail.value = await fetchAdminOrderDetail(orderId);
  detailVisible.value = true;
}

async function refreshDetailIfNeeded(orderId) {
  if (detailVisible.value && detail.value?.orderId === orderId) {
    detail.value = await fetchAdminOrderDetail(orderId);
  }
}

async function handleCancel(orderId) {
  try {
    const orderDetail = await fetchAdminOrderDetail(orderId);
    if (!orderDetail.canCancel) {
      ElMessage.warning('当前订单不允许取消');
      return;
    }
    const { value } = await ElMessageBox.prompt('请输入取消原因', '取消订单', {
      confirmButtonText: '确认取消',
      cancelButtonText: '返回',
      inputPlaceholder: '例如：用户要求取消',
    });
    await cancelAdminOrder(orderId, value);
    ElMessage.success('订单已取消');
    await loadRows();
    await refreshDetailIfNeeded(orderId);
  } catch (error) {
    if (error === 'cancel' || error?.message === 'cancel') {
      return;
    }
    ElMessage.error(error.message || '取消订单失败');
  }
}

async function handleRefund(orderId) {
  try {
    const orderDetail = await fetchAdminOrderDetail(orderId);
    if (!orderDetail.canRefund) {
      ElMessage.warning('当前订单不允许退款');
      return;
    }
    const { value } = await ElMessageBox.prompt('请输入退款备注', '订单退款', {
      confirmButtonText: '确认退款',
      cancelButtonText: '返回',
      inputPlaceholder: '例如：售后裁定退款',
    });
    await refundAdminOrder(orderId, value);
    ElMessage.success('退款状态已更新');
    await loadRows();
    await refreshDetailIfNeeded(orderId);
  } catch (error) {
    if (error === 'cancel' || error?.message === 'cancel') {
      return;
    }
    ElMessage.error(error.message || '退款失败');
  }
}

onMounted(loadRows);
</script>

<style scoped>
/* 页面局部样式：补充操作区和详情区布局 */
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

.timeline-label {
  font-weight: 600;
}

.timeline-desc {
  margin-top: 4px;
  color: #6b7280;
}
</style>
