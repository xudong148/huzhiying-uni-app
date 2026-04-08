<template>
  <div class="page-panel">
    <!-- 页面标题 -->
    <h2 class="page-title">定价与类目</h2>
    <p class="page-desc">维护服务类目、服务项、商品、SKU、定价规则和服务区域。</p>

    <!-- 多资源配置入口 -->
    <el-tabs v-model="activeTab" class="pricing-tabs">
      <el-tab-pane label="服务类目" name="categories">
        <crud-console
          title="服务类目"
          description="维护首页金刚区和类目页展示的服务类目。"
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
        />
      </el-tab-pane>

      <el-tab-pane label="商品" name="products">
        <crud-console
          title="商品"
          description="维护商城商品、安装联动配置和启用状态。"
          resource="/api/admin/catalog/products"
          :columns="productColumns"
          :fields="productFields"
          @changed="reloadMeta"
        />
      </el-tab-pane>

      <el-tab-pane label="SKU" name="skus">
        <crud-console
          title="SKU"
          description="维护商品规格、库存和价格。"
          resource="/api/admin/catalog/skus"
          :columns="skuColumns"
          :fields="skuFields"
        />
      </el-tab-pane>

      <el-tab-pane label="定价规则" name="rules">
        <crud-console
          title="定价规则"
          description="维护基础价、指导价和特殊时段加价说明。"
          resource="/api/admin/pricing/rules"
          :columns="pricingRuleColumns"
          :fields="pricingRuleFields"
        />
      </el-tab-pane>

      <el-tab-pane label="服务区域" name="zones">
        <crud-console
          title="服务区域"
          description="维护可服务城市、区县和地图白名单。"
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
/**
 * 定价与类目页面。
 * 部分表单字段依赖类目与商品元数据，因此页面初始化时会先加载选项。
 */
import { computed, onMounted, ref } from 'vue';
import CrudConsole from '../components/CrudConsole.vue';
import { fetchCrudList } from '../api/request';

const activeTab = ref('categories');
const categoryOptions = ref([]);
const productOptions = ref([]);

async function reloadMeta() {
  const [categories, products] = await Promise.all([
    fetchCrudList('/api/admin/catalog/categories'),
    fetchCrudList('/api/admin/catalog/products'),
  ]);
  categoryOptions.value = categories.map((item) => ({ label: item.name, value: item.id }));
  productOptions.value = products.map((item) => ({ label: item.name, value: item.id }));
}

// 服务类目配置。
const categoryColumns = [
  { prop: 'name', label: '类目名称', minWidth: 180 },
  { prop: 'icon', label: '图标', minWidth: 220 },
  { prop: 'sortOrder', label: '排序', width: 100 },
  { prop: 'enabled', label: '状态', width: 100, type: 'switch' },
];

const categoryFields = [
  { prop: 'name', label: '类目名称', type: 'text' },
  { prop: 'icon', label: '图标地址', type: 'text' },
  { prop: 'sortOrder', label: '排序', type: 'number', min: 0 },
  { prop: 'enabled', label: '启用', type: 'switch', default: true },
];

// 服务项配置。
const serviceItemColumns = [
  { prop: 'name', label: '服务名称', minWidth: 180 },
  { prop: 'categoryId', label: '类目 ID', width: 120 },
  { prop: 'basePrice', label: '基础价', width: 120, formatter: (row) => `¥${Number(row.basePrice || 0).toFixed(2)}` },
  { prop: 'guidePrice', label: '指导价', minWidth: 140 },
  { prop: 'enabled', label: '状态', width: 100, type: 'switch' },
];

const serviceItemFields = computed(() => ([
  { prop: 'categoryId', label: '所属类目', type: 'select', options: categoryOptions.value },
  { prop: 'name', label: '服务名称', type: 'text' },
  { prop: 'subtitle', label: '副标题', type: 'text' },
  { prop: 'basePrice', label: '基础价', type: 'number', precision: 2, min: 0 },
  { prop: 'doorPrice', label: '上门费', type: 'number', precision: 2, min: 0 },
  { prop: 'guidePrice', label: '指导价', type: 'text' },
  { prop: 'warrantyText', label: '质保说明', type: 'text' },
  { prop: 'guaranteesText', label: '保障说明', type: 'textarea', rows: 3 },
  { prop: 'tagsText', label: '标签', type: 'textarea', rows: 3, placeholder: '使用 | 分隔多个标签' },
  { prop: 'imageUrls', label: '图片地址', type: 'textarea', rows: 3, placeholder: '使用 | 分隔多个图片地址' },
  { prop: 'processSteps', label: '流程步骤', type: 'textarea', rows: 5, placeholder: '使用 | 分隔多个流程节点' },
  { prop: 'enabled', label: '启用', type: 'switch', default: true },
]));

