<template>
  <view class="page-container flex-column">
    <view class="nav-header">
      <view class="status-bar"></view>
      <view class="header-content flex-center-between">
        <view class="location-box flex-center-start" @tap="chooseLocation">
          <uni-icons type="location-filled" size="18" color="#111"></uni-icons>
          <text class="location-text text-ellipsis">金茂府</text>
          <uni-icons type="bottom" size="12" color="#111" class="arrow-down"></uni-icons>
        </view>
        <view class="action-icons flex-center">
          <uni-icons type="chat" size="24" color="#111"></uni-icons>
        </view>
      </view>
    </view>

    <scroll-view class="main-scroll flex-1" scroll-y scroll-with-animation>
      <view class="scroll-inner">
        
        <view class="greeting-section">
          <text class="greeting-title">早上好，许先生</text>
          <text class="greeting-sub">呼之应管家，随时为您服务</text>
          
          <view class="search-bar flex-center-start" @tap="goToSearch">
            <uni-icons type="search" size="18" color="#A0A5B1"></uni-icons>
            <text class="placeholder">搜一下需要的服务，如 "修空调"</text>
          </view>
        </view>

        <view class="express-card flex-row flex-center-between" @tap="goToQuickRepair">
          <view class="express-left flex-column">
            <text class="express-title">极速报修</text>
            <text class="express-desc">最快30分钟上门 · 90天售后</text>
          </view>
          <view class="express-btn flex-center">
            <uni-icons type="mic-filled" size="18" color="#FFF"></uni-icons>
            <text>一键呼叫</text>
          </view>
        </view>

        <view class="category-grid">
          <view class="grid-item flex-column flex-center" v-for="(nav, index) in navList" :key="index">
            <view class="icon-box flex-center">
              <image :src="nav.icon" mode="aspectFit" class="nav-icon"></image>
            </view>
            <text class="nav-text">{{ nav.name }}</text>
          </view>
        </view>

        <view class="banner-section">
          <swiper class="swiper-box" circular autoplay interval="4000">
            <swiper-item v-for="(banner, index) in bannerList" :key="index">
              <image :src="banner.img" mode="aspectFill" class="banner-img"></image>
            </swiper-item>
          </swiper>
        </view>

        <view class="recommend-section">
          <view class="section-header flex-center-between">
            <text class="title">精选服务</text>
            <text class="more" @tap="goToAllServices">全部服务 <uni-icons type="right" size="14" color="#8A8F99"></uni-icons></text>
          </view>

          <view class="service-list">
            <view class="service-card flex-row" v-for="(item, index) in hotServices" :key="index" hover-class="card-hover">
              <image :src="item.img" mode="aspectFill" class="service-img"></image>
              <view class="service-info flex-column flex-1">
                <text class="service-name">{{ item.name }}</text>
                <text class="service-desc">{{ item.desc }}</text>
                <view class="tags flex-row">
                  <text class="tag" v-for="(tag, tIdx) in item.tags" :key="tIdx">{{ tag }}</text>
                </view>
                <view class="price-action flex-center-between">
                  <view class="price-box">
                    <text class="unit">￥</text><text class="price">{{ item.price }}</text><text class="suffix">{{ item.unit }}</text>
                  </view>
                  <view class="book-btn flex-center">预约</view>
                </view>
              </view>
            </view>
          </view>
        </view>
        
        <view class="safe-bottom-space"></view>
      </view>
    </scroll-view>
  </view>
</template>

<script setup>
import { ref } from 'vue';

// 导航数据 - 纯 O2O 本地生活服务
const navList = ref([
  { name: '家电维修', icon: 'https://api.iconify.design/solar:washing-machine-bold-duotone.svg?color=%232B5CFF' },
  { name: '日常保洁', icon: 'https://api.iconify.design/solar:broom-bold-duotone.svg?color=%232B5CFF' },
  { name: '上门安装', icon: 'https://api.iconify.design/solar:screwdrive-bold-duotone.svg?color=%232B5CFF' },
  { name: '管道疏通', icon: 'https://api.iconify.design/solar:waterdrop-bold-duotone.svg?color=%232B5CFF' },
  { name: '全屋翻新', icon: 'https://api.iconify.design/solar:home-smile-angle-bold-duotone.svg?color=%232B5CFF' },
]);

