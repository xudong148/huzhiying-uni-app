<template>
  <div class="content-grid">
    <crud-console
      title="首页 Banner"
      description="维护首页轮播图、跳转链接和启用状态。"
      resource="/api/admin/content/banners"
      :columns="bannerColumns"
      :fields="bannerFields"
    />

    <crud-console
      title="公告通知"
      description="维护首页公告和消息中心通知。"
      resource="/api/admin/content/notices"
      :columns="noticeColumns"
      :fields="noticeFields"
    />

    <crud-console
      title="协议文档"
      description="维护用户协议、隐私政策等平台文档。"
      resource="/api/admin/content/agreements"
      :columns="agreementColumns"
      :fields="agreementFields"
      width="820px"
    />

    <crud-console
      title="生态卡片"
      description="配置首页学堂、商城、圈子等核心入口。"
      resource="/api/admin/content/ecosystem-cards"
      :columns="ecosystemColumns"
      :fields="ecosystemFields"
    />

    <crud-console
      title="学堂栏目"
      description="维护学堂栏目结构、排序和启用状态。"
      resource="/api/admin/content/academy-categories"
      :columns="academyCategoryColumns"
      :fields="academyCategoryFields"
    />

    <crud-console
      title="学堂文章"
      description="维护学堂文章封面、摘要、正文和发布状态。"
      resource="/api/admin/content/academy-articles"
      :columns="academyArticleColumns"
      :fields="academyArticleFields"
      width="980px"
    />

    <crud-console
      title="圈子帖子"
      description="维护同城圈子帖子、作者、配图和发布状态。"
      resource="/api/admin/content/community-posts"
      :columns="communityPostColumns"
      :fields="communityPostFields"
      width="980px"
    />

    <crud-console
      title="圈子举报"
      description="查看和处理圈子举报，形成内容审核闭环。"
      resource="/api/admin/content/community-reports"
      :columns="communityReportColumns"
      :fields="communityReportFields"
      width="860px"
    />
  </div>
</template>

<script setup>
import CrudConsole from '../components/CrudConsole.vue';

const bannerColumns = [
  { prop: 'title', label: '标题', minWidth: 180 },
  { prop: 'subtitle', label: '副标题', minWidth: 220 },
  { prop: 'sortOrder', label: '排序', width: 100 },
  { prop: 'enabled', label: '状态', width: 100, type: 'switch' },
];

const bannerFields = [
  { prop: 'title', label: '标题', type: 'text', required: true },
  { prop: 'subtitle', label: '副标题', type: 'text' },
  { prop: 'image', label: '图片地址', type: 'text', required: true },
  { prop: 'link', label: '跳转链接', type: 'text', required: true },
  { prop: 'sortOrder', label: '排序', type: 'number', min: 0, required: true },
  { prop: 'enabled', label: '启用', type: 'switch', default: true },
];

const noticeColumns = [
  { prop: 'title', label: '标题', minWidth: 220 },
  { prop: 'levelCode', label: '级别', width: 120 },
  { prop: 'enabled', label: '状态', width: 100, type: 'switch' },
];

const noticeFields = [
  { prop: 'title', label: '公告标题', type: 'text', required: true },
  { prop: 'content', label: '公告内容', type: 'textarea', rows: 4, required: true },
  {
    prop: 'levelCode',
    label: '级别',
    type: 'select',
    required: true,
    options: [
      { label: '提醒', value: 'warning' },
      { label: '运营', value: 'promo' },
      { label: '系统', value: 'system' },
    ],
  },
  { prop: 'enabled', label: '启用', type: 'switch', default: true },
];

const agreementColumns = [
  { prop: 'title', label: '文档标题', minWidth: 180 },
  { prop: 'version', label: '版本号', width: 140 },
  { prop: 'enabled', label: '状态', width: 100, type: 'switch' },
];

const agreementFields = [
  { prop: 'title', label: '文档标题', type: 'text', required: true },
  { prop: 'version', label: '版本号', type: 'text', required: true, hint: '建议使用 yyyy.MM 或语义化版本。' },
  { prop: 'content', label: '正文内容', type: 'textarea', rows: 8, required: true },
  { prop: 'enabled', label: '启用', type: 'switch', default: true },
];

const ecosystemColumns = [
  { prop: 'name', label: '名称', minWidth: 140 },
  { prop: 'description', label: '描述', minWidth: 220 },
  { prop: 'link', label: '跳转', minWidth: 180 },
  { prop: 'sortOrder', label: '排序', width: 100 },
  { prop: 'enabled', label: '状态', width: 100, type: 'switch' },
];