// 商品配置。
const productColumns = [
  { prop: 'name', label: '商品名称', minWidth: 180 },
  { prop: 'price', label: '售价', width: 120, formatter: (row) => `¥${Number(row.price || 0).toFixed(2)}` },
  {
    prop: 'createInstallOrder',
    label: '安装联动',
    width: 140,
    formatter: (row) => (row.createInstallOrder ? '自动生成工单' : '不生成'),
  },
  { prop: 'enabled', label: '状态', width: 100, type: 'switch' },
];

const productFields = [
  { prop: 'name', label: '商品名称', type: 'text' },
  { prop: 'descriptionText', label: '商品描述', type: 'textarea', rows: 4 },
  { prop: 'price', label: '售价', type: 'number', precision: 2, min: 0 },
  { prop: 'createInstallOrder', label: '自动生成安装工单', type: 'switch', default: true },
  { prop: 'imageUrl', label: '商品图片', type: 'text' },
  { prop: 'enabled', label: '启用', type: 'switch', default: true },
];

// SKU 配置。
const skuColumns = [
  { prop: 'productId', label: '商品 ID', width: 120 },
  { prop: 'name', label: 'SKU 名称', minWidth: 180 },
  { prop: 'price', label: '价格', width: 120, formatter: (row) => `¥${Number(row.price || 0).toFixed(2)}` },
  { prop: 'stock', label: '库存', width: 120 },
  { prop: 'enabled', label: '状态', width: 100, type: 'switch' },
];

const skuFields = computed(() => ([
  { prop: 'productId', label: '所属商品', type: 'select', options: productOptions.value },
  { prop: 'name', label: 'SKU 名称', type: 'text' },
  { prop: 'price', label: '价格', type: 'number', precision: 2, min: 0 },
  { prop: 'stock', label: '库存', type: 'number', min: 0 },
  { prop: 'enabled', label: '启用', type: 'switch', default: true },
]));

// 定价规则配置。
const pricingRuleColumns = [
  { prop: 'label', label: '规则名称', minWidth: 220 },
  { prop: 'categoryId', label: '类目 ID', width: 120 },
  { prop: 'basePrice', label: '基础价', width: 120, formatter: (row) => `¥${Number(row.basePrice || 0).toFixed(2)}` },
  { prop: 'coefficient', label: '系数说明', minWidth: 180 },
  { prop: 'enabled', label: '状态', width: 100, type: 'switch' },
];

const pricingRuleFields = computed(() => ([
  { prop: 'categoryId', label: '所属类目', type: 'select', options: categoryOptions.value },
  { prop: 'label', label: '规则名称', type: 'text' },
  { prop: 'basePrice', label: '基础价', type: 'number', precision: 2, min: 0 },
  { prop: 'coefficient', label: '系数说明', type: 'textarea', rows: 3 },
  { prop: 'guidePrice', label: '指导价', type: 'text' },
  { prop: 'enabled', label: '启用', type: 'switch', default: true },
]));

// 服务区域配置。
const dispatchZoneColumns = [
  { prop: 'cityName', label: '城市', width: 140 },
  { prop: 'districtName', label: '区县', minWidth: 180 },
  { prop: 'sortOrder', label: '排序', width: 100 },
  { prop: 'enabled', label: '状态', width: 100, type: 'switch' },
];

const dispatchZoneFields = [
  { prop: 'cityName', label: '城市名称', type: 'text' },
  { prop: 'districtName', label: '区县名称', type: 'text' },
  { prop: 'sortOrder', label: '排序', type: 'number', min: 0 },
  { prop: 'enabled', label: '启用', type: 'switch', default: true },
];

onMounted(reloadMeta);
</script>

<style scoped>
/* 标签页间距 */
.pricing-tabs {
  margin-top: 18px;
}
</style>
