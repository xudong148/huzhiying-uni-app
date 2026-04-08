<template>
  <div class="page-panel">
    <h2 class="page-title">定价与类目</h2>
    <p class="page-desc">维护服务类目、服务项、商品、SKU、定价规则和服务区域。</p>

    <el-tabs v-model="activeTab" class="pricing-tabs">
      <el-tab-pane label="服务类目" name="categories">
        <crud-console
          title="服务类目"
          description="维护首页和类目页展示的服务分类。"
          resource="/api/admin/catalog/categories"
          :columns="categoryColumns"
          :fields="categoryFields"
          @changed="reloadMeta"
        />
      </el-tab-pane>

      <el-tab-pane label="服务项" name="services">
        <crud-console
          title="服务项"
          description="维护服务详情页、价格说明和履约配置。"
          resource="/api/admin/catalog/service-items"
          :columns="serviceItemColumns"
          :fields="serviceItemFields"
          width="860px"
          @changed="reloadMeta"
        />
      </el-tab-pane>

      <el-tab-pane label="商品" name="products">
        <crud-console
          title="商品"
          description="维护商品基础价、标签价、折扣价和安装联动配置。"
          resource="/api/admin/catalog/products"
          :columns="productColumns"
          :fields="productFields"
          @changed="reloadMeta"
        />
      </el-tab-pane>

      <el-tab-pane label="SKU" name="skus">
        <crud-console
          title="SKU"
          description="维护商品规格、SKU 价格体系和库存。"
          resource="/api/admin/catalog/skus"
          :columns="skuColumns"
          :fields="skuFields"
          @changed="reloadMeta"
        />
      </el-tab-pane>

      <el-tab-pane label="定价规则" name="rules">
        <crud-console
          title="定价规则"
          description="维护基础价、系数说明和指导价说明。"
          resource="/api/admin/pricing/rules"
          :columns="pricingRuleColumns"
          :fields="pricingRuleFields"
          @changed="reloadMeta"
        />
      </el-tab-pane>

      <el-tab-pane label="服务区域" name="zones">
        <crud-console
          title="服务区域"
          description="维护可服务城市、区县和排序。"
          resource="/api/admin/dispatch/zones"
          :columns="dispatchZoneColumns"
          :fields="dispatchZoneFields"
          @changed="reloadMeta"
        />
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue';
import CrudConsole from '../components/CrudConsole.vue';
import { fetchCrudList } from '../api/request';

const activeTab = ref('categories');
const categories = ref([]);
const products = ref([]);

const categoryOptions = computed(() => categories.value.map((item) => ({ label: item.name, value: item.id })));
const productOptions = computed(() => products.value.map((item) => ({ label: item.name, value: item.id })));
const categoryMap = computed(() => Object.fromEntries(categories.value.map((item) => [item.id, item.name])));
const productMap = computed(() => Object.fromEntries(products.value.map((item) => [item.id, item.name])));

function formatMoney(value) {
  return `¥${Number(value || 0).toFixed(2)}`;
}

async function reloadMeta() {
  const [categoryList, productList] = await Promise.all([
    fetchCrudList('/api/admin/catalog/categories'),
    fetchCrudList('/api/admin/catalog/products'),
  ]);
  categories.value = categoryList;
  products.value = productList;
}

function resolveCategoryName(categoryId) {
  return categoryMap.value[categoryId] || `#${categoryId}`;
}

function resolveProductName(productId) {
  return productMap.value[productId] || `#${productId}`;
}

const categoryColumns = [
  { prop: 'name', label: '类目名称', minWidth: 180 },
  { prop: 'icon', label: '图标', minWidth: 220 },
  { prop: 'sortOrder', label: '排序', width: 100 },
  { prop: 'enabled', label: '状态', width: 100, type: 'switch' },
];

const categoryFields = [
  { prop: 'name', label: '类目名称', type: 'text', required: true },
  { prop: 'icon', label: '图标地址', type: 'text' },
  { prop: 'sortOrder', label: '排序', type: 'number', min: 0, required: true },
  { prop: 'enabled', label: '启用', type: 'switch', default: true },
];

const serviceItemColumns = [
  { prop: 'name', label: '服务名称', minWidth: 180 },
  { prop: 'categoryId', label: '所属类目', minWidth: 140, formatter: (row) => resolveCategoryName(row.categoryId) },
  { prop: 'basePrice', label: '基础价', width: 120, formatter: (row) => formatMoney(row.basePrice) },
  { prop: 'doorPrice', label: '上门费', width: 120, formatter: (row) => formatMoney(row.doorPrice) },
  { prop: 'guidePrice', label: '指导价', minWidth: 140 },
  { prop: 'enabled', label: '状态', width: 100, type: 'switch' },
];

const serviceItemFields = computed(() => [
  {
    prop: 'categoryId',
    label: '所属类目',
    type: 'select',
    options: categoryOptions.value,
    required: true,
    placeholder: '请选择服务类目',
  },
  { prop: 'name', label: '服务名称', type: 'text', required: true },
  { prop: 'subtitle', label: '副标题', type: 'text' },
  { prop: 'basePrice', label: '基础价', type: 'number', precision: 2, min: 0, required: true },
  { prop: 'doorPrice', label: '上门费', type: 'number', precision: 2, min: 0, required: true },
  { prop: 'guidePrice', label: '指导价', type: 'text' },
  { prop: 'warrantyText', label: '质保说明', type: 'text' },
  {
    prop: 'guaranteesText',
    label: '保障说明',
    type: 'textarea',
    rows: 3,
    hint: '多条说明可用 | 分隔。',
  },
  {
    prop: 'tagsText',
    label: '标签',
    type: 'textarea',
    rows: 3,
    placeholder: '使用 | 分隔多个标签',
  },
  {
    prop: 'imageUrls',
    label: '图片地址',
    type: 'textarea',
    rows: 3,
    placeholder: '使用 | 分隔多个图片地址',
  },
  {
    prop: 'processSteps',
    label: '流程步骤',
    type: 'textarea',
    rows: 5,
    placeholder: '使用 | 分隔多个流程节点',
  },
  { prop: 'enabled', label: '启用', type: 'switch', default: true },
]);

