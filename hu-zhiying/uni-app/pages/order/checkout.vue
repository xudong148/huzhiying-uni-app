<template>
  <view class="page-container">
    <scroll-view scroll-y class="order-checkout__scroll" :show-scrollbar="false">
      <view class="page-shell">
        <view class="card order-checkout__section pressable" @tap="goAddress">
          <view class="section-title">
            <text class="section-title__text">服务地址</text>
          </view>
          <view class="order-checkout__title">{{ selectedAddress.address }}</view>
          <view class="muted">{{ selectedAddress.name }} · {{ selectedAddress.mobile }}</view>
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
            <text class="section-title__text">故障描述</text>
            <text class="section-title__desc">支持文本、图片、录音</text>
          </view>
          <textarea v-model="description" class="order-checkout__textarea" placeholder="请简要描述故障现象，师傅会据此准备工具和配件"></textarea>
          <view class="order-checkout__upload-row">
            <view class="order-checkout__upload">上传图片</view>
            <view class="order-checkout__upload">录音说明</view>
          </view>
        </view>

        <view class="card order-checkout__section">
          <view class="section-title">
            <text class="section-title__text">费用明细</text>
          </view>
          <view class="order-checkout__fee-row"><text>基础检测费</text><text>¥58</text></view>
          <view class="order-checkout__fee-row"><text>预估上门费</text><text>¥30</text></view>
          <view class="order-checkout__fee-row"><text>优惠券抵扣</text><text>-¥30</text></view>
        </view>
      </view>
      <view class="safe-bottom"></view>
    </scroll-view>

    <view class="order-checkout__submit">
      <view>
        <view class="muted">需支付预付款</view>
        <price-format :value="58"></price-format>
      </view>
      <button class="primary-btn order-checkout__submit-btn" @tap="submitOrder">确认并支付</button>
    </view>
  </view>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import PriceFormat from '../../components/price-format.vue';
import { getTimeSlots } from '../../api/order';
import { getAddressList } from '../../api/user';

const selectedAddress = ref({});
const slots = ref([]);
const selectedSlot = ref('');
const description = ref('客厅空调制冷效果很差，外机有异响。');

function goAddress() {
  uni.navigateTo({ url: '/pages/setting/address-list' });
}

function submitOrder() {
  uni.showToast({
    title: '已生成预付款订单',
    icon: 'none',
  });
  setTimeout(() => {
    uni.switchTab({ url: '/pages/order/list' });
  }, 500);
}

onMounted(async () => {
  const [addressRes, slotRes] = await Promise.all([getAddressList(), getTimeSlots()]);
  selectedAddress.value = addressRes.data[0];
  slots.value = slotRes.data;
  selectedSlot.value = slotRes.data[0];
});
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
