<template>
  <div class="kpi-card">
    <div class="kpi-card__label">{{ label }}</div>
    <div class="kpi-card__value">{{ value }}</div>
  </div>
</template>

<script setup>
defineProps({
  label: String,
  value: [String, Number],
});
</script>

<style scoped>
.kpi-card {
  padding: 24px;
  border-radius: 24px;
  background: linear-gradient(135deg, #2b5cff 0%, #8ab4f8 100%);
  color: #ffffff;
}

.kpi-card__label {
  font-size: 14px;
  opacity: 0.84;
}

.kpi-card__value {
  margin-top: 10px;
  font-size: 32px;
  font-weight: 800;
}
</style>