// 运营位轮播图
const bannerList = ref([
  { img: 'https://picsum.photos/700/260?random=1' },
  { img: 'https://picsum.photos/700/260?random=2' },
]);

// 热门服务流 - 突出服务价值和价格规范
const hotServices = ref([
  {
    name: '空调深度拆洗杀菌',
    desc: '高温高压蒸汽杀菌，除异味防过敏',
    img: 'https://picsum.photos/200/200?random=11',
    tags: ['金牌技师', '不净全退'],
    price: '89',
    unit: '起/台'
  },
  {
    name: '日常居家保洁 (3小时)',
    desc: '全屋表面除尘除垢，厨卫重点清洁',
    img: 'https://picsum.photos/200/200?random=12',
    tags: ['背景调查', '自带工具'],
    price: '135',
    unit: '/次'
  },
  {
    name: '智能马桶盖安装',
    desc: '含旧件拆除，水电安全检测',
    img: 'https://picsum.photos/200/200?random=13',
    tags: ['专业电工', '乱收费双倍赔'],
    price: '80',
    unit: '起'
  },
  {
    name: '指纹密码锁安装/维修',
    desc: '支持99%主流品牌防盗门改装',
    img: 'https://picsum.photos/200/200?random=14',
    tags: ['极速响应', '持证上岗'],
    price: '120',
    unit: '起'
  }
]);

// 路由跳转模拟
const chooseLocation = () => console.log('选择服务地址');
const goToSearch = () => console.log('跳转搜索页');
const goToQuickRepair = () => console.log('跳转 -> 极速报修页面');
const goToAllServices = () => console.log('跳转 -> 全部服务分类页面');

</script>

<style lang="scss" scoped>
/* 全局重置 */
view, scroll-view, text, image, button { box-sizing: border-box; }

.page-container {
  font-family: -apple-system, "PingFang SC", "Helvetica Neue", Arial, sans-serif;
  height: 100vh;
  height: 100dvh;
  background-color: #F4F5F8; /* 干净的高级灰白底色 */
  display: flex;
  flex-direction: column;
}

/* Flex 快捷类 */
.flex-column { display: flex; flex-direction: column; }
.flex-row { display: flex; flex-direction: row; }
.flex-center { display: flex; align-items: center; justify-content: center; }
.flex-center-start { display: flex; align-items: center; justify-content: flex-start; }
.flex-center-between { display: flex; align-items: center; justify-content: space-between; }
.flex-1 { flex: 1; }
.text-ellipsis { overflow: hidden; white-space: nowrap; text-overflow: ellipsis; }

/* 品牌色 - 呼之应专属科技信任蓝 */
$brand-blue: #2B5CFF;
$text-main: #111111;
$text-sub: #5C6370;

/* --- 1. 顶部导航区 --- */
.nav-header {
  background-color: #FFFFFF;
  flex-shrink: 0;
  .status-bar { height: var(--status-bar-height); }
  .header-content {
    height: 88rpx;
    padding: 0 32rpx;
    
    .location-box {
      background: #F4F5F8;
      padding: 10rpx 20rpx;
      border-radius: 30rpx;
      max-width: 300rpx;
      .location-text { font-size: 28rpx; font-weight: 600; color: $text-main; margin: 0 8rpx; }
      .arrow-down { margin-top: 4rpx; }
    }
  }
}

/* --- 主滚动区 --- */
.main-scroll { height: 0; width: 100%; }
.scroll-inner { padding: 0 0 40rpx; }