const ecosystemFields = [
  { prop: 'name', label: '名称', type: 'text', required: true },
  { prop: 'description', label: '描述', type: 'text' },
  { prop: 'icon', label: '图标地址', type: 'text', required: true },
  { prop: 'color', label: '主题色', type: 'text', required: true },
  { prop: 'link', label: '跳转路由', type: 'text', required: true },
  { prop: 'sortOrder', label: '排序', type: 'number', min: 0, required: true },
  { prop: 'enabled', label: '启用', type: 'switch', default: true },
];

const academyCategoryColumns = [
  { prop: 'name', label: '栏目名称', minWidth: 160 },
  { prop: 'description', label: '描述', minWidth: 220 },
  { prop: 'sortOrder', label: '排序', width: 100 },
  { prop: 'enabled', label: '状态', width: 100, type: 'switch' },
];

const academyCategoryFields = [
  { prop: 'name', label: '栏目名称', type: 'text', required: true },
  { prop: 'icon', label: '图标地址', type: 'text' },
  { prop: 'description', label: '栏目描述', type: 'textarea', rows: 3 },
  { prop: 'sortOrder', label: '排序', type: 'number', min: 0, required: true },
  { prop: 'enabled', label: '启用', type: 'switch', default: true },
];

const academyArticleColumns = [
  { prop: 'title', label: '文章标题', minWidth: 220 },
  { prop: 'categoryId', label: '栏目 ID', width: 100 },
  { prop: 'author', label: '作者', width: 140 },
  { prop: 'published', label: '发布', width: 100, type: 'switch' },
];

const academyArticleFields = [
  { prop: 'categoryId', label: '栏目 ID', type: 'number', min: 1, required: true },
  { prop: 'title', label: '文章标题', type: 'text', required: true },
  { prop: 'summary', label: '文章摘要', type: 'textarea', rows: 3 },
  { prop: 'content', label: '正文内容', type: 'textarea', rows: 10, required: true },
  { prop: 'coverImage', label: '封面图', type: 'text' },
  { prop: 'author', label: '作者', type: 'text' },
  { prop: 'viewCount', label: '浏览量', type: 'number', min: 0, required: true },
  { prop: 'sortOrder', label: '排序', type: 'number', min: 0, required: true },
  { prop: 'published', label: '已发布', type: 'switch', default: true },
];

const communityPostColumns = [
  { prop: 'title', label: '帖子标题', minWidth: 220 },
  { prop: 'cityName', label: '城市', width: 120 },
  { prop: 'authorName', label: '作者', width: 140 },
  { prop: 'published', label: '发布', width: 100, type: 'switch' },
];

const communityPostFields = [
  { prop: 'cityName', label: '城市', type: 'text', required: true },
  { prop: 'title', label: '帖子标题', type: 'text', required: true },
  { prop: 'content', label: '帖子正文', type: 'textarea', rows: 8, required: true },
  { prop: 'coverImage', label: '封面图', type: 'text' },
  { prop: 'images', label: '配图链接', type: 'textarea', rows: 4, hint: '多个图片地址使用 | 分隔。' },
  { prop: 'authorName', label: '作者', type: 'text' },
  { prop: 'likeCount', label: '点赞数', type: 'number', min: 0, required: true },
  { prop: 'commentCount', label: '评论数', type: 'number', min: 0, required: true },
  { prop: 'published', label: '已发布', type: 'switch', default: true },
];

const communityReportColumns = [
  { prop: 'postId', label: '帖子 ID', width: 100 },
  { prop: 'reason', label: '举报原因', minWidth: 180 },
  { prop: 'reporterName', label: '举报人', width: 140 },
  { prop: 'status', label: '状态', width: 120 },
  { prop: 'handled', label: '已处理', width: 100, type: 'switch' },
];

const communityReportFields = [
  { prop: 'postId', label: '帖子 ID', type: 'number', min: 1, required: true },
  { prop: 'reason', label: '举报原因', type: 'text', required: true },
  { prop: 'detail', label: '补充说明', type: 'textarea', rows: 4 },
  { prop: 'reporterName', label: '举报人', type: 'text' },
  { prop: 'status', label: '处理状态', type: 'text', required: true },
  { prop: 'handled', label: '已处理', type: 'switch', default: false },
];
</script>

<style scoped>
.content-grid {
  display: flex;
  flex-direction: column;
  gap: 20px;
}
</style>
