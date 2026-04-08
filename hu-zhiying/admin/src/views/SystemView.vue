<template>
  <div class="system-page">
    <crud-console
      title="角色配置"
      description="维护后台角色编码、名称和职责描述。"
      resource="/api/admin/system/roles"
      :columns="roleColumns"
      :fields="roleFields"
      @changed="loadGrantDictionaries"
    />

    <crud-console
      title="菜单配置"
      description="维护后台导航菜单路径、图标和排序。"
      resource="/api/admin/system/menus"
      :columns="menuColumns"
      :fields="menuFields"
      @changed="loadGrantDictionaries"
    />

    <crud-console
      title="权限点"
      description="维护订单强派、退款审核等细粒度权限点。"
      resource="/api/admin/system/permissions"
      :columns="permissionColumns"
      :fields="permissionFields"
      width="820px"
      @changed="loadGrantDictionaries"
    />

    <el-card shadow="never" class="grant-card">
      <template #header>
        <div class="grant-card__header">
          <div>
            <div class="grant-card__title">角色授权</div>
            <div class="grant-card__desc">把菜单和权限点真正绑定到角色，后台登录后的导航和可操作能力都从这里生效。</div>
          </div>
          <el-button type="primary" :loading="grantSaving" @click="handleSaveGrant">保存授权</el-button>
        </div>
      </template>

      <el-form label-position="top" class="grant-form">
        <el-form-item label="选择角色">
          <el-select v-model="currentRoleId" placeholder="请选择角色" class="grant-form__select" @change="loadRoleGrant">
            <el-option v-for="role in roles" :key="role.id" :label="role.name" :value="role.id" />
          </el-select>
        </el-form-item>

        <el-form-item label="可见菜单">
          <el-select v-model="grantForm.menuIds" multiple collapse-tags collapse-tags-tooltip class="grant-form__select">
            <el-option v-for="menu in menus" :key="menu.id" :label="`${menu.name} (${menu.path})`" :value="menu.id" />
          </el-select>
        </el-form-item>

        <el-form-item label="权限点">
          <el-select v-model="grantForm.permissionIds" multiple collapse-tags collapse-tags-tooltip class="grant-form__select">
            <el-option
              v-for="permission in permissions"
              :key="permission.id"
              :label="`${permission.name} (${permission.code})`"
              :value="permission.id"
            />
          </el-select>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import { ElMessage } from 'element-plus';
import CrudConsole from '../components/CrudConsole.vue';
import { fetchCrudList, fetchRoleGrant, saveRoleGrant } from '../api/request';

const lowerCodePattern = /^[a-z][a-z0-9_-]*$/;
const menuPathPattern = /^\/.*/;
const permissionCodePattern = /^[a-z][a-z0-9_-]*:[a-z][a-z0-9_-]*$/;

const roles = ref([]);
const menus = ref([]);
const permissions = ref([]);
const currentRoleId = ref(null);
const grantSaving = ref(false);
const grantForm = ref({
  menuIds: [],
  permissionIds: [],
});

const roleColumns = [
  { prop: 'code', label: '角色编码', width: 160 },
  { prop: 'name', label: '角色名称', minWidth: 180 },
  { prop: 'description', label: '职责说明', minWidth: 260 },
  { prop: 'enabled', label: '状态', width: 100, type: 'switch' },
];

const roleFields = [
  {
    prop: 'code',
    label: '角色编码',
    type: 'text',
    required: true,
    pattern: lowerCodePattern,
    patternMessage: '角色编码仅支持小写字母、数字、- 和 _',
    hint: '示例：admin、dispatcher、finance',
  },
  { prop: 'name', label: '角色名称', type: 'text', required: true },
  { prop: 'description', label: '职责说明', type: 'textarea', rows: 4 },
  { prop: 'enabled', label: '启用', type: 'switch', default: true },
];

