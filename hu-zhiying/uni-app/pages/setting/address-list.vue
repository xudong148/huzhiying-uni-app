<template>
  <view class="page-shell">
    <view v-if="selectMode" class="card address-page__notice">
      <text class="address-page__notice-title">请选择本次服务地址</text>
      <text class="address-page__notice-desc">选择后会自动回填到下单页，不需要重复输入。</text>
    </view>

    <view v-if="!list.length" class="card address-page__empty">
      <text class="address-page__empty-title">还没有地址</text>
      <text class="address-page__empty-desc">建议至少保存 1 个常用地址，避免下单时被地址步骤卡住。</text>
    </view>

    <view
      v-for="item in list"
      :key="item.id"
      class="card address-page__card"
    >
      <view class="address-page__body pressable" @tap="handleTap(item)">
        <view class="address-page__top">
          <view class="address-page__tag">{{ item.tag || '常用地址' }}</view>
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

    <button class="primary-btn address-page__btn" @tap="goEdit()">新增地址</button>
  </view>
</template>

<script setup>
import { ref } from 'vue';
import { onLoad, onShow } from '@dcloudio/uni-app';
import { deleteAddress, getAddressList } from '../../api/user';
import { safeAsync, showActionError } from '../../utils/page-task';

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
  list.value = res.data || [];
}

const safeLoadAddresses = safeAsync(loadAddresses, '加载地址列表');

async function handleDelete(id) {
  const modal = await new Promise((resolve) => {
    uni.showModal({
      title: '删除地址',
      content: '确认删除这条地址吗？删除后下单页将无法继续引用它。',
      success: resolve,
    });
  });
  if (!modal.confirm) {
    return;
  }
  try {
    await deleteAddress(id);
    uni.showToast({ title: '删除成功', icon: 'none' });
    await safeLoadAddresses();
  } catch (error) {
    showActionError(error, '删除地址失败');
  }
}

onLoad((options) => {
  selectMode.value = options.select === '1';
});

onShow(safeLoadAddresses);
</script>

<style scoped>
.address-page__notice,
.address-page__empty,
.address-page__card {
  padding: 26rpx;
}

.address-page__notice {
  margin-bottom: 16rpx;
}

.address-page__notice-title,
.address-page__empty-title {
  display: block;
  font-size: 30rpx;
  font-weight: 700;
  color: #1f2937;
}

.address-page__notice-desc,
.address-page__empty-desc {
  display: block;
  margin-top: 10rpx;
  font-size: 24rpx;
  line-height: 1.7;
  color: #667085;
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
