<template>
  <view class="page-container">
    <scroll-view scroll-y class="order-checkout__scroll" :show-scrollbar="false">
      <view class="page-shell">
        <view class="card order-checkout__section pressable" @tap="goAddress">
          <view class="section-title">
            <text class="section-title__text">服务地址</text>
            <text class="section-title__desc">{{ selectedAddress.id ? '可点击切换' : '请先选择地址' }}</text>
          </view>
          <view class="order-checkout__title">{{ selectedAddress.address || '请选择服务地址' }}</view>
          <view class="muted">{{ selectedAddress.name || '联系人' }} / {{ selectedAddress.mobile || '手机号' }}</view>
        </view>

        <view class="card order-checkout__section">
          <view class="section-title">
            <text class="section-title__text">预约时间</text>
            <text class="section-title__desc">默认取平台最近可约时段</text>
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
            placeholder="请描述故障现象、使用年限或已尝试过的处理方式，方便师傅提前准备工具和备件。"
          />
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
        </view>

        <view class="card order-checkout__section">
          <view class="section-title">
            <text class="section-title__text">图片凭证</text>
            <text class="section-title__desc">{{ imageFiles.length }}/3</text>
          </view>
          <view class="order-checkout__media-grid">
            <view
              v-for="item in imageFiles"
              :key="item.fileId"
              class="order-checkout__media-item"
              @tap="previewImage(item.url)"
            >
              <image class="order-checkout__media-image" :src="item.url" mode="aspectFill" />
              <view class="order-checkout__media-delete" @tap.stop="removeImage(item.fileId)">×</view>
            </view>
            <view class="order-checkout__media-add" @tap="safeChooseImages">
              <text class="order-checkout__media-add-icon">+</text>
              <text class="order-checkout__media-add-text">{{ uploadingImages ? '上传中' : '上传图片' }}</text>
            </view>
          </view>
        </view>

        <view class="card order-checkout__section">
          <view class="section-title">
            <text class="section-title__text">录音凭证</text>
            <text class="section-title__desc">{{ audioFile ? '已上传' : '可选' }}</text>
          </view>
          <view class="order-checkout__audio">
            <view class="order-checkout__audio-info">
              <text class="order-checkout__audio-name">{{ audioFile?.originalName || '未上传录音文件' }}</text>
              <text class="muted">{{ audioFile ? '将随本次工单一起提交' : '支持 mp3 / m4a / amr' }}</text>
            </view>
            <view class="order-checkout__audio-actions">
              <button class="secondary-btn" size="mini" :loading="uploadingAudio" @tap="safeChooseAudio">上传录音</button>
              <button v-if="audioFile" class="secondary-btn" size="mini" @tap="clearAudio">清除</button>
            </view>
          </view>
        </view>

        <view class="card order-checkout__section">
          <view class="section-title">
            <text class="section-title__text">费用说明</text>
            <text class="section-title__desc">支付成功后平台开始派单</text>
          </view>
          <view class="order-checkout__fee-row"><text>预计服务费</text><text>¥{{ estimatedPrice.toFixed(2) }}</text></view>
          <view class="order-checkout__fee-row"><text>支付方式</text><text>微信支付</text></view>
          <view class="order-checkout__fee-row"><text>支付能力</text><text>{{ payCapabilityText }}</text></view>
        </view>
      </view>
      <view class="safe-bottom"></view>
    </scroll-view>

    <view class="order-checkout__submit">
      <view>
        <view class="muted">需支付预付款</view>
        <price-format :value="estimatedPrice" />
      </view>
      <button class="primary-btn order-checkout__submit-btn" :loading="submitting" @tap="safeSubmitOrder">确认并支付</button>
    </view>
  </view>
</template>

