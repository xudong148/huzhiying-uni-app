<template>
  <div>
    <!-- Banner 配置 -->
    <crud-console
      title="首页 Banner"
      description="维护首页轮播图、跳转链接和启用状态。"
      resource="/api/admin/content/banners"
      :columns="bannerColumns"
      :fields="bannerFields"
    />

    <!-- 公告配置 -->
    <crud-console
      title="公告通知"
      description="维护首页通知条、消息中心公告和等级标签。"
      resource="/api/admin/content/notices"
      :columns="noticeColumns"
      :fields="noticeFields"
    />

    <!-- 协议配置 -->
    <crud-console
      title="协议文档"
      description="维护用户服务协议、隐私政策等文档内容。"
      resource="/api/admin/content/agreements"
      :columns="agreementColumns"
      :fields="agreementFields"
      width="820px"
    />
  </div>
</template>

<script setup>
/**
 * 内容运营页面。
 * 所有数据都走后台真实 CRUD 接口。
 */
import CrudConsole from '../components/CrudConsole.vue';

// Banner 表格和表单配置。
const bannerColumns = [
  { prop: 'title', label: '标题', minWidth: 180 },
  { prop: 'subtitle', label: '副标题', minWidth: 220 },
  { prop: 'sortOrder', label: '排序', width: 100 },
  { prop: 'enabled', label: '状态', width: 100, type: 'switch' },
];

const bannerFields = [
  { prop: 'title', label: '标题', type: 'text' },
  { prop: 'subtitle', label: '副标题', type: 'text' },
  { prop: 'image', label: '图片地址', type: 'text' },
  { prop: 'link', label: '跳转链接', type: 'text' },
  { prop: 'sortOrder', label: '排序', type: 'number' },
  { prop: 'enabled', label: '启用', type: 'switch', default: true },
];

// 公告表格和表单配置。
const noticeColumns = [
  { prop: 'title', label: '标题', minWidth: 220 },
  { prop: 'levelCode', label: '级别', width: 120 },
  { prop: 'enabled', label: '状态', width: 100, type: 'switch' },
];

const noticeFields = [
  { prop: 'title', label: '公告标题', type: 'text' },
  { prop: 'content', label: '公告内容', type: 'textarea', rows: 4 },
  {
    prop: 'levelCode',
    label: '级别',
    type: 'select',
    options: [
      { label: '提醒', value: 'warning' },
      { label: '营销', value: 'promo' },
      { label: '系统', value: 'system' },
    ],
  },
  { prop: 'enabled', label: '启用', type: 'switch', default: true },
];

// 协议表格和表单配置。
const agreementColumns = [
  { prop: 'title', label: '文档标题', minWidth: 180 },
  { prop: 'version', label: '版本号', width: 140 },
  { prop: 'enabled', label: '状态', width: 100, type: 'switch' },
];

const agreementFields = [
  { prop: 'title', label: '文档标题', type: 'text' },
  { prop: 'version', label: '版本号', type: 'text' },
  { prop: 'content', label: '正文内容', type: 'textarea', rows: 8 },
  { prop: 'enabled', label: '启用', type: 'switch', default: true },
];
</script>

<style scoped>
/* 当前页使用通用 CRUD 面板布局，这里保留样式区块注释以统一页面结构。 */
</style>
