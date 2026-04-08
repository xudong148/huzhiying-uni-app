<template>
  <div class="page-panel">
    <!-- 页面标题区 -->
    <h2 class="page-title">订单管理</h2>
    <p class="page-desc">统一查看服务单和商品单，支持详情、取消、退款、发券和改预约。</p>

    <!-- 订单列表区 -->
    <el-table :data="rows">
      <el-table-column prop="id" label="订单号" min-width="180" />
      <el-table-column prop="category" label="标题" min-width="220" />
      <el-table-column prop="status" label="业务状态" width="120" />
      <el-table-column prop="user" label="用户" width="120" />
      <el-table-column label="金额" width="120">
        <template #default="{ row }">¥{{ Number(row.amount || 0).toFixed(2) }}</template>
      </el-table-column>
      <el-table-column label="操作" width="260" fixed="right">
        <template #default="{ row }">
          <div class="action-row">
            <el-button size="small" @click="openDetail(row.id)">详情</el-button>
            <el-button size="small" type="warning" @click="handleCancel(row.id)">取消</el-button>
            <el-button size="small" type="danger" @click="handleRefund(row.id)">退款</el-button>
          </div>
        </template>
      </el-table-column>
    </el-table>

    <!-- 详情抽屉 -->
    <el-drawer v-model="detailVisible" title="订单详情" size="720px">
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

        <div class="drawer-section">
          <div class="section-header">
            <h3 class="drawer-title">客服动作</h3>
            <div class="action-row">
              <el-button
                size="small"
                type="primary"
                :disabled="!detail.canGrantCoupon"
                @click="openGrantCoupon"
              >
                手工发券
              </el-button>
              <el-button
                size="small"
                type="success"
                :disabled="!detail.canUpdateAppointment"
                @click="openAppointmentDialog"
              >
                改预约
              </el-button>
            </div>
          </div>
          <el-descriptions :column="1" border>
            <el-descriptions-item label="聊天摘要">
              {{ detail.messageSummary?.latestMessage || '暂无聊天记录' }}
            </el-descriptions-item>
            <el-descriptions-item label="消息数量">
              {{ detail.messageSummary?.messageCount || 0 }}
            </el-descriptions-item>
            <el-descriptions-item label="会话标题">
              {{ detail.messageSummary?.title || '—' }}
            </el-descriptions-item>
          </el-descriptions>
        </div>

        <div v-if="detail.mediaItems?.length" class="drawer-section">
          <h3 class="drawer-title">媒体摘要</h3>
          <el-table :data="detail.mediaItems" size="small" class="sub-table">
            <el-table-column prop="bizType" label="业务类型" width="160" />
            <el-table-column prop="originalName" label="文件名" min-width="220" />
            <el-table-column prop="url" label="访问地址" min-width="220" />
          </el-table>
        </div>

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

    <!-- 发券弹窗 -->
    <el-dialog v-model="couponVisible" title="手工发券" width="420px">
      <el-form label-width="90px">
        <el-form-item label="优惠券 ID">
          <el-input-number v-model="couponForm.couponId" :min="1" controls-position="right" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="couponForm.remark" placeholder="例如：客服补偿" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="couponVisible = false">取消</el-button>
        <el-button type="primary" :loading="couponLoading" @click="submitGrantCoupon">确认发券</el-button>
      </template>
    </el-dialog>

    <!-- 改预约弹窗 -->
    <el-dialog v-model="appointmentVisible" title="修改预约" width="420px">
      <el-form label-width="90px">
        <el-form-item label="预约时间">
          <el-input v-model="appointmentForm.appointment" placeholder="例如：明天 10:00-12:00" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="appointmentVisible = false">取消</el-button>
        <el-button type="primary" :loading="appointmentLoading" @click="submitAppointment">确认修改</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
/**
 * 订单管理页面。
 * 1. 列表接口读取后台订单汇总。
 * 2. 详情抽屉读取后台详情接口，展示报价、聊天、媒体和轨迹。
 * 3. 取消、退款、发券和改预约统一走后台真实接口，并在动作后刷新列表与详情。
 */
import { onMounted, reactive, ref } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import {
  cancelAdminOrder,
  fetchAdminOrderDetail,
  fetchAdminOrders,
  grantAdminCoupon,
  refundAdminOrder,
  updateAdminOrderAppointment,
} from '../api/request';

const rows = ref([]);
const detailVisible = ref(false);
const detail = ref(null);
const couponVisible = ref(false);
const appointmentVisible = ref(false);
const couponLoading = ref(false);
const appointmentLoading = ref(false);

const couponForm = reactive({
  couponId: 1,
  remark: '',
});

const appointmentForm = reactive({
  appointment: '',
});

async function loadRows() {
  rows.value = await fetchAdminOrders();
}

async function openDetail(orderId) {
  detail.value = await fetchAdminOrderDetail(orderId);
  appointmentForm.appointment = detail.value.appointment || '';
  detailVisible.value = true;
}

async function refreshDetailIfNeeded(orderId) {
  if (detailVisible.value && detail.value?.orderId === orderId) {
    detail.value = await fetchAdminOrderDetail(orderId);
    appointmentForm.appointment = detail.value.appointment || '';
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

function openGrantCoupon() {
  couponForm.remark = '';
  couponVisible.value = true;
}

function openAppointmentDialog() {
  appointmentForm.appointment = detail.value?.appointment || '';
  appointmentVisible.value = true;
}

async function submitGrantCoupon() {
  if (!detail.value?.orderId) {
    return;
  }
  couponLoading.value = true;
  try {
    await grantAdminCoupon(detail.value.orderId, {
      couponId: couponForm.couponId || null,
      remark: couponForm.remark,
    });
    couponVisible.value = false;
    ElMessage.success('优惠券已发放');
    await loadRows();
    await refreshDetailIfNeeded(detail.value.orderId);
  } catch (error) {
    ElMessage.error(error.message || '发券失败');
  } finally {
    couponLoading.value = false;
  }
}

async function submitAppointment() {
  if (!detail.value?.orderId) {
    return;
  }
  appointmentLoading.value = true;
  try {
    await updateAdminOrderAppointment(detail.value.orderId, appointmentForm.appointment);
    appointmentVisible.value = false;
    ElMessage.success('预约时间已更新');
    await loadRows();
    await refreshDetailIfNeeded(detail.value.orderId);
  } catch (error) {
    ElMessage.error(error.message || '改预约失败');
  } finally {
    appointmentLoading.value = false;
  }
}

onMounted(loadRows);
</script>

<style scoped>
/* 页面局部样式 */
.action-row {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.drawer-section {
  margin-top: 20px;
}

.section-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
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