<script setup>
import { computed, ref, watch } from 'vue';
import { onLoad, onShow, onUnload } from '@dcloudio/uni-app';
import PriceFormat from '../../components/price-format.vue';
import { uploadMedia } from '../../api/file';
import { createServiceOrder, getTimeSlots, requestWechatPrepay } from '../../api/order';
import { getAddressList } from '../../api/user';
import { safeAsync } from '../../utils/page-task';
import { getRequestErrorMessage } from '../../utils/request';
import { isWechatPayCanceled, launchWechatPay } from '../../utils/wechat-pay';

const serviceItemId = ref(201);
const serviceTitle = ref('空调上门维修');
const selectedAddress = ref({});
const addressList = ref([]);
const slots = ref([]);
const selectedSlot = ref('');
const description = ref('室内机制冷效果差，外机有异响。');
const emergency = ref(false);
const nightService = ref(false);
const imageFiles = ref([]);
const audioFile = ref(null);
const uploadingImages = ref(false);
const uploadingAudio = ref(false);
const submitting = ref(false);
const submitted = ref(false);
const payEnabled = ref(null);

const estimatedPrice = computed(() => {
  let amount = 58;
  if (emergency.value) amount += 30;
  if (nightService.value) amount += 20;
  return amount;
});

const payCapabilityText = computed(() => {
  if (payEnabled.value === null) {
    return '待检测';
  }
  return payEnabled.value ? '已配置商户' : '未配置商户';
});

function getDraftKey() {
  return `hzy-checkout-draft-${serviceItemId.value}`;
}

function loadDraft() {
  try {
    const draft = uni.getStorageSync(getDraftKey());
    if (!draft) {
      return;
    }
    selectedSlot.value = draft.selectedSlot || selectedSlot.value;
    description.value = draft.description || description.value;
    emergency.value = Boolean(draft.emergency);
    nightService.value = Boolean(draft.nightService);
  } catch (error) {
    // ignore
  }
}

function persistDraft() {
  if (submitted.value) {
    return;
  }
  uni.setStorageSync(getDraftKey(), {
    selectedSlot: selectedSlot.value,
    description: description.value,
    emergency: emergency.value,
    nightService: nightService.value,
  });
}

function clearDraft() {
  uni.removeStorageSync(getDraftKey());
}

function goAddress() {
  uni.navigateTo({ url: '/pages/setting/address-list?select=1' });
}

function previewImage(url) {
  uni.previewImage({
    urls: imageFiles.value.map((item) => item.url),
    current: url,
  });
}

function removeImage(fileId) {
  imageFiles.value = imageFiles.value.filter((item) => item.fileId !== fileId);
}

function clearAudio() {
  audioFile.value = null;
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
  addressList.value = addressRes.data || [];
  slots.value = slotRes.data || [];
  if (!selectedSlot.value && slots.value.length) {
    selectedSlot.value = slots.value[0];
  }
  syncSelectedAddress();
  loadDraft();
}

const safeLoadBaseData = safeAsync(loadBaseData, '加载下单页');

async function chooseImages() {
  if (imageFiles.value.length >= 3) {
    uni.showToast({ title: '最多上传 3 张图片', icon: 'none' });
    return;
  }
  const count = 3 - imageFiles.value.length;
  const chooseRes = await uni.chooseMedia({
    count,
    mediaType: ['image'],
    sourceType: ['album', 'camera'],
  });
  uploadingImages.value = true;
  try {
    const uploadJobs = (chooseRes.tempFiles || []).map((item) => uploadMedia(item.tempFilePath, 'order_evidence'));
    const uploaded = await Promise.all(uploadJobs);
    imageFiles.value = [...imageFiles.value, ...uploaded.map((item) => item.data)];
  } finally {
    uploadingImages.value = false;
  }
}

async function chooseAudio() {
  const chooseRes = await uni.chooseMessageFile({
    count: 1,
    type: 'file',
  });
  const target = chooseRes.tempFiles?.[0];
  const filePath = target?.path || target?.tempFilePath;
  if (!filePath) {
    return;
  }
  uploadingAudio.value = true;
  try {
    const result = await uploadMedia(filePath, 'order_evidence');
    audioFile.value = result.data;
  } finally {
    uploadingAudio.value = false;
  }
}

