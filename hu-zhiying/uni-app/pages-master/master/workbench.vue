<template>
  <view class="page-shell">
    <order-timeline :list="steps"></order-timeline>

    <view class="card master-workbench__section">
      <view class="section-title">
        <text class="section-title__text">现场操作</text>
      </view>
      <view class="master-workbench__grid">
        <view class="master-workbench__action" @tap="markDone('已开始出发')">开始出发</view>
        <view class="master-workbench__action" @tap="markDone('已完成到场打卡')">到达打卡</view>
        <view class="master-workbench__action" @tap="markDone('已上传服务前照片')">服务前拍照</view>
        <view class="master-workbench__action" @tap="markDone('已提交增项报价')">增项报价</view>
        <view class="master-workbench__action" @tap="markDone('已完成完工拍照')">完工拍照</view>
      </view>
    </view>

    <view class="card master-workbench__section">
      <view class="section-title">
        <text class="section-title__text">离线任务队列</text>
      </view>
      <view class="muted">弱网环境下的打卡、照片和报价将进入离线队列，恢复网络后自动重传。</view>
    </view>
  </view>
</template>

<script setup>
import { ref } from 'vue';
import OrderTimeline from '../../components/order-timeline.vue';
import { enqueueOfflineTask } from '../../utils/offline-queue';

const steps = ref([
  { key: 'start', label: '开始出发', desc: '点击后同步通知用户', done: true },
  { key: 'arrival', label: '到达现场', desc: '需 LBS 校验并允许离线补传', done: false },
  { key: 'before', label: '服务前拍照', desc: '拍照后留存工单凭证', done: false },
  { key: 'quote', label: '提交增项报价', desc: '用户确认后方可施工', done: false },
  { key: 'finish', label: '完工拍照', desc: '自动生成电子服务报告', done: false },
]);

function markDone(label) {
  enqueueOfflineTask({
    type: 'workbench-action',
    label,
  });
  uni.showToast({ title: `${label} 已入队`, icon: 'none' });
}
</script>

<style scoped>
.master-workbench__section {
  margin-top: 20rpx;
  padding: 28rpx;
}

.master-workbench__grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 14rpx;
}

.master-workbench__action {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 90rpx;
  border-radius: 24rpx;
  background: #eef2ff;
  color: #2b5cff;
  font-size: 24rpx;
  font-weight: 700;
}
</style>
