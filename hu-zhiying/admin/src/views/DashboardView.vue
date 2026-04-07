<template>
  <div>
    <div class="page-panel">
      <h2 class="page-title">平台总览</h2>
      <p class="page-desc">监控 GMV、订单、在线师傅和异常预警。</p>
      <div class="dashboard-grid">
        <kpi-card label="GMV" :value="data.gmv" />
        <kpi-card label="服务订单" :value="data.serviceOrders" />
        <kpi-card label="商品订单" :value="data.productOrders" />
        <kpi-card label="在线师傅" :value="data.onlineMasters" />
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted, reactive } from 'vue';
import KpiCard from '../components/KpiCard.vue';
import { getAdminMock } from '../api/request';

const data = reactive({
  gmv: 0,
  serviceOrders: 0,
  productOrders: 0,
  onlineMasters: 0,
});

onMounted(async () => {
  Object.assign(data, await getAdminMock('dashboard'));
});
</script>

<style scoped>
.dashboard-grid {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 16px;
}
</style>
