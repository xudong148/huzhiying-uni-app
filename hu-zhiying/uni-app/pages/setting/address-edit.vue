<template>
  <view class="page-shell">
    <view class="card address-edit__section">
      <view class="address-edit__row">
        <text>联系人</text>
        <input v-model="form.name" placeholder="请输入联系人" />
      </view>
      <view class="address-edit__row">
        <text>手机号</text>
        <input v-model="form.mobile" type="number" maxlength="11" placeholder="请输入手机号" />
      </view>
      <view class="address-edit__row">
        <text>标签</text>
        <input v-model="form.tag" placeholder="家 / 公司 / 其他" />
      </view>
      <view class="address-edit__row address-edit__row--column">
        <text>详细地址</text>
        <textarea v-model="form.address" class="address-edit__textarea" placeholder="支持用当前位置回填，也支持按当前城市做服务范围校验。" />
      </view>
      <view class="address-edit__row">
        <text>设为默认</text>
        <switch :checked="form.default" color="#2B5CFF" @change="form.default = $event.detail.value" />
      </view>
    </view>

    <view class="address-edit__actions">
      <button class="secondary-btn" @tap="safePickLocation">使用当前位置</button>
      <button class="secondary-btn" @tap="safeParseAddress">服务范围校验</button>
    </view>

    <button class="primary-btn" :loading="submitting" @tap="safeSave">保存地址</button>
  </view>
</template>

<script setup>
import { reactive, ref } from 'vue';
import { onLoad } from '@dcloudio/uni-app';
import { checkServiceArea } from '../../api/map';
import { getAddressList, saveAddress } from '../../api/user';
import { useLocationStore } from '../../stores/location';
import { safeAsync, showActionError } from '../../utils/page-task';

const location = useLocationStore();
const editingId = ref('');
const submitting = ref(false);

const form = reactive({
  id: '',
  name: '',
  mobile: '',
  tag: '家',
  address: '',
  default: false,
});

function applyForm(payload = {}) {
  form.id = payload.id || '';
  form.name = payload.name || '';
  form.mobile = payload.mobile || '';
  form.tag = payload.tag || '家';
  form.address = payload.address || location.current.address || '';
  form.default = Boolean(payload.default);
}

async function pickLocation() {
  const current = await location.locate();
  form.address = current.address || form.address;
  uni.showToast({ title: '已填入当前位置', icon: 'none' });
}

async function parseAddress() {
  const current = await location.locate();
  const prefix = `${current.city}${current.district}`;

  if (form.address && !form.address.startsWith(prefix)) {
    form.address = `${prefix}${form.address.replace(/^(上海市|杭州市|苏州市|南京市)/, '')}`;
  } else if (!form.address) {
    form.address = current.address || '';
  }

  const areaRes = await checkServiceArea({
    city: current.city,
    district: current.district,
  });

  uni.showToast({
    title: areaRes.data?.serviceable
      ? `已匹配 ${areaRes.data.matchedZone || `${current.city}${current.district}`}`
      : '当前地址暂不在服务范围',
    icon: 'none',
  });
}

function isValidMobile(value) {
  return /^1\d{10}$/.test(String(value || '').trim());
}

async function save() {
  if (!form.name || !form.mobile || !form.address) {
    uni.showToast({ title: '请补全地址信息', icon: 'none' });
    return;
  }
  if (!isValidMobile(form.mobile)) {
    uni.showToast({ title: '请输入正确的手机号', icon: 'none' });
    return;
  }
  submitting.value = true;
  try {
    await saveAddress(form);
    uni.showToast({ title: '地址已保存', icon: 'none' });
    setTimeout(() => uni.navigateBack(), 400);
  } catch (error) {
    showActionError(error, '保存地址失败');
  } finally {
    submitting.value = false;
  }
}

const safePickLocation = safeAsync(pickLocation, '填入当前位置');
const safeParseAddress = safeAsync(parseAddress, '校验服务范围');
const safeSave = safeAsync(save, '保存地址');

onLoad(safeAsync(async (options) => {
  editingId.value = options.id || '';
  if (!editingId.value) {
    applyForm();
    return;
  }
  const res = await getAddressList();
  const current = (res.data || []).find((item) => String(item.id) === editingId.value);
  applyForm(current);
}, '初始化地址表单'));
</script>

<style scoped>
.address-edit__section {
  padding: 28rpx;
}

.address-edit__row {
  display: flex;
  align-items: center;
  gap: 20rpx;
  padding: 20rpx 0;
  border-bottom: 1rpx solid #edf1f6;
}

.address-edit__row text {
  width: 130rpx;
  font-size: 26rpx;
  color: #344054;
}

.address-edit__row input {
  flex: 1;
  height: 58rpx;
  font-size: 26rpx;
}

.address-edit__row--column {
  align-items: flex-start;
}

.address-edit__textarea {
  flex: 1;
  min-height: 180rpx;
  padding: 18rpx;
  border-radius: 18rpx;
  background: #f4f6f9;
  font-size: 26rpx;
  box-sizing: border-box;
}

.address-edit__actions {
  display: flex;
  gap: 16rpx;
  margin: 22rpx 0;
}

.address-edit__actions button {
  flex: 1;
}
</style>