/* --- 2. 问候与搜索 --- */
.greeting-section {
  background-color: #FFFFFF;
  padding: 20rpx 32rpx 40rpx;
  border-radius: 0 0 32rpx 32rpx; /* 底部圆角，营造层次感 */
  
  .greeting-title { font-size: 40rpx; font-weight: bold; color: $text-main; display: block; margin-bottom: 8rpx; }
  .greeting-sub { font-size: 26rpx; color: $text-sub; display: block; margin-bottom: 32rpx; }
  
  .search-bar {
    width: 100%; height: 80rpx;
    background-color: #F4F5F8;
    border-radius: 16rpx;
    padding: 0 24rpx;
    .placeholder { font-size: 28rpx; color: #A0A5B1; margin-left: 12rpx; }
  }
}

/* --- 3. 极速报修卡片 (C位视觉引导) --- */
.express-card {
  margin: -24rpx 32rpx 32rpx; /* 向上浮动，压在白底上 */
  background: linear-gradient(135deg, #2B5CFF 0%, #1A3673 100%);
  border-radius: 24rpx;
  padding: 32rpx 32rpx;
  box-shadow: 0 12rpx 32rpx rgba(43, 92, 255, 0.2);
  position: relative;
  z-index: 10;
  
  .express-left {
    .express-title { font-size: 36rpx; font-weight: bold; color: #FFFFFF; margin-bottom: 8rpx; }
    .express-desc { font-size: 24rpx; color: rgba(255,255,255,0.8); }
  }
  
  .express-btn {
    background: rgba(255,255,255,0.15);
    backdrop-filter: blur(8px);
    border: 1px solid rgba(255,255,255,0.3);
    padding: 16rpx 28rpx;
    border-radius: 40rpx;
    color: #FFFFFF;
    font-size: 26rpx;
    font-weight: 600;
    text { margin-left: 6rpx; }
  }
}

/* --- 4. 金刚区 (服务大类) --- */
.category-grid {
  display: flex;
  justify-content: space-between;
  padding: 0 40rpx;
  margin-bottom: 40rpx;
  
  .grid-item {
    .icon-box {
      width: 96rpx; height: 96rpx;
      background-color: #FFFFFF;
      border-radius: 32rpx;
      margin-bottom: 12rpx;
      box-shadow: 0 4rpx 16rpx rgba(0,0,0,0.02);
      .nav-icon { width: 56rpx; height: 56rpx; }
    }
    .nav-text { font-size: 24rpx; color: #333; font-weight: 500; }
  }
}

/* --- 5. 运营 Banner --- */
.banner-section {
  padding: 0 32rpx;
  margin-bottom: 40rpx;
  .swiper-box { height: 200rpx; border-radius: 20rpx; overflow: hidden; transform: translateY(0); }
  .banner-img { width: 100%; height: 100%; background-color: #EAEBEC; }
}

/* --- 6. 热门服务列表 --- */
.recommend-section {
  padding: 0 32rpx;
  
  .section-header {
    margin-bottom: 24rpx;
    .title { font-size: 34rpx; font-weight: bold; color: $text-main; }
    .more { font-size: 24rpx; color: #8A8F99; display: flex; align-items: center; }
  }
  
  .service-list {
    .service-card {
      background: #FFFFFF;
      border-radius: 20rpx;
      padding: 24rpx;
      margin-bottom: 20rpx;
      box-shadow: 0 4rpx 20rpx rgba(0,0,0,0.02);
      transition: all 0.2s;
      
      .service-img {
        width: 180rpx; height: 180rpx;
        border-radius: 12rpx;
        background-color: #F4F5F8;
        margin-right: 24rpx;
        flex-shrink: 0;
      }
      
      .service-info {
        justify-content: space-between;
        .service-name { font-size: 30rpx; font-weight: 600; color: $text-main; margin-bottom: 6rpx; }
        .service-desc { font-size: 24rpx; color: $text-sub; margin-bottom: 12rpx; display: -webkit-box; -webkit-box-orient: vertical; -webkit-line-clamp: 1; overflow: hidden; }
        
        .tags {
          flex-wrap: wrap; margin-bottom: 16rpx;
          .tag {
            font-size: 20rpx; color: $brand-blue;
            background: rgba(43, 92, 255, 0.08);
            padding: 4rpx 12rpx; border-radius: 8rpx;
            margin-right: 12rpx; margin-bottom: 8rpx;
          }
        }
        
        .price-action {
          .price-box {
            color: #FF4A4A;
            .unit { font-size: 24rpx; font-weight: bold; }
            .price { font-size: 38rpx; font-weight: bold; font-family: din; }
            .suffix { font-size: 22rpx; color: #A0A5B1; margin-left: 4rpx; }
          }
          .book-btn {
            background: #111111; color: #FFFFFF;
            width: 100rpx; height: 56rpx;
            border-radius: 28rpx; font-size: 26rpx; font-weight: 500;
          }
        }
      }
    }
    .card-hover { transform: scale(0.98); opacity: 0.9; }
  }
}

.safe-bottom-space { height: calc(60rpx + env(safe-area-inset-bottom)); }
</style>