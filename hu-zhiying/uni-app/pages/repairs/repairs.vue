<template>
  <view class="page-container flex-column">
    <view class="bg-glow-1"></view>
    <view class="bg-glow-2"></view>

    <view class="nav-bar">
      <view class="status-bar"></view>
      <view class="nav-header flex-center-between">
        <uni-icons type="left" size="22" color="#FFFFFF" class="back-icon" @click="goBack"></uni-icons>
        <text class="nav-title">极速报修</text>
        <view class="placeholder-box"></view>
      </view>
    </view>

    <scroll-view 
      class="content-scroll flex-1" 
      scroll-y 
      scroll-with-animation
    >
      <view class="scroll-inner-wrapper">
        <view class="radar-section flex-column flex-center">
          <view class="voice-orb" @touchstart="startVoice" @touchend="endVoice" hover-class="orb-hover">
            <view class="orb-core flex-center">
              <uni-icons type="mic-filled" size="36" color="#0DF2C9"></uni-icons>
            </view>
            <view class="ripple ripple-1"></view>
            <view class="ripple ripple-2"></view>
          </view>
          <text class="orb-hint">按住 告诉我们什么坏了</text>
        </view>

        <view class="glass-card">
          <view class="input-container">
            <textarea 
              class="fault-textarea" 
              placeholder="也可在此输入，或直接补充照片/视频..." 
              placeholder-class="ph-style"
              v-model="faultDescription"
              :maxlength="200"
              :show-confirm-bar="false"
            />
            <view class="media-dock flex-center-start">
              <view class="upload-btn flex-center" hover-class="btn-hover" @click="chooseMedia">
                <uni-icons type="camera-filled" size="20" color="#A0A5B1"></uni-icons>
              </view>
              <text class="word-limit">{{ faultDescription.length }}/200</text>
            </view>
          </view>

          <view class="tag-list flex-row">
            <view 
              class="tag-item" 
              v-for="(tag, index) in faultTags" 
              :key="index"
              :class="{ 'tag-active': selectedTags.includes(index) }"
              @click="toggleTag(index)"
            >
              {{ tag }}
            </view>
          </view>
        </view>

        <view class="glass-card info-card">
          <view class="list-item flex-center-between" hover-class="item-hover" @click="chooseAddress">
            <view class="item-left flex-center-start">
              <view class="icon-box"><uni-icons type="location-filled" size="18" color="#0DF2C9"></uni-icons></view>
              <view class="item-content flex-column">
                <text class="main-text text-ellipsis">金茂府 3栋 1204室</text>
                <text class="sub-text">许先生 138****8888</text>
              </view>
            </view>
            <uni-icons type="right" size="16" color="#5C6270"></uni-icons>
          </view>

          <view class="line-divider"></view>

          <view class="list-item flex-center-between" hover-class="item-hover" @click="chooseTime">
            <view class="item-left flex-center-start">
              <view class="icon-box"><uni-icons type="calendar-filled" size="18" color="#0DF2C9"></uni-icons></view>
              <view class="item-content flex-column">
                <text class="sub-text">期望上门</text>
                <text class="main-text text-glow">尽快 (预计30分钟内)</text>
              </view>
            </view>
            <uni-icons type="right" size="16" color="#5C6270"></uni-icons>
          </view>
        </view>
        
        <view class="bottom-visual-space"></view>
      </view>
    </scroll-view>

    <view class="footer-dock">
      <view class="price-info flex-column">
        <text class="price-label">
          预估检测费 
          <uni-icons type="info-circle" size="12" color="#8A8F99" style="margin-left: 4rpx;"></uni-icons>
        </text>
        <view class="price-value">
          <text class="symbol">￥</text>
          <text class="num">30</text>
        </view>
      </view>
      
      <button class="btn-submit flex-center" hover-class="btn-submit-hover" @click="submitRepair">
        极速呼叫
      </button>
    </view>
  </view>
</template>

<script setup>
import { ref } from 'vue';

const faultDescription = ref('');
const faultTags = ref(['不制冷/不热', '漏水/滴水', '异响/噪音大', '开不了机', '跳闸/漏电', '部件脱落']);
const selectedTags = ref([]);

const goBack = () => uni.navigateBack();

const toggleTag = (index) => {
  const pos = selectedTags.value.indexOf(index);
  if (pos > -1) selectedTags.value.splice(pos, 1);
  else selectedTags.value.push(index);
};

