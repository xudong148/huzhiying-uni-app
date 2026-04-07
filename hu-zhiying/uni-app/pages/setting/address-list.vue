<template>
  <view class="page-shell">
    <view v-for="item in list" :key="item.id" class="card address-page__card pressable" @tap="goEdit(item.id)">
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
import { onMounted, ref } from 'vue';
import { getAddressList } from '../../api/user';

const list = ref([]);

function goEdit(id = '') {
  const suffix = id ? `?id=${id}` : '';
  uni.navigateTo({ url: `/pages/setting/address-edit${suffix}` });
}

onMounted(async () => {
  const res = await getAddressList();
  list.value = res.data;
});
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
