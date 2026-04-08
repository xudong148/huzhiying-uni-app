<template>
  <view class="page-shell">
    <!-- 地址列表 -->
    <view
      v-for="item in list"
      :key="item.id"
      class="card address-page__card"
    >
      <view class="address-page__body pressable" @tap="handleTap(item)">
        <view class="address-page__top">
          <view class="address-page__tag">{{ item.tag }}</view>
          <text class="chip" v-if="item.default">默认</text>
        </view>
        <view class="address-page__address">{{ item.address }}</view>
        <view class="address-page__contact">{{ item.name }} / {{ item.mobile }}</view>
      </view>

      <view class="address-page__actions">
        <button class="mini-btn" @tap="goEdit(item.id)">编辑</button>
        <button class="mini-btn mini-btn--danger" @tap="handleDelete(item.id)">删除</button>
      </view>
    </view>

    <!-- 新增按钮 -->
    <button class="primary-btn address-page__btn" @tap="goEdit()">新增地址</button>
  </view>
</template>

<script setup>
/**
 * 地址列表页。
 * 1. 支持地址选择、编辑和删除。
 * 2. 所有操作都走真实地址接口，不依赖本地假数据。
 */
import { ref } from 'vue';
import { onLoad, onShow } from '@dcloudio/uni-app';
import { deleteAddress, getAddressList } from '../../api/user';

const list = ref([]);
const selectMode = ref(false);

function goEdit(id = '') {
  const suffix = id ? `?id=${id}` : '';
  uni.navigateTo({ url: `/pages/setting/address-edit${suffix}` });
}

function handleTap(item) {
  if (selectMode.value) {
    uni.setStorageSync('hzy-selected-address-id', item.id);
    uni.navigateBack();
    return;
  }
  goEdit(item.id);
}

async function loadAddresses() {
  const res = await getAddressList();
  list.value = res.data;
}

async function handleDelete(id) {
  const modal = await new Promise((resolve) => {
    uni.showModal({
      title: '删除地址',
      content: '确认删除这条地址吗？',
      success: resolve,
    });
  });
  if (!modal.confirm) {
    return;
  }
  await deleteAddress(id);
  uni.showToast({ title: '删除成功', icon: 'none' });
  await loadAddresses();
}

onLoad((options) => {
  selectMode.value = options.select === '1';
});

onShow(loadAddresses);
</script>

<style scoped>
/* 地址卡片布局 */
.address-page__card {
  padding: 26rpx;
}

.address-page__card + .address-page__card {
  margin-top: 16rpx;
}

.address-page__body {
  width: 100%;
}

.address-page__top {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.address-page__tag {
  display: inline-flex;
  align-items: center;
  height: 44rpx;
  padding: 0 18rpx;
  border-radius: 999rpx;
  background: rgba(43, 92, 255, 0.1);
  color: #2b5cff;
  font-size: 22rpx;
}

.address-page__address {
  margin-top: 16rpx;
  font-size: 28rpx;
  line-height: 1.7;
  font-weight: 700;
}

.address-page__contact {
  margin-top: 10rpx;
  color: #667085;
  font-size: 24rpx;
}

.address-page__actions {
  display: flex;
  gap: 12rpx;
  margin-top: 20rpx;
}

.mini-btn {
  min-width: 140rpx;
  height: 64rpx;
  border-radius: 999rpx;
  background: #eef2ff;
  color: #2b5cff;
  font-size: 24rpx;
  line-height: 64rpx;
}

.mini-btn--danger {
  background: #fff1f2;
  color: #e11d48;
}

.address-page__btn {
  margin-top: 22rpx;
}
</style>
