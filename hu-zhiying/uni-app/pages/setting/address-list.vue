<template>
  <view class="page-shell">
    <view
      v-for="item in list"
      :key="item.id"
      class="card address-page__card pressable"
      @tap="handleTap(item)"
    >
      <view class="address-page__top">
        <view class="address-page__tag">{{ item.tag }}</view>
        <text class="chip" v-if="item.default">默认</text>
      </view>
      <view class="address-page__address">{{ item.address }}</view>
      <view class="address-page__contact">{{ item.name }} · {{ item.mobile }}</view>
    </view>

    <button class="primary-btn address-page__btn" @tap="goEdit()">新增地址</button>
  </view>
</template>

<script setup>
import { ref } from 'vue';
import { onLoad, onShow } from '@dcloudio/uni-app';
import { getAddressList } from '../../api/user';

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

onLoad((options) => {
  selectMode.value = options.select === '1';
});

onShow(loadAddresses);
</script>

<style scoped>
.address-page__card {
  padding: 26rpx;
}

.address-page__card + .address-page__card {
  margin-top: 16rpx;
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

.address-page__btn {
  margin-top: 22rpx;
}
</style>
