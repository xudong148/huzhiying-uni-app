<template>
  <div class="page-panel">
    <!-- 页面标题区：说明当前页负责师傅资料、启停用和信用分管理 -->
    <h2 class="page-title">师傅管理</h2>
    <p class="page-desc">查看师傅资料、编辑技能和服务区域，并支持启停用与信用分调整。</p>

    <!-- 师傅列表区：展示基础资料和常用动作 -->
    <el-table :data="rows">
      <el-table-column prop="name" label="师傅" width="120" />
      <el-table-column prop="skills" label="技能标签" min-width="220" />
      <el-table-column prop="area" label="服务区域" width="160" />
      <el-table-column prop="status" label="在线状态" width="120" />
      <el-table-column prop="score" label="信用分" width="100" />
      <el-table-column label="保证金" width="120">
        <template #default="{ row }">¥{{ Number(row.deposit || 0).toFixed(0) }}</template>
      </el-table-column>
      <el-table-column label="操作" width="320" fixed="right">
        <template #default="{ row }">
          <div class="action-row">
            <el-button size="small" @click="openDetail(row.userId)">详情</el-button>
            <el-button size="small" type="primary" @click="openEdit(row.userId)">编辑</el-button>
            <el-button size="small" type="warning" @click="openCredit(row.userId)">信用分</el-button>
            <el-button size="small" type="success" @click="handleEnable(row.userId)">启用</el-button>
            <el-button size="small" type="danger" @click="handleDisable(row.userId)">停用</el-button>
          </div>
        </template>
      </el-table-column>
    </el-table>

    <!-- 详情抽屉区：展示师傅完整资料 -->
    <el-drawer v-model="detailVisible" title="师傅详情" size="560px">
      <template v-if="detail">
        <el-descriptions :column="1" border>
          <el-descriptions-item label="用户 ID">{{ detail.userId }}</el-descriptions-item>
          <el-descriptions-item label="师傅姓名">{{ detail.realName }}</el-descriptions-item>
          <el-descriptions-item label="手机号">{{ detail.mobile }}</el-descriptions-item>
          <el-descriptions-item label="技能标签">
            <el-tag v-for="tag in detail.skillTags || []" :key="tag" class="tag-item">{{ tag }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="服务区域">
            <el-tag v-for="area in detail.serviceAreas || []" :key="area" class="tag-item" type="success">{{ area }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="保证金">¥{{ Number(detail.deposit || 0).toFixed(2) }}</el-descriptions-item>
          <el-descriptions-item label="信用分">{{ detail.creditScore }}</el-descriptions-item>
          <el-descriptions-item label="是否在线">{{ detail.online ? '是' : '否' }}</el-descriptions-item>
          <el-descriptions-item label="听单状态">{{ detail.listening ? '开启' : '关闭' }}</el-descriptions-item>
          <el-descriptions-item label="最大接单距离">{{ detail.maxDistanceKm }} km</el-descriptions-item>
          <el-descriptions-item label="隐私号">{{ detail.privacyNumber ? '开启' : '关闭' }}</el-descriptions-item>
          <el-descriptions-item label="启用状态">{{ detail.enabled ? '启用' : '停用' }}</el-descriptions-item>
        </el-descriptions>
      </template>
    </el-drawer>

    <!-- 编辑弹窗区：更新师傅基础资料和设置 -->
    <el-dialog v-model="editVisible" title="编辑师傅" width="520px">
      <el-form label-width="120px">
        <el-form-item label="师傅姓名">
          <el-input v-model="editForm.realName" />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="editForm.mobile" />
        </el-form-item>
        <el-form-item label="技能标签">
          <el-input v-model="editForm.skillTagsText" placeholder="用逗号分隔，例如：空调维修,智能锁安装" />
        </el-form-item>
        <el-form-item label="服务区域">
          <el-input v-model="editForm.serviceAreasText" placeholder="用逗号分隔，例如：浦东新区,徐汇区" />
        </el-form-item>
        <el-form-item label="保证金">
          <el-input-number v-model="editForm.deposit" :min="0" :precision="2" />
        </el-form-item>
        <el-form-item label="最大接单距离">
          <el-input-number v-model="editForm.maxDistanceKm" :min="1" :max="100" />
        </el-form-item>
        <el-form-item label="在线状态">
          <el-switch v-model="editForm.online" />
        </el-form-item>
        <el-form-item label="听单状态">
          <el-switch v-model="editForm.listening" />
        </el-form-item>
        <el-form-item label="隐私号">
          <el-switch v-model="editForm.privacyNumber" />
        </el-form-item>
        <el-form-item label="启用状态">
          <el-switch v-model="editForm.enabled" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="editVisible = false">取消</el-button>
        <el-button type="primary" :loading="submittingEdit" @click="submitEdit">保存</el-button>
      </template>
    </el-dialog>

    <!-- 信用分弹窗区：单独调整信用分，方便运营快速处理 -->
    <el-dialog v-model="creditVisible" title="调整信用分" width="360px">
      <el-form label-width="100px">
        <el-form-item label="信用分">
          <el-input-number v-model="creditScore" :min="0" :max="100" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="creditVisible = false">取消</el-button>
        <el-button type="primary" :loading="submittingCredit" @click="submitCredit">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
/**
 * 师傅管理页面。
 * 1. 列表接口读取后台师傅总表。
 * 2. 详情抽屉读取师傅详情接口。
 * 3. 编辑、启停用和信用分调整全部走后台真实接口。
 */
import { onMounted, reactive, ref } from 'vue';
import { ElMessage } from 'element-plus';
import {
  disableAdminMaster,
  enableAdminMaster,
  fetchAdminMasterDetail,
  fetchAdminMasters,
  updateAdminMaster,
  updateAdminMasterCredit,
} from '../api/request';

const rows = ref([]);
const detailVisible = ref(false);
const detail = ref(null);
const editVisible = ref(false);
const creditVisible = ref(false);
const currentUserId = ref(null);
const submittingEdit = ref(false);
const submittingCredit = ref(false);
const creditScore = ref(100);

const editForm = reactive({
  realName: '',
  mobile: '',
  skillTagsText: '',
  serviceAreasText: '',
  deposit: 0,
  online: false,
  listening: false,
  maxDistanceKm: 20,
  privacyNumber: true,
  enabled: true,
});

async function loadRows() {
  rows.value = await fetchAdminMasters();
}

async function openDetail(userId) {
  currentUserId.value = userId;
  detail.value = await fetchAdminMasterDetail(userId);
  detailVisible.value = true;
}

async function openEdit(userId) {
  const payload = await fetchAdminMasterDetail(userId);
  currentUserId.value = userId;
  editForm.realName = payload.realName || '';
  editForm.mobile = payload.mobile || '';
  editForm.skillTagsText = (payload.skillTags || []).join(', ');
  editForm.serviceAreasText = (payload.serviceAreas || []).join(', ');
  editForm.deposit = Number(payload.deposit || 0);
  editForm.online = !!payload.online;
  editForm.listening = !!payload.listening;
  editForm.maxDistanceKm = Number(payload.maxDistanceKm || 20);
  editForm.privacyNumber = !!payload.privacyNumber;
  editForm.enabled = !!payload.enabled;
  editVisible.value = true;
}

async function openCredit(userId) {
  const payload = await fetchAdminMasterDetail(userId);
  currentUserId.value = userId;
  creditScore.value = Number(payload.creditScore || 0);
  creditVisible.value = true;
}

async function refreshCurrentDetail(userId) {
  if (detailVisible.value && detail.value?.userId === userId) {
    detail.value = await fetchAdminMasterDetail(userId);
  }
}

async function submitEdit() {
  submittingEdit.value = true;
  try {
    await updateAdminMaster(currentUserId.value, {
      realName: editForm.realName,
      mobile: editForm.mobile,
      skillTags: splitText(editForm.skillTagsText),
      serviceAreas: splitText(editForm.serviceAreasText),
      deposit: editForm.deposit,
      online: editForm.online,
      listening: editForm.listening,
      maxDistanceKm: editForm.maxDistanceKm,
      privacyNumber: editForm.privacyNumber,
      enabled: editForm.enabled,
    });
    editVisible.value = false;
    ElMessage.success('师傅资料已更新');
    await loadRows();
    await refreshCurrentDetail(currentUserId.value);
  } catch (error) {
    ElMessage.error(error.message || '更新失败');
  } finally {
    submittingEdit.value = false;
  }
}

async function submitCredit() {
  submittingCredit.value = true;
  try {
    await updateAdminMasterCredit(currentUserId.value, creditScore.value);
    creditVisible.value = false;
    ElMessage.success('信用分已更新');
    await loadRows();
    await refreshCurrentDetail(currentUserId.value);
  } catch (error) {
    ElMessage.error(error.message || '更新信用分失败');
  } finally {
    submittingCredit.value = false;
  }
}

async function handleEnable(userId) {
  try {
    await enableAdminMaster(userId);
    ElMessage.success('师傅已启用');
    await loadRows();
    await refreshCurrentDetail(userId);
  } catch (error) {
    ElMessage.error(error.message || '启用失败');
  }
}

async function handleDisable(userId) {
  try {
    await disableAdminMaster(userId);
    ElMessage.success('师傅已停用');
    await loadRows();
    await refreshCurrentDetail(userId);
  } catch (error) {
    ElMessage.error(error.message || '停用失败');
  }
}

function splitText(value) {
  return String(value || '')
    .split(',')
    .map((item) => item.trim())
    .filter(Boolean);
}

onMounted(loadRows);
</script>

<style scoped>
/* 页面局部样式：补充按钮布局和标签间距 */
.action-row {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.tag-item {
  margin-right: 8px;
  margin-bottom: 8px;
}
</style>