const safeChooseImages = safeAsync(chooseImages, '上传报修图片');
const safeChooseAudio = safeAsync(chooseAudio, '上传录音凭证');

async function submitOrder() {
  if (!selectedAddress.value.id) {
    uni.showToast({ title: '请先选择服务地址', icon: 'none' });
    return;
  }
  if (!selectedSlot.value) {
    uni.showToast({ title: '请选择预约时间', icon: 'none' });
    return;
  }

  submitting.value = true;
  try {
    const evidenceFileIds = [
      ...imageFiles.value.map((item) => item.fileId),
      ...(audioFile.value ? [audioFile.value.fileId] : []),
    ];

    const createRes = await createServiceOrder({
      serviceItemId: serviceItemId.value,
      title: serviceTitle.value,
      appointment: selectedSlot.value,
      addressId: selectedAddress.value.id,
      description: description.value,
      emergency: emergency.value,
      nightService: nightService.value,
      evidenceFileIds,
    });

    const orderId = createRes.data.id;
    try {
      const prepayRes = await requestWechatPrepay(orderId);
      payEnabled.value = Boolean(prepayRes.data?.payEnabled);
      await launchWechatPay(prepayRes.data);
      uni.showToast({
        title: '支付结果确认中，请稍后查看订单状态',
        icon: 'none',
      });
    } catch (error) {
      payEnabled.value = false;
      if (isWechatPayCanceled(error)) {
        uni.showToast({ title: '你已取消支付，可稍后在订单详情继续支付', icon: 'none' });
      } else {
        uni.showToast({
          title: getRequestErrorMessage(error, '支付暂不可用'),
          icon: 'none',
        });
      }
    }

    submitted.value = true;
    clearDraft();
    setTimeout(() => {
      uni.redirectTo({ url: `/pages/order/detail?id=${orderId}` });
    }, 400);
  } finally {
    submitting.value = false;
  }
}

const safeSubmitOrder = safeAsync(submitOrder, '提交服务订单');

watch([selectedSlot, description, emergency, nightService], () => {
  persistDraft();
});

onLoad((options) => {
  serviceItemId.value = Number(options.serviceItemId || 201);
  serviceTitle.value = decodeURIComponent(options.title || '空调上门维修');
});

onShow(safeLoadBaseData);

onUnload(() => {
  if (!submitted.value) {
    persistDraft();
  }
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
  box-sizing: border-box;
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

.order-checkout__media-grid {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 14rpx;
}

.order-checkout__media-item,
.order-checkout__media-add {
  position: relative;
  height: 180rpx;
  border-radius: 22rpx;
  overflow: hidden;
}

.order-checkout__media-item {
  background: #e9eef8;
}

.order-checkout__media-image {
  width: 100%;
  height: 100%;
}

.order-checkout__media-delete {
  position: absolute;
  top: 8rpx;
  right: 8rpx;
  width: 40rpx;
  height: 40rpx;
  border-radius: 50%;
  background: rgba(28, 30, 38, 0.7);
  color: #ffffff;
  display: flex;
  align-items: center;
  justify-content: center;
}

.order-checkout__media-add {
  background: #eef2ff;
  color: #2b5cff;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.order-checkout__media-add-icon {
  font-size: 42rpx;
  font-weight: 700;
}

.order-checkout__media-add-text {
  font-size: 22rpx;
}

.order-checkout__audio {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 20rpx;
}

.order-checkout__audio-info {
  flex: 1;
}

.order-checkout__audio-name {
  display: block;
  font-size: 26rpx;
  font-weight: 700;
}

.order-checkout__audio-actions {
  display: flex;
  gap: 12rpx;
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
