<template>
  <section class="crud-console page-panel">
    <!-- 面板头部 -->
    <div class="crud-console__header">
      <div>
        <h3 class="page-title">{{ title }}</h3>
        <p class="page-desc">{{ description }}</p>
      </div>
      <el-button type="primary" @click="openCreate">新增</el-button>
    </div>

    <!-- 数据表格 -->
    <el-table v-loading="loading" :data="rows" row-key="id">
      <el-table-column
        v-for="column in columns"
        :key="column.prop"
        :prop="column.prop"
        :label="column.label"
        :min-width="column.minWidth || 140"
        :width="column.width"
      >
        <template #default="{ row }">
          <span v-if="column.type === 'switch'">
            <el-tag :type="row[column.prop] ? 'success' : 'info'">
              {{ row[column.prop] ? '启用' : '停用' }}
            </el-tag>
          </span>
          <span v-else-if="typeof column.formatter === 'function'">
            {{ column.formatter(row) }}
          </span>
          <span v-else>
            {{ row[column.prop] }}
          </span>
        </template>
      </el-table-column>

      <el-table-column label="操作" width="180" fixed="right">
        <template #default="{ row }">
          <div class="crud-console__actions">
            <el-button link type="primary" @click="openEdit(row)">编辑</el-button>
            <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
          </div>
        </template>
      </el-table-column>
    </el-table>

    <!-- 新增 / 编辑弹窗 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" :width="width">
      <el-form label-width="116px">
        <el-form-item v-for="field in fields" :key="field.prop" :label="field.label">
          <el-input
            v-if="field.type === 'text'"
            v-model="form[field.prop]"
            :placeholder="field.placeholder || `请输入${field.label}`"
          />

          <el-input
            v-else-if="field.type === 'textarea'"
            v-model="form[field.prop]"
            type="textarea"
            :rows="field.rows || 4"
            :placeholder="field.placeholder || `请输入${field.label}`"
          />

          <el-input-number
            v-else-if="field.type === 'number'"
            v-model="form[field.prop]"
            :min="field.min ?? 0"
            :max="field.max ?? 999999"
            :precision="field.precision ?? 0"
            class="crud-console__number"
          />

          <el-switch v-else-if="field.type === 'switch'" v-model="form[field.prop]" />

          <el-select
            v-else-if="field.type === 'select'"
            v-model="form[field.prop]"
            class="crud-console__select"
            :placeholder="field.placeholder || `请选择${field.label}`"
          >
            <el-option
              v-for="option in field.options || []"
              :key="option.value"
              :label="option.label"
              :value="option.value"
            />
          </el-select>

          <el-input
            v-else
            v-model="form[field.prop]"
            :placeholder="field.placeholder || `请输入${field.label}`"
          />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="submit">保存</el-button>
      </template>
    </el-dialog>
  </section>
</template>

<script setup>
/**
 * 通用后台 CRUD 面板。
 * 约定：
 * 1. 每个资源都具备 list/detail/create/update/delete 五类接口。
 * 2. 字段渲染由 fields / columns 描述驱动，避免每个页面重复写表格和表单。
 */
import { computed, onMounted, ref } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import {
  createCrudItem,
  deleteCrudItem,
  fetchCrudDetail,
  fetchCrudList,
  updateCrudItem,
} from '../api/request';

const props = defineProps({
  title: { type: String, required: true },
  description: { type: String, default: '' },
  resource: { type: String, required: true },
  columns: { type: Array, default: () => [] },
  fields: { type: Array, default: () => [] },
  width: { type: String, default: '760px' },
});
const emit = defineEmits(['changed']);

// 列表、弹窗和表单状态。
const loading = ref(false);
const saving = ref(false);
const dialogVisible = ref(false);
const editingId = ref(null);
const rows = ref([]);
const form = ref({});

const dialogTitle = computed(() => (editingId.value ? `编辑${props.title}` : `新增${props.title}`));

function getDefaultValue(field) {
  if (field.default !== undefined) {
    return field.default;
  }
  if (field.type === 'switch') {
    return true;
  }
  if (field.type === 'number') {
    return 0;
  }
  return '';
}

function buildForm(source = {}) {
  return props.fields.reduce((accumulator, field) => {
    accumulator[field.prop] = source[field.prop] ?? getDefaultValue(field);
    return accumulator;
  }, {});
}

async function loadRows() {
  loading.value = true;
  try {
    rows.value = await fetchCrudList(props.resource);
  } finally {
    loading.value = false;
  }
}

function openCreate() {
  editingId.value = null;
  form.value = buildForm();
  dialogVisible.value = true;
}

async function openEdit(row) {
  loading.value = true;
  try {
    const detail = await fetchCrudDetail(props.resource, row.id);
    editingId.value = row.id;
    form.value = buildForm(detail);
    dialogVisible.value = true;
  } finally {
    loading.value = false;
  }
}

async function submit() {
  saving.value = true;
  try {
    if (editingId.value) {
      await updateCrudItem(props.resource, editingId.value, form.value);
      ElMessage.success('保存成功');
    } else {
      await createCrudItem(props.resource, form.value);
      ElMessage.success('新增成功');
    }
    dialogVisible.value = false;
    await loadRows();
    emit('changed');
  } catch (error) {
    ElMessage.error(error.message || '保存失败');
  } finally {
    saving.value = false;
  }
}

async function handleDelete(row) {
  try {
    await ElMessageBox.confirm(
      `确认删除“${row.title || row.name || row.code || row.id}”吗？`,
      '删除确认',
      { type: 'warning' },
    );
    await deleteCrudItem(props.resource, row.id);
    ElMessage.success('删除成功');
    await loadRows();
    emit('changed');
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error(error.message || '删除失败');
    }
  }
}

// 页面加载后立即查询真实数据。
onMounted(loadRows);
</script>

<style scoped>
/* 布局与操作区 */
.crud-console + .crud-console {
  margin-top: 20px;
}

.crud-console__header {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 20px;
  margin-bottom: 18px;
}

.crud-console__actions {
  display: flex;
  align-items: center;
  gap: 12px;
}

.crud-console__number,
.crud-console__select {
  width: 100%;
}
</style>