const menuColumns = [
  { prop: 'name', label: '菜单名称', minWidth: 180 },
  { prop: 'path', label: '路由路径', minWidth: 180 },
  { prop: 'icon', label: '图标', minWidth: 180 },
  { prop: 'sortOrder', label: '排序', width: 100 },
  { prop: 'enabled', label: '状态', width: 100, type: 'switch' },
];

const menuFields = [
  { prop: 'name', label: '菜单名称', type: 'text', required: true },
  {
    prop: 'path',
    label: '路由路径',
    type: 'text',
    required: true,
    pattern: menuPathPattern,
    patternMessage: '菜单路径必须以 / 开头',
    hint: '示例：/dashboard、/pricing',
  },
  { prop: 'icon', label: '图标', type: 'text' },
  { prop: 'sortOrder', label: '排序', type: 'number', min: 0, required: true },
  { prop: 'enabled', label: '启用', type: 'switch', default: true },
];

const permissionColumns = [
  { prop: 'code', label: '权限编码', width: 220 },
  { prop: 'name', label: '权限名称', minWidth: 180 },
  { prop: 'description', label: '描述', minWidth: 260 },
  { prop: 'enabled', label: '状态', width: 100, type: 'switch' },
];

const permissionFields = [
  {
    prop: 'code',
    label: '权限编码',
    type: 'text',
    required: true,
    pattern: permissionCodePattern,
    patternMessage: '权限编码必须符合 module:action 格式',
    hint: '示例：dispatch:force-assign、finance:refund-audit',
  },
  { prop: 'name', label: '权限名称', type: 'text', required: true },
  { prop: 'description', label: '权限描述', type: 'textarea', rows: 4 },
  { prop: 'enabled', label: '启用', type: 'switch', default: true },
];

async function loadGrantDictionaries() {
  const [roleRows, menuRows, permissionRows] = await Promise.all([
    fetchCrudList('/api/admin/system/roles'),
    fetchCrudList('/api/admin/system/menus'),
    fetchCrudList('/api/admin/system/permissions'),
  ]);
  roles.value = roleRows.filter((item) => item.enabled);
  menus.value = menuRows.filter((item) => item.enabled);
  permissions.value = permissionRows.filter((item) => item.enabled);

  if (!currentRoleId.value && roles.value.length > 0) {
    currentRoleId.value = roles.value[0].id;
  }
  if (currentRoleId.value) {
    await loadRoleGrant();
  }
}

async function loadRoleGrant() {
  if (!currentRoleId.value) {
    grantForm.value = { menuIds: [], permissionIds: [] };
    return;
  }
  const payload = await fetchRoleGrant(currentRoleId.value);
  grantForm.value = {
    menuIds: Array.isArray(payload.menuIds) ? payload.menuIds : [],
    permissionIds: Array.isArray(payload.permissionIds) ? payload.permissionIds : [],
  };
}

async function handleSaveGrant() {
  if (!currentRoleId.value) {
    ElMessage.warning('请先选择角色');
    return;
  }
  grantSaving.value = true;
  try {
    const payload = await saveRoleGrant(currentRoleId.value, {
      roleId: currentRoleId.value,
      menuIds: grantForm.value.menuIds,
      permissionIds: grantForm.value.permissionIds,
    });
    grantForm.value = {
      menuIds: payload.menuIds || [],
      permissionIds: payload.permissionIds || [],
    };
    ElMessage.success('角色授权已保存');
  } catch (error) {
    ElMessage.error(error.message || '角色授权保存失败');
  } finally {
    grantSaving.value = false;
  }
}

onMounted(async () => {
  try {
    await loadGrantDictionaries();
  } catch (error) {
    ElMessage.error(error.message || '角色授权字典加载失败');
  }
});
</script>

<style scoped>
.system-page {
  display: grid;
  gap: 24px;
}

.grant-card__header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 24px;
}

.grant-card__title {
  font-size: 18px;
  font-weight: 700;
  color: #101828;
}

.grant-card__desc {
  margin-top: 6px;
  color: #667085;
  line-height: 1.6;
}

.grant-form {
  display: grid;
  gap: 6px;
}

.grant-form__select {
  width: 100%;
}
</style>
