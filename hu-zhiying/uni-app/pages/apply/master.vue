<template>
  <view class="page-shell">
    <view class="card apply-page__section">
      <view class="apply-page__row">
        <text>姓名</text>
        <input v-model="form.name" placeholder="请输入真实姓名" />
      </view>
      <view class="apply-page__row">
        <text>手机号</text>
        <input v-model="form.mobile" placeholder="请输入手机号" />
      </view>
      <view class="apply-page__row">
        <text>技能方向</text>
        <input v-model="form.skills" placeholder="如空调维修、智能锁安装" />
      </view>
      <view class="apply-page__row">
        <text>服务区域</text>
        <input v-model="form.area" placeholder="请输入常驻服务区域" />
      </view>
      <view class="apply-page__row apply-page__row--column">
        <text>资质说明</text>
        <textarea v-model="form.remark" class="apply-page__textarea" placeholder="可补充从业年限、证书情况和工具配备"></textarea>
      </view>
    </view>

    <button class="primary-btn" :loading="submitting" @tap="submit">提交入驻申请</button>
  </view>
</template>

<script setup>
import { reactive, ref } from 'vue';
import { applyMaster } from '../../api/master';
import { useUserStore } from '../../stores/user';

const userStore = useUserStore();
const submitting = ref(false);

const form = reactive({
  name: '',
  mobile: '',
  skills: '空调维修 / 智能锁安装',
  area: '上海市浦东新区',
  remark: '',
});

async function submit() {
  if (!form.name || !form.mobile || !form.skills || !form.area) {
    uni.showToast({ title: '请补全入驻信息', icon: 'none' });
    return;
  }

  submitting.value = true;
  try {
    await applyMaster({
      realName: form.name,
      mobile: form.mobile,
      skills: form.skills,
      area: form.area,
    });
    userStore.switchRole('master');
    userStore.updateProfile({
      nickname: form.name,
      mobile: form.mobile,
      level: '认证工程师',
    });
    uni.showToast({ title: '申请已提交，已切换到师傅端', icon: 'none' });
    setTimeout(() => {
      uni.navigateTo({ url: '/pages-master/master/dispatch' });
    }, 400);
  } finally {
    submitting.value = false;
  }
}
</script>

<style scoped>
.apply-page__section {
  padding: 28rpx;
}

.apply-page__row {
  display: flex;
  align-items: center;
  gap: 20rpx;
  padding: 24rpx 0;
  border-bottom: 1rpx solid #edf1f6;
}

.apply-page__row text {
  width: 150rpx;
  font-size: 26rpx;
}

.apply-page__row input {
  flex: 1;
  height: 56rpx;
  font-size: 26rpx;
}

.apply-page__row--column {
  align-items: flex-start;
}

.apply-page__textarea {
  flex: 1;
  min-height: 200rpx;
  padding: 18rpx;
  border-radius: 22rpx;
  background: #f4f6f9;
  font-size: 26rpx;
}
</style>
