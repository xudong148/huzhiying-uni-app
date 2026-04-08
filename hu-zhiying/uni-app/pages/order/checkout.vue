<template>
  <view class="page-container">
    <scroll-view scroll-y class="order-checkout__scroll" :show-scrollbar="false">
      <view class="page-shell">
        <view class="card order-checkout__section pressable" @tap="goAddress">
          <view class="section-title">
            <text class="section-title__text">服务地址</text>
          </view>
          <view class="order-checkout__title">{{ selectedAddress.address || '请选择服务地址' }}</view>
          <view class="muted">{{ selectedAddress.name || '联系人' }} · {{ selectedAddress.mobile || '手机号' }}</view>
        </view>

        <view class="card order-checkout__section">
          <view class="section-title">
            <text class="section-title__text">预约时间</text>
          </view>
          <view class="order-checkout__chips">
            <view
              v-for="item in slots"
              :key="item"
              class="chip"
              :class="{ 'order-checkout__chip--active': item === selectedSlot }"
              @tap="selectedSlot = item"
            >
              {{ item }}
            </view>
          </view>
        </view>

        <view class="card order-checkout__section">
          <view class="section-title">
            <text class="section-title__text">服务信息</text>
            <text class="section-title__desc">{{ serviceTitle }}</text>
          </view>
          <textarea
            v-model="description"
            class="order-checkout__textarea"
            placeholder="请简要描述故障现象，师傅会据此准备工具和配件"
          ></textarea>
          <view class="order-checkout__switch-row">
            <view class="order-checkout__switch-item">
              <text>加急上门</text>
              <switch :checked="emergency" color="#2B5CFF" @change="emergency = $event.detail.value" />
            </view>
            <view class="order-checkout__switch-item">
              <text>夜间服务</text>
              <switch :checked="nightService" color="#2B5CFF" @change="nightService = $event.detail.value" />
            </view>
          </view>
          <view class="order-checkout__upload-row">
            <view class="order-checkout__upload">图片凭证占位</view>
            <view class="order-checkout__upload">录音凭证占位</view>
          </view>
        </view>

        <view class="card order-checkout__section">
          <view class="section-title">
            <text class="section-title__text">费用说明</text>
          </view>
          <view class="order-checkout__fee-row"><text>预估服务费</text><text>¥{{ estimatedPrice.toFixed(2) }}</text></view>
          <view class="order-checkout__fee-row"><text>支付方式</text><text>微信支付</text></view>
          <view class="order-checkout__fee-row"><text>支付环境</text><text>{{ prepaySandbox ? '开发沙箱' : '真实商户' }}</text></view>
        </view>
      </view>
      <view class="safe-bottom"></view>
    </scroll-view>

    <view class="order-checkout__submit">
      <view>
        <view class="muted">需支付预付款</view>
        <price-format :value="estimatedPrice"></price-format>
      </view>
      <button class="primary-btn order-checkout__submit-btn" :loading="submitting" @tap="submitOrder">确认并支付</button>
    </view>
  </view>
</template>

<script setup>
import { computed, ref } from 'vue';
import { onLoad, onShow } from '@dcloudio/uni-app';
import PriceFormat from '../../components/price-format.vue';
import { createServiceOrder, getTimeSlots, requestWechatPrepay } from '../../api/order';
import { getAddressList } from '../../api/user';

const serviceItemId = ref(201);
const serviceTitle = ref('空调上门维修');
const selectedAddress = ref({});
const addressList = ref([]);
const slots = ref([]);
const selectedSlot = ref('');
const description = ref('室内机制冷效果差，外机有异响。');
const emergency = ref(false);
const nightService = ref(false);
const submitting = ref(false);
const prepaySandbox = ref(true);

const estimatedPrice = computed(() => {
  let amount = 58;
  if (emergency.value) {
    amount += 30;
  }
  if (nightService.value) {
    amount += 20;
  }
  return amount;
});

function goAddress() {
  uni.navigateTo({ url: '/pages/setting/address-list?select=1' });
}

function syncSelectedAddress() {
  const selectedAddressId = Number(uni.getStorageSync('hzy-selected-address-id') || 0);
  if (selectedAddressId) {
    const matched = addressList.value.find((item) => item.id === selectedAddressId);
    if (matched) {
      selectedAddress.value = matched;
    }
    uni.removeStorageSync('hzy-selected-address-id');
    return;
  }

  if (!selectedAddress.value.id && addressList.value.length) {
    selectedAddress.value = addressList.value.find((item) => item.default) || addressList.value[0];
  }
}

async function loadBaseData() {
  const [addressRes, slotRes] = await Promise.all([getAddressList(), getTimeSlots()]);
  addressList.value = addressRes.data;
  slots.value = slotRes.data;
  selectedSlot.value = selectedSlot.value || slotRes.data[0];
  syncSelectedAddress();
}

async function submitOrder() {
  if (!selectedAddress.value.id) {
    uni.showToast({ title: '请先选择服务地址', icon: 'none' });
    return;
  }
  submitting.value = true;
  try {
    const createRes = await createServiceOrder({
      serviceItemId: serviceItemId.value,
      title: serviceTitle.value,
      appointment: selectedSlot.value,
      addressId: selectedAddress.value.id,
      description: description.value,
      emergency: emergency.value,
      nightService: nightService.value,
      evidences: [],
    });

    const prepayRes = await requestWechatPrepay(createRes.data.id);
    prepaySandbox.value = Boolean(prepayRes.data?.sandbox);

    uni.showToast({
      title: prepaySandbox.value ? '已创建沙箱支付单' : '支付单创建成功',
      icon: 'none',
    });
    setTimeout(() => {
      uni.redirectTo({ url: `/pages/order/detail?id=${createRes.data.id}` });
    }, 500);
  } finally {
    submitting.value = false;
  }
}

onLoad((options) => {
  serviceItemId.value = Number(options.serviceItemId || 201);
  serviceTitle.value = decodeURIComponent(options.title || '空调上门维修');
});

onShow(loadBaseData);
</script>

<style scoped>
.order-checkout__scroll {
  height: calc(100vh - 132rpx);
}

.order-checkout__section {
  padding: 28rpx;
}

.order-checkout__section + .order-checkout__section {
  margin-top: 18rpx;
}

.order-checkout__title {
  font-size: 28rpx;
  font-weight: 700;
  line-height: 1.6;
}

.order-checkout__chips {
  display: flex;
  flex-wrap: wrap;
  gap: 12rpx;
}

.order-checkout__chip--active {
  background: #2b5cff;
  color: #ffffff;
}

.order-checkout__textarea {
  width: 100%;
  min-height: 200rpx;
  padding: 20rpx;
  border-radius: 24rpx;
  background: #f4f6f9;
  font-size: 26rpx;
}

.order-checkout__switch-row {
  margin-top: 18rpx;
  display: flex;
  gap: 16rpx;
}

.order-checkout__switch-item {
  flex: 1;
  padding: 20rpx;
  border-radius: 20rpx;
  background: #f7f8fb;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 24rpx;
}

.order-checkout__upload-row {
  display: flex;
  gap: 12rpx;
  margin-top: 18rpx;
}

.order-checkout__upload {
  flex: 1;
  height: 84rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 22rpx;
  background: #eef2ff;
  color: #2b5cff;
  font-size: 24rpx;
  font-weight: 700;
}

.order-checkout__fee-row {
  display: flex;
  justify-content: space-between;
  font-size: 26rpx;
}

.order-checkout__fee-row + .order-checkout__fee-row {
  margin-top: 16rpx;
}

.order-checkout__submit {
  position: fixed;
  left: 0;
  right: 0;
  bottom: 0;
  display: flex;
  align-items: center;
  gap: 18rpx;
  padding: 20rpx 24rpx calc(20rpx + env(safe-area-inset-bottom));
  background: rgba(255, 255, 255, 0.94);
}

.order-checkout__submit-btn {
  flex: 1;
}
</style>