const startVoice = () => { uni.vibrateShort(); console.log('开始录音波纹动画'); };
const endVoice = () => { faultDescription.value += '外机漏水，噪音特别大。'; };
const chooseMedia = () => { console.log('选择媒体'); };
const chooseAddress = () => { console.log('选择地址'); };
const chooseTime = () => { console.log('选择时间'); };
const submitRepair = () => { console.log('提交'); };
</script>

<style lang="scss" scoped>
/* 全局强力盒模型重置，彻底杜绝宽度溢出 */
view, scroll-view, text, textarea, button {
  box-sizing: border-box;
}

.page-container {
  font-family: -apple-system, "PingFang SC", "Helvetica Neue", Arial, sans-serif;
  height: 100vh;
  height: 100dvh;
  background-color: #0B0E14; 
  position: relative;
  overflow: hidden; 
  color: #FFFFFF;
}

.flex-column { display: flex; flex-direction: column; }
.flex-row { display: flex; flex-direction: row; flex-wrap: wrap; }
.flex-center { display: flex; align-items: center; justify-content: center; }
.flex-center-start { display: flex; align-items: center; justify-content: flex-start; }
.flex-center-between { display: flex; align-items: center; justify-content: space-between; }
.flex-1 { flex: 1; }
.text-ellipsis { overflow: hidden; white-space: nowrap; text-overflow: ellipsis; }

/* 氛围光效 */
.bg-glow-1 {
  position: absolute; top: -10%; left: -10%;
  width: 500rpx; height: 500rpx;
  background: radial-gradient(circle, rgba(13,242,201,0.15) 0%, rgba(0,0,0,0) 70%);
  z-index: 0; pointer-events: none; 
}
.bg-glow-2 {
  position: absolute; top: 30%; right: -20%;
  width: 600rpx; height: 600rpx;
  background: radial-gradient(circle, rgba(43,92,255,0.1) 0%, rgba(0,0,0,0) 70%);
  z-index: 0; pointer-events: none;
}

