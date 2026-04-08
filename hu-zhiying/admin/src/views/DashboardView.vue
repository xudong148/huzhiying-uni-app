<template>
  <div>
    <div class="page-panel">
      <h2 class="page-title">平台总览</h2>
      <p class="page-desc">实时查看 GMV、订单体量、在线师傅与异常预警。</p>
      <div class="dashboard-grid">
        <kpi-card label="GMV" :value="`¥${Number(data.gmv || 0).toLocaleString()}`" />
        <kpi-card label="服务订单" :value="data.serviceOrders" />
        <kpi-card label="商品订单" :value="data.productOrders" />
        <kpi-card label="在线师傅" :value="data.onlineMasters" />
      </div>
    </div>

    <div class="page-panel dashboard-alert">
      <div>
        <h3 class="page-title">风险提醒</h3>
        <p class="page-desc">当前有 {{ data.warning }} 条仲裁或退款异常待处理。</p>
      </div>
      <router-link class="dashboard-alert__link" to="/arbitration">进入仲裁中心</router-link>
    </div>
  </div>
</template>

<script setup>
import { onMounted, reactive } from 'vue';
import KpiCard from '../components/KpiCard.vue';
import { fetchAdminDashboard } from '../api/request';

const data = reactive({
  gmv: 0,
  serviceOrders: 0,
  productOrders: 0,
  onlineMasters: 0,
  warning: 0,
});

onMounted(async () => {
  Object.assign(data, await fetchAdminDashboard());
});
</script>

<style scoped>
.dashboard-grid {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 16px;
}

.dashboard-alert {
  margin-top: 20px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 24px;
}

.dashboard-alert__link {
  color: #2b5cff;
  font-weight: 700;
  text-decoration: none;
}
</style>
