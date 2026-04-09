<template>
  <view class="publish-page">
    <view class="publish-card">
      <view class="publish-field">
        <text class="publish-label">所在城市</text>
        <input v-model="form.cityName" class="publish-input" maxlength="20" placeholder="例如：上海" />
      </view>

      <view class="publish-field">
        <text class="publish-label">帖子标题</text>
        <input v-model="form.title" class="publish-input" maxlength="30" placeholder="写一个明确的主题，方便同城用户判断是否值得点开" />
      </view>

      <view class="publish-field">
        <view class="publish-label-row">
          <text class="publish-label">正文内容</text>
          <text class="publish-counter">{{ form.content.length }}/500</text>
        </view>
        <textarea
          v-model="form.content"
          class="publish-textarea"
          maxlength="500"
          placeholder="把你的维修经历、避坑提醒、价格参考或师傅口碑写下来，越具体越容易帮助别人。"
        />
      </view>

      <view class="publish-field">
        <view class="publish-label-row">
          <text class="publish-label">配图链接</text>
          <text class="publish-counter">每行 1 张，可选</text>
        </view>
        <textarea
          v-model="form.imagesText"
          class="publish-textarea publish-textarea--small"
          maxlength="600"
          placeholder="暂时支持填图片 URL。后续如补本地上传，这里可以直接复用为图片选择区。"
        />
      </view>
    </view>

    <view class="publish-tips card">
      <view class="section-title">
        <text class="section-title__text">发布提醒</text>
      </view>
      <view class="publish-tip">1. 标题尽量带上设备或场景，比如“空调移机避坑”或“智能锁安装踩雷”。</view>
      <view class="publish-tip">2. 正文尽量带上价格区间、服务结果和是否推荐，方便后续做同城推荐。</view>
      <view class="publish-tip">3. 未发布的内容会自动保存在本机草稿，误返回不会丢。</view>
    </view>

    <button class="publish-submit" :loading="submitting" @tap="submitPost">发布帖子</button>
  </view>
</template>

<script setup>
import { computed, ref, watch } from 'vue';
import { onLoad, onUnload } from '@dcloudio/uni-app';
import { createCommunityPost } from '../../api/service';
import { useLocationStore } from '../../stores/location';
import { safeAsync } from '../../utils/page-task';

const DRAFT_KEY = 'hzy-community-draft';

const locationStore = useLocationStore();
const submitting = ref(false);
const submitted = ref(false);
const form = ref({
  cityName: '',
  title: '',
  content: '',
  imagesText: '',
});

const isDirty = computed(() =>
  Boolean(form.value.title.trim() || form.value.content.trim() || form.value.imagesText.trim()),
);

function loadDraft() {
  try {
    const raw = uni.getStorageSync(DRAFT_KEY);
    if (!raw) {
      return;
    }
    form.value = {
      ...form.value,
      ...(raw || {}),
    };
  } catch (error) {
    // ignore draft read failure
  }
}

function persistDraft() {
  try {
    uni.setStorageSync(DRAFT_KEY, {
      ...form.value,
      cityName: form.value.cityName.trim() || locationStore.current.city || '上海',
    });
  } catch (error) {
    // ignore draft write failure
  }
}

function clearDraft() {
  uni.removeStorageSync(DRAFT_KEY);
}

const submitPost = safeAsync(async () => {
  if (!form.value.title.trim()) {
    uni.showToast({ title: '请输入标题', icon: 'none' });
    return;
  }
  if (!form.value.content.trim()) {
    uni.showToast({ title: '请输入正文', icon: 'none' });
    return;
  }

  submitting.value = true;
  try {
    await createCommunityPost({
      cityName: form.value.cityName.trim() || locationStore.current.city || '上海',
      title: form.value.title.trim(),
      content: form.value.content.trim(),
      images: form.value.imagesText
        .split(/\r?\n/)
        .map((item) => item.trim())
        .filter(Boolean),
    });
    submitted.value = true;
    clearDraft();
    uni.showToast({ title: '发布成功', icon: 'none' });
    setTimeout(() => {
      uni.navigateBack();
    }, 400);
  } finally {
    submitting.value = false;
  }
}, '发布圈子帖子');

watch(
  () => form.value,
  () => {
    if (submitted.value) {
      return;
    }
    persistDraft();
  },
  { deep: true },
);

onLoad(async () => {
  form.value.cityName = locationStore.current.city || '上海';
  loadDraft();
  if (!form.value.cityName) {
    form.value.cityName = locationStore.current.city || '上海';
  }
});

onUnload(() => {
  if (!submitted.value && isDirty.value) {
    persistDraft();
  }
});
</script>

<style lang="scss" scoped>
.publish-page {
  min-height: 100vh;
  padding: 24rpx;
  box-sizing: border-box;
  background: linear-gradient(180deg, #f5faf8 0%, #f8fafc 100%);
}

.publish-card,
.publish-tips {
  padding: 28rpx;
  border-radius: 28rpx;
  background: rgba(255, 255, 255, 0.97);
  box-shadow: 0 14rpx 34rpx rgba(15, 23, 42, 0.06);
}

.publish-tips {
  margin-top: 20rpx;
}

.publish-field + .publish-field {
  margin-top: 24rpx;
}

.publish-label-row {
  display: flex;
  justify-content: space-between;
  gap: 16rpx;
  margin-bottom: 14rpx;
}

.publish-label {
  display: block;
  margin-bottom: 14rpx;
  font-size: 26rpx;
  color: #1f2937;
  font-weight: 600;
}

.publish-counter {
  font-size: 22rpx;
  color: #667085;
}

.publish-input,
.publish-textarea {
  width: 100%;
  box-sizing: border-box;
  padding: 22rpx 24rpx;
  border-radius: 22rpx;
  background: #f1f5f9;
  font-size: 26rpx;
  color: #111827;
}

.publish-textarea {
  min-height: 220rpx;
}

.publish-textarea--small {
  min-height: 160rpx;
}

.publish-tip {
  font-size: 24rpx;
  line-height: 1.7;
  color: #475467;
}

.publish-tip + .publish-tip {
  margin-top: 12rpx;
}

.publish-submit {
  margin-top: 28rpx;
  border: none;
  border-radius: 999rpx;
  background: #111827;
  color: #fff;
  font-size: 28rpx;
}
</style>