const productColumns = [
  { prop: 'name', label: '商品名称', minWidth: 180 },
  { prop: 'tagPrice', label: '标签价', width: 120, formatter: (row) => formatMoney(row.tagPrice) },
  { prop: 'discountPrice', label: '折扣价', width: 120, formatter: (row) => formatMoney(row.discountPrice) },
  { prop: 'price', label: '结算价', width: 120, formatter: (row) => formatMoney(row.price) },
  {
    prop: 'createInstallOrder',
    label: '安装联动',
    width: 140,
    formatter: (row) => (row.createInstallOrder ? '自动生成工单' : '不生成'),
  },
  { prop: 'enabled', label: '状态', width: 100, type: 'switch' },
];

const productFields = [
  { prop: 'name', label: '商品名称', type: 'text', required: true },
  { prop: 'descriptionText', label: '商品描述', type: 'textarea', rows: 4 },
  { prop: 'tagPrice', label: '标签价', type: 'number', precision: 2, min: 0, required: true },
  { prop: 'discountPrice', label: '折扣价', type: 'number', precision: 2, min: 0, required: true },
  {
    prop: 'price',
    label: '结算价',
    type: 'number',
    precision: 2,
    min: 0,
    required: true,
    hint: '前台最终成交价，默认不应高于标签价。',
  },
  { prop: 'createInstallOrder', label: '自动生成安装工单', type: 'switch', default: false },
  { prop: 'imageUrl', label: '商品图片', type: 'text' },
  { prop: 'enabled', label: '启用', type: 'switch', default: true },
];

const skuColumns = [
  { prop: 'productId', label: '所属商品', minWidth: 160, formatter: (row) => resolveProductName(row.productId) },
  { prop: 'name', label: 'SKU 名称', minWidth: 180 },
  { prop: 'tagPrice', label: '标签价', width: 120, formatter: (row) => formatMoney(row.tagPrice) },
  { prop: 'discountPrice', label: '折扣价', width: 120, formatter: (row) => formatMoney(row.discountPrice) },
  { prop: 'price', label: '结算价', width: 120, formatter: (row) => formatMoney(row.price) },
  { prop: 'stock', label: '库存', width: 120 },
  { prop: 'enabled', label: '状态', width: 100, type: 'switch' },
];

const skuFields = computed(() => [
  {
    prop: 'productId',
    label: '所属商品',
    type: 'select',
    options: productOptions.value,
    required: true,
    placeholder: '请选择商品',
  },
  { prop: 'name', label: 'SKU 名称', type: 'text', required: true },
  { prop: 'tagPrice', label: '标签价', type: 'number', precision: 2, min: 0, required: true },
  { prop: 'discountPrice', label: '折扣价', type: 'number', precision: 2, min: 0, required: true },
  {
    prop: 'price',
    label: '结算价',
    type: 'number',
    precision: 2,
    min: 0,
    required: true,
    hint: 'SKU 成交价，建议和商品价格体系保持一致。',
  },
  { prop: 'stock', label: '库存', type: 'number', min: 0, required: true },
  { prop: 'enabled', label: '启用', type: 'switch', default: true },
]);

const pricingRuleColumns = [
  { prop: 'label', label: '规则名称', minWidth: 220 },
  { prop: 'categoryId', label: '所属类目', minWidth: 140, formatter: (row) => resolveCategoryName(row.categoryId) },
  { prop: 'basePrice', label: '基础价', width: 120, formatter: (row) => formatMoney(row.basePrice) },
  { prop: 'coefficient', label: '系数说明', minWidth: 180 },
  { prop: 'enabled', label: '状态', width: 100, type: 'switch' },
];

const pricingRuleFields = computed(() => [
  {
    prop: 'categoryId',
    label: '所属类目',
    type: 'select',
    options: categoryOptions.value,
    required: true,
    placeholder: '请选择服务类目',
  },
  { prop: 'label', label: '规则名称', type: 'text', required: true },
  { prop: 'basePrice', label: '基础价', type: 'number', precision: 2, min: 0, required: true },
  { prop: 'coefficient', label: '系数说明', type: 'textarea', rows: 3 },
  { prop: 'guidePrice', label: '指导价', type: 'text' },
  { prop: 'enabled', label: '启用', type: 'switch', default: true },
]);

const dispatchZoneColumns = [
  { prop: 'cityName', label: '城市', width: 140 },
  { prop: 'districtName', label: '区县', minWidth: 180 },
  { prop: 'sortOrder', label: '排序', width: 100 },
  { prop: 'enabled', label: '状态', width: 100, type: 'switch' },
];

const dispatchZoneFields = [
  { prop: 'cityName', label: '城市名称', type: 'text', required: true },
  { prop: 'districtName', label: '区县名称', type: 'text', required: true },
  { prop: 'sortOrder', label: '排序', type: 'number', min: 0, required: true },
  { prop: 'enabled', label: '启用', type: 'switch', default: true },
];

onMounted(reloadMeta);
</script>

<style scoped>
.pricing-tabs {
  margin-top: 18px;
}
</style>
