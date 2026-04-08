<template>
  <section class="crud-console page-panel">
    <div class="crud-console__header">
      <div>
        <h3 class="page-title">{{ title }}</h3>
        <p class="page-desc">{{ description }}</p>
      </div>
      <el-button type="primary" @click="openCreate">新增</el-button>
    </div>

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

    <el-dialog v-model="dialogVisible" :title="dialogTitle" :width="width" @closed="handleDialogClosed">
      <el-alert
        v-if="submitError"
        :title="submitError"
        type="error"
        :closable="false"
        show-icon
        class="crud-console__alert"
      />

      <el-form ref="formRef" :model="form" :rules="formRules" label-width="116px" status-icon>
        <el-form-item v-for="field in fields" :key="field.prop" :label="field.label" :prop="field.prop">
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
            :step="field.step ?? (field.precision ? 0.01 : 1)"
            class="crud-console__number"
          />

          <el-switch v-else-if="field.type === 'switch'" v-model="form[field.prop]" />

          <el-select
            v-else-if="field.type === 'select'"
            v-model="form[field.prop]"
            class="crud-console__select"
            clearable
            :placeholder="field.placeholder || `请选择${field.label}`"
          >
            <el-option
              v-for="option in field.options || []"
              :key="option.value"
              :label="option.label"
              :value="option.value"
            />
          </el-select>

          <el-date-picker
            v-else-if="field.type === 'date'"
            v-model="form[field.prop]"
            type="date"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD"
            class="crud-console__select"
            :placeholder="field.placeholder || `请选择${field.label}`"
          />

          <el-input
            v-else
            v-model="form[field.prop]"
            :placeholder="field.placeholder || `请输入${field.label}`"
          />

          <div v-if="field.hint" class="crud-console__hint">{{ field.hint }}</div>
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="closeDialog">取消</el-button>
        <el-button type="primary" :loading="saving" @click="submit">保存</el-button>
      </template>
    </el-dialog>
  </section>
</template>

<script setup>
import { computed, nextTick, onMounted, ref } from 'vue';
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

const formRef = ref();
const loading = ref(false);
const saving = ref(false);
const dialogVisible = ref(false);
const editingId = ref(null);
const rows = ref([]);
const form = ref({});
const submitError = ref('');

const dialogTitle = computed(() => (editingId.value ? `编辑${props.title}` : `新增${props.title}`));

const formRules = computed(() =>
  props.fields.reduce((accumulator, field) => {
    const rules = buildFieldRules(field);
    if (rules.length) {
      accumulator[field.prop] = rules;
    }
    return accumulator;
  }, {}),
);

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
  if (field.type === 'select' || field.type === 'date') {
    return null;
  }
  return '';
}

function buildForm(source = {}) {
  return props.fields.reduce((accumulator, field) => {
    accumulator[field.prop] = source[field.prop] ?? getDefaultValue(field);
    return accumulator;
  }, {});
}

function buildFieldRules(field) {
  const rules = [];
  const trigger = field.type === 'select' || field.type === 'date' || field.type === 'switch' ? 'change' : 'blur';

  if (field.required) {
    rules.push({
      required: true,
      message: field.requiredMessage || `${field.label}不能为空`,
      trigger,
    });
  }

  if (field.pattern) {
    rules.push({
      pattern: field.pattern,
      message: field.patternMessage || `${field.label}格式不正确`,
      trigger,
    });
  }

  if (field.type === 'number' && (field.min !== undefined || field.max !== undefined)) {
    rules.push({
      validator: (_, value, callback) => {
        if (value === null || value === undefined) {
          callback();
          return;
        }
        if (field.min !== undefined && value < field.min) {
          callback(new Error(field.minMessage || `${field.label}不能小于 ${field.min}`));
          return;
        }
        if (field.max !== undefined && value > field.max) {
          callback(new Error(field.maxMessage || `${field.label}不能大于 ${field.max}`));
          return;
        }
        callback();
      },
      trigger: 'change',
    });
  }

  if (Array.isArray(field.rules)) {
    rules.push(...field.rules);
  }

  return rules;
}

async function loadRows() {
  loading.value = true;
  try {
    rows.value = await fetchCrudList(props.resource);
  } catch (error) {
    ElMessage.error(error.message || '加载失败');
  } finally {
    loading.value = false;
  }
}

async function prepareDialog(source, id) {
  editingId.value = id;
  form.value = buildForm(source);
  submitError.value = '';
  dialogVisible.value = true;
  await nextTick();
  formRef.value?.clearValidate?.();
}

function openCreate() {
  prepareDialog({}, null);
}

async function openEdit(row) {
  loading.value = true;
  try {
    const detail = await fetchCrudDetail(props.resource, row.id);
    await prepareDialog(detail, row.id);
  } catch (error) {
    ElMessage.error(error.message || '加载详情失败');
  } finally {
    loading.value = false;
  }
}

async function submit() {
  submitError.value = '';
  try {
    await formRef.value?.validate();
  } catch (error) {
    return;
  }

  saving.value = true;
  try {
    if (editingId.value) {
      await updateCrudItem(props.resource, editingId.value, form.value);
      ElMessage.success('保存成功');
    } else {
      await createCrudItem(props.resource, form.value);
      ElMessage.success('新增成功');
    }
    closeDialog();
    await loadRows();
    emit('changed');
  } catch (error) {
    submitError.value = error.message || '保存失败';
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
    if (error !== 'cancel' && error !== 'close') {
      ElMessage.error(error.message || '删除失败');
    }
  }
}

function closeDialog() {
  dialogVisible.value = false;
}

function handleDialogClosed() {
  editingId.value = null;
  submitError.value = '';
  form.value = buildForm();
  formRef.value?.clearValidate?.();
}

onMounted(() => {
  form.value = buildForm();
  loadRows();
});
</script>

<style scoped>
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

.crud-console__alert {
  margin-bottom: 16px;
}

.crud-console__number,
.crud-console__select {
  width: 100%;
}

.crud-console__hint {
  margin-top: 6px;
  font-size: 12px;
  line-height: 1.5;
  color: var(--el-text-color-secondary);
}
</style>
