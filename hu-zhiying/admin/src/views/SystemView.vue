<template>
  <div>
    <!-- 角色配置 -->
    <crud-console
      title="角色配置"
      description="维护后台角色编码、名称和职责描述。"
      resource="/api/admin/system/roles"
      :columns="roleColumns"
      :fields="roleFields"
    />

    <!-- 菜单配置 -->
    <crud-console
      title="菜单配置"
      description="维护后台导航菜单路径、图标和排序。"
      resource="/api/admin/system/menus"
      :columns="menuColumns"
      :fields="menuFields"
    />

    <!-- 权限点配置 -->
    <crud-console
      title="权限点"
      description="维护订单强派、退款审核等细粒度权限点。"
      resource="/api/admin/system/permissions"
      :columns="permissionColumns"
      :fields="permissionFields"
      width="820px"
    />
  </div>
</template>

<script setup>
/**
 * 系统权限页面。
 * 角色、菜单和权限点都走独立的真实接口。
 */
import CrudConsole from '../components/CrudConsole.vue';

// 角色配置。
const roleColumns = [
  { prop: 'code', label: '角色编码', width: 160 },
  { prop: 'name', label: '角色名称', minWidth: 180 },
  { prop: 'description', label: '职责说明', minWidth: 260 },
  { prop: 'enabled', label: '状态', width: 100, type: 'switch' },
];

const roleFields = [
  { prop: 'code', label: '角色编码', type: 'text' },
  { prop: 'name', label: '角色名称', type: 'text' },
  { prop: 'description', label: '职责说明', type: 'textarea', rows: 4 },
  { prop: 'enabled', label: '启用', type: 'switch', default: true },
];

// 菜单配置。
const menuColumns = [
  { prop: 'name', label: '菜单名称', minWidth: 180 },
  { prop: 'path', label: '路由路径', minWidth: 180 },
  { prop: 'icon', label: '图标', minWidth: 180 },
  { prop: 'sortOrder', label: '排序', width: 100 },
  { prop: 'enabled', label: '状态', width: 100, type: 'switch' },
];

const menuFields = [
  { prop: 'name', label: '菜单名称', type: 'text' },
  { prop: 'path', label: '路由路径', type: 'text' },
  { prop: 'icon', label: '图标', type: 'text' },
  { prop: 'sortOrder', label: '排序', type: 'number', min: 0 },
  { prop: 'enabled', label: '启用', type: 'switch', default: true },
];

// 权限点配置。
const permissionColumns = [
  { prop: 'code', label: '权限编码', width: 220 },
  { prop: 'name', label: '权限名称', minWidth: 180 },
  { prop: 'description', label: '描述', minWidth: 260 },
  { prop: 'enabled', label: '状态', width: 100, type: 'switch' },
];

const permissionFields = [
  { prop: 'code', label: '权限编码', type: 'text' },
  { prop: 'name', label: '权限名称', type: 'text' },
  { prop: 'description', label: '权限描述', type: 'textarea', rows: 4 },
  { prop: 'enabled', label: '启用', type: 'switch', default: true },
];
</script>

<style scoped>
/* 当前页使用通用 CRUD 面板布局，这里保留样式区块注释以统一页面结构。 */
</style>