/* 导航栏 */
.nav-bar {
  background-color: transparent;
  position: relative; z-index: 10;
  flex-shrink: 0; 
  .status-bar { height: var(--status-bar-height); }
  .nav-header {
    height: 88rpx; padding: 0 32rpx;
    .nav-title { font-size: 32rpx; font-weight: 500; color: #FFFFFF; letter-spacing: 1px; }
    .placeholder-box { width: 44rpx; }
  }
}

/* 滚动区 */
.content-scroll { 
  height: 0; 
  width: 100%;
  position: relative; 
  z-index: 10; 
}
.scroll-inner-wrapper { padding-bottom: 20rpx; }

/* 雷达交互 */
.radar-section {
  padding: 50rpx 0;
  .voice-orb {
    position: relative; width: 160rpx; height: 160rpx; margin-bottom: 24rpx;
    .orb-core {
      position: absolute; top: 0; left: 0; width: 100%; height: 100%;
      background: linear-gradient(135deg, rgba(255,255,255,0.1) 0%, rgba(255,255,255,0.02) 100%);
      border: 1px solid rgba(13,242,201,0.4); border-radius: 50%;
      box-shadow: 0 0 30rpx rgba(13,242,201,0.2), inset 0 0 20rpx rgba(13,242,201,0.1);
      z-index: 3; backdrop-filter: blur(10px);
    }
    .ripple {
      position: absolute; top: 50%; left: 50%; transform: translate(-50%, -50%);
      border-radius: 50%; border: 1px solid rgba(13,242,201,0.5); z-index: 1;
      animation: pulse 2.5s infinite cubic-bezier(0.215, 0.61, 0.355, 1);
    }
    .ripple-1 { width: 100%; height: 100%; }
    .ripple-2 { width: 100%; height: 100%; animation-delay: 1.25s; }
  }
  .orb-hover .orb-core { transform: scale(0.95); transition: all 0.2s; box-shadow: 0 0 50rpx rgba(13,242,201,0.5); }
  .orb-hint { font-size: 26rpx; color: #A0A5B1; letter-spacing: 1px; }
}

@keyframes pulse { 0% { width: 160rpx; height: 160rpx; opacity: 1; } 100% { width: 300rpx; height: 300rpx; opacity: 0; } }

/* 玻璃卡片 */
.glass-card {
  background: rgba(255, 255, 255, 0.03); 
  backdrop-filter: blur(24px); -webkit-backdrop-filter: blur(24px);
  border: 1px solid rgba(255, 255, 255, 0.08); border-radius: 24rpx;
  margin: 0 32rpx 32rpx; padding: 32rpx;
  box-shadow: 0 8rpx 32rpx rgba(0, 0, 0, 0.2);
}

.input-container {
  margin-bottom: 32rpx;
  .fault-textarea { width: 100%; height: 140rpx; font-size: 28rpx; color: #FFFFFF; line-height: 1.6; }
  .ph-style { color: #5C6270; }
  .media-dock {
    border-top: 1px dashed rgba(255,255,255,0.1); padding-top: 20rpx; justify-content: space-between;
    .upload-btn { width: 64rpx; height: 64rpx; background: rgba(255,255,255,0.05); border-radius: 12rpx; transition: all 0.2s; }
    .btn-hover { background: rgba(255,255,255,0.1); }
    .word-limit { font-size: 22rpx; color: #5C6270; font-family: din; }
  }
}

.tag-list {
  gap: 20rpx 16rpx;
  .tag-item {
    padding: 12rpx 28rpx; background: rgba(255, 255, 255, 0.05); color: #A0A5B1; font-size: 24rpx;
    border-radius: 30rpx; border: 1px solid rgba(255,255,255,0.05); transition: all 0.3s ease;
  }
  .tag-active {
    background: rgba(13, 242, 201, 0.1); color: #0DF2C9; border-color: rgba(13, 242, 201, 0.4);
    box-shadow: 0 0 16rpx rgba(13, 242, 201, 0.15); 
  }
}

.info-card {
  padding: 16rpx 32rpx;
  .list-item {
    padding: 24rpx 0; transition: opacity 0.2s;
    .icon-box {
      width: 64rpx; height: 64rpx; background: rgba(13,242,201,0.05); border: 1px solid rgba(13,242,201,0.1);
      border-radius: 16rpx; display: flex; align-items: center; justify-content: center; margin-right: 24rpx;
    }
    .item-content {
      .main-text { font-size: 28rpx; font-weight: 500; color: #FFFFFF; margin-bottom: 6rpx; }
      .sub-text { font-size: 24rpx; color: #8A8F99; }
      .text-glow { color: #0DF2C9; text-shadow: 0 0 10rpx rgba(13,242,201,0.3); } 
    }
  }
  .item-hover { opacity: 0.7; }
  .line-divider { height: 1px; background: rgba(255,255,255,0.05); margin-left: 88rpx; }
}

.bottom-visual-space { height: 40rpx; }

/* ================== 全新设计的底部 Dock 栏 ================== */
/* 抛弃 absolute，采用正常 flex 布局，左右分栏，高级感拉满 */
.footer-dock {
  flex-shrink: 0; 
  width: 100%;
  background: rgba(14, 18, 26, 0.9); /* 更深沉的底色，托住整个页面 */
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
  border-top: 1px solid rgba(255, 255, 255, 0.06);
  display: flex;
  justify-content: space-between;
  align-items: center;
  /* 完美的内边距：上24 左右32 下部留出安全区并加一点缓冲 */
  padding: 24rpx 32rpx 24rpx 32rpx;
  z-index: 100;
  
  /* 左侧：价格区 */
  .price-info {
    justify-content: center;
    .price-label { 
      font-size: 24rpx; 
      color: #8A8F99; 
      margin-bottom: 4rpx;
      display: flex;
      align-items: center;
    }
    .price-value {
      color: #FFFFFF;
      display: flex;
      align-items: baseline;
      .symbol { font-size: 26rpx; font-weight: 600; margin-right: 2rpx;}
      .num { font-size: 46rpx; font-weight: bold; font-family: din, sans-serif; line-height: 1; }
    }
  }
  
  /* 右侧：行动按钮 (改为更精致的胶囊形状) */
  .btn-submit {
    margin: 0; /* 清除默认边距 */
    width: 280rpx; /* 固定优雅宽度 */
    height: 88rpx;
    background: linear-gradient(90deg, #0DF2C9 0%, #08BCA0 100%);
    color: #0B0E14;
    font-size: 32rpx; 
    font-weight: 600; 
    border-radius: 44rpx; /* 全圆角胶囊 */
    box-shadow: 0 8rpx 24rpx rgba(13,242,201,0.25); 
    border: none;
    &::after { display: none; }
  }
  .btn-submit-hover { 
    transform: scale(0.97); 
    box-shadow: 0 4rpx 12rpx rgba(13,242,201,0.4); 
    opacity: 0.9;
  }
}
</style>