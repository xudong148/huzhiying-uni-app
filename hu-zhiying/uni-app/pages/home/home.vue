<template>
  <view class="page-container flex-column">
    <view class="global-gradient-bg"></view>

    <view
      class="fixed-title-layer flex-center-start"
      :style="{
        paddingTop: titleLayerTop,
        height: titleLayerHeight,
        paddingLeft: pageBase.capsuleInfo.pagePadding,
        opacity: titleOpacity,
      }">
      <text class="header-name">{{ projectInfo.name }}</text>
      <text class="header-slogan">{{ projectInfo.slogan }}</text>
    </view>

    <scroll-view
      scroll-y
      class="page-main flex-1"
      :show-scrollbar="false"
      refresher-enabled
      :refresher-triggered="isRefreshing"
      @refresherrefresh="handlePullDownRefresh"
      @scrolltolower="handleReachBottom"
      @scroll="handleScroll">
      
      <view class="header-placeholder" :style="{ height: headerPlaceholderHeight }"></view>

      <view class="sticky-search-wrapper" :style="{ top: searchStickyTop }">
        <view class="sticky-white-bg" :style="{ opacity: bgOpacity }"></view>
        <view
          class="search-layout-container"
          :style="{
            paddingLeft: searchPaddingLeft,
            paddingRight: searchPaddingRight,
            paddingBottom: isStuck ? '0px' : '10rpx',
          }">
          <view
            class="search-box flex-between-center"
            :class="{ 'is-stuck': isStuck }"
            :style="{ height: searchBoxHeight, borderRadius: searchBoxRadius }"
            @tap="handleSearchClick">
            <view class="location-tag flex-center" v-if="!isStuck">
              <text>金茂府</text>
              <uni-icons type="bottom" size="12" color="#fff" style="margin-left: 4rpx;"></uni-icons>
            </view>
            <view class="left-icon flex-center" v-else>
              <uni-icons type="search" size="20" color="#666"></uni-icons>
            </view>

            <swiper class="search-swiper flex-1" vertical circular autoplay interval="3000">
              <swiper-item class="flex-center-start" v-for="(keyword, index) in hotKeywords" :key="index">
                <text class="placeholder" :style="{ color: isStuck ? '#666' : '#fff' }">{{ keyword }}</text>
              </swiper-item>
            </swiper>
            <view
              class="search-btn flex-center"
              :style="{ height: searchBtnHeight, borderRadius: searchBtnRadius }"
            >搜索</view>
          </view>
        </view>
      </view>

      <view class="express-repair-section" :style="{ padding: '16rpx ' + pageBase.capsuleInfo.pagePadding }">
        <view class="express-card flex-between-center" @tap="handleExpressRepair">
          <view class="card-left flex-column">
            <text class="title">极速报修</text>
            <text class="desc">最快30分钟上门 · 乱收费双倍赔</text>
          </view>
          <view class="card-right flex-center">
            <uni-icons type="mic-filled" size="18" color="#fff"></uni-icons>
            <text class="btn-text">一键呼叫</text>
          </view>
          <view class="glow-circle"></view>
        </view>
      </view>

      <view class="service-column-card" :style="{ margin: '0 ' + pageBase.capsuleInfo.pagePadding }">
        <view class="column-container">
          <view class="service-column" v-for="(cat, index) in categoryList" :key="index">
            <view class="item-node level-1 flex-column flex-center" @tap="handleServiceClick(cat.name)">
              <view class="icon-wrapper flex-center">
                <image :src="cat.icon" mode="aspectFit" class="icon-img"></image>
              </view>
              <text class="node-name main-text">{{ cat.name }}</text>
            </view>
            <view class="sub-list-box flex-column">
              <view class="item-node level-2 flex-column flex-center" v-for="(sub, sIdx) in cat.subs" :key="sIdx" @tap="handleServiceClick(sub.name)">
                <text class="node-name sub-text">{{ sub.name }}</text>
              </view>
            </view>
          </view>
        </view>
      </view>

      <view class="banner-section" :style="{ padding: '0 ' + pageBase.capsuleInfo.pagePadding + ' 24rpx' }">
        <swiper class="home-banner" circular autoplay interval="5000" indicator-dots indicator-active-color="#2B5CFF" indicator-color="rgba(255,255,255,0.8)">
          <swiper-item v-for="(item, index) in bannerList" :key="index">
            <image :src="item.url" mode="aspectFill" class="banner-image" @tap="handleBannerClick(item)"></image>
          </swiper-item>
        </swiper>
      </view>

      <view class="butler-entrance" :style="{ margin: '0 ' + pageBase.capsuleInfo.pagePadding + ' 24rpx' }" @tap="handleGoButler">
        <view class="butler-content flex-between-center">
          <view class="left-info flex-center-start">
            <view class="badge-icon flex-center">
              <uni-icons type="vip-filled" size="28" color="#ffe0a3"></uni-icons>
            </view>
            <view class="text-group flex-column">
              <view class="title-wrapper flex-center-start">
                <text class="title">全屋金管家</text>
                <text class="vip-tag">SVIP</text>
              </view>
              <text class="desc">一站式全屋维护 · 专属私享服务</text>
            </view>
          </view>
          <view class="right-btn flex-center">
            <text>立即体验</text>
            <uni-icons type="right" size="14" color="#2b2d38" style="margin-left: 4rpx;"></uni-icons>
          </view>
        </view>
        <view class="glare-effect"></view>
      </view>

      <view class="ecology-section" :style="{ margin: '0 ' + pageBase.capsuleInfo.pagePadding }">
        <view class="ecology-bento">
          <view class="bento-left clean-card flex-column" @tap="handleEcologyClick(ecologyList[0])">
            <view class="bento-text">
              <text class="bento-title" style="color: #2B5CFF;">{{ ecologyList[0].name }}</text>
              <text class="bento-desc">{{ ecologyList[0].desc }}</text>
            </view>
            <image :src="ecologyList[0].svg" mode="aspectFit" class="bento-svg-large"></image>
          </view>
          <view class="bento-right flex-column">
            <view class="bento-item-small clean-card flex-between-center" v-for="(item, index) in ecologyList.slice(1)" :key="index" @tap="handleEcologyClick(item)">
              <view class="bento-text">
                <text class="bento-title" :style="{ color: item.color }">{{ item.name }}</text>
                <text class="bento-desc">{{ item.desc }}</text>
              </view>
              <image :src="item.svg" mode="aspectFit" class="bento-svg-small"></image>
            </view>
          </view>
        </view>
      </view>

      <view class="hot-recommend-section" :style="{ padding: '40rpx ' + pageBase.capsuleInfo.pagePadding }">
        <view class="section-header flex-between-center">
          <view class="title-left flex-center-start">
            <text class="main-title">热门推荐</text>
            <view class="tag-line"></view>
            <text class="sub-title">大家都在修</text>
          </view>
        </view>

        <view class="waterfall-container flex-row flex-between-start">
          <view class="waterfall-column flex-column">
            <view class="waterfall-item clean-card" v-for="(item, index) in leftList" :key="'l' + index" @tap="handleProjectClick(item)">
              <image :src="item.image" mode="widthFix" class="item-cover" lazy-load></image>
              <view class="item-info">
                <text class="item-title">{{ item.title }}</text>
                <view class="item-tags"><text class="tag">{{ item.tag }}</text></view>
                <view class="item-bottom flex-between-end">
                  <view class="price-box"><text class="symbol">¥</text><text class="price">{{ item.price }}</text></view>
                  <text class="sales">已售 {{ item.sales }}</text>
                </view>
              </view>
            </view>
          </view>
          <view class="waterfall-column flex-column">
            <view class="waterfall-item clean-card" v-for="(item, index) in rightList" :key="'r' + index" @tap="handleProjectClick(item)">
              <image :src="item.image" mode="widthFix" class="item-cover" lazy-load></image>
              <view class="item-info">
                <text class="item-title">{{ item.title }}</text>
                <view class="item-tags"><text class="tag">{{ item.tag }}</text></view>
                <view class="item-bottom flex-between-end">
                  <view class="price-box"><text class="symbol">¥</text><text class="price">{{ item.price }}</text></view>
                  <text class="sales">已售 {{ item.sales }}</text>
                </view>
              </view>
            </view>
          </view>
        </view>
      </view>

      <view class="load-more-status flex-center"><text class="status-text">{{ loadStatusText }}</text></view>
      <view class="safe-bottom-area"></view>
    </scroll-view>
  </view>
</template>

<script setup>
import { ref, computed } from 'vue';
import { onLoad } from '@dcloudio/uni-app';

// 兼容你的 pageBaseInfo (使用默认值防止报错)
const pageBase = ref({
  navInfo: { contentTop: 44, barBottom: 88, contentHeight: 32, hasCapsule: true, stickySearchRight: 100 },
  capsuleInfo: { pagePadding: '24rpx' }
});

const projectInfo = ref({ name: '呼之应', slogan: '一呼百应 · 专业到家' });
const navInfo = pageBase.value.navInfo;
const capsuleInfo = pageBase.value.capsuleInfo;

const searchStartGap = 12;
const titleFadeDistance = 18;
const bgFadeDistance = 24;

const titleLayerTop = `${navInfo.contentTop}px`;
const titleLayerHeight = `${navInfo.barBottom - navInfo.contentTop}px`;
const headerPlaceholderHeight = computed(() => `${navInfo.barBottom + searchStartGap}px`);
const searchStickyTop = computed(() => `${navInfo.contentTop}px`);
const searchBoxHeight = computed(() => `${navInfo.contentHeight}px`);
const searchBoxRadius = computed(() => `${navInfo.contentHeight / 2}px`);
const searchBtnBaseHeight = Math.max(navInfo.contentHeight - 8, 24);
const searchBtnHeight = computed(() => `${searchBtnBaseHeight}px`);
const searchBtnRadius = computed(() => `${searchBtnBaseHeight / 2}px`);
const searchPaddingLeft = computed(() => capsuleInfo.pagePadding);

const searchStickyThreshold = navInfo.barBottom + searchStartGap - navInfo.contentTop;
const scrollTop = ref(0);

const titleOpacity = computed(() => Math.max(0, 1 - scrollTop.value / titleFadeDistance));
const bgOpacity = computed(() => Math.min(1, scrollTop.value / bgFadeDistance));
const isStuck = computed(() => scrollTop.value >= searchStickyThreshold);

const searchPaddingRight = computed(() => isStuck.value && navInfo.hasCapsule ? `${navInfo.stickySearchRight}px` : capsuleInfo.pagePadding);

const handleScroll = (e) => { scrollTop.value = e.detail.scrollTop; };

const hotKeywords = ref(['搜 "空调清洗" 享8折', '指纹锁安装 极速上门', '日常保洁 3小时特惠']);

// ---------------- 核心数据源 (全部替换为高质量SVG和相关业务) ----------------

// 金刚区：使用 Iconify 的 Solar 双色系列SVG，配色采用主题蓝 #2B5CFF 和 浅蓝辅助色
const categoryList = ref([
  {
    name: '家电清洗',
    icon: 'https://api.iconify.design/solar:washing-machine-bold-duotone.svg?color=%232B5CFF&secondary=%238AB4F8',
    subs: [{ name: '空调洗' }, { name: '洗衣机' }]
  },
  {
    name: '专业维修',
    icon: 'https://api.iconify.design/solar:screwdrive-bold-duotone.svg?color=%232B5CFF&secondary=%238AB4F8',
    subs: [{ name: '修家电' }, { name: '修水电' }]
  },
  {
    name: '上门安装',
    icon: 'https://api.iconify.design/solar:box-bold-duotone.svg?color=%232B5CFF&secondary=%238AB4F8',
    subs: [{ name: '装锁具' }, { name: '装灯具' }]
  },
  {
    name: '保洁收纳',
    icon: 'https://api.iconify.design/solar:broom-bold-duotone.svg?color=%232B5CFF&secondary=%238AB4F8',
    subs: [{ name: '日常保洁' }, { name: '深度开荒' }]
  },
  {
    name: '管道疏通',
    icon: 'https://api.iconify.design/solar:waterdrop-bold-duotone.svg?color=%232B5CFF&secondary=%238AB4F8',
    subs: [{ name: '通马桶' }, { name: '查漏水' }]
  }
]);

// 运营 Banner
const bannerList = ref([
  { url: 'https://picsum.photos/800/300?random=11' },
  { url: 'https://picsum.photos/800/300?random=12' },
]);

// 生态便当盒：使用高质量彩色 3D/扁平 SVG 增强视觉
const ecologyList = ref([
  {
    name: '小应学堂', desc: '金牌技师进阶课', color: '#2B5CFF',
    svg: 'https://api.iconify.design/fluent-emoji-flat:graduation-cap.svg',
    url: '/pages/school/index'
  },
  {
    name: '精选商城', desc: '五金耗材一站购', color: '#FF7D00',
    svg: 'https://api.iconify.design/fluent-emoji-flat:shopping-cart.svg',
    url: '/pages/mall/index'
  },
  {
    name: '同城圈子', desc: '本地生活互助', color: '#00B578',
    svg: 'https://api.iconify.design/fluent-emoji-flat:handshake.svg',
    url: '/pages/life/index'
  }
]);

// 瀑布流逻辑
const leftList = ref([]);
const rightList = ref([]);
const pageNum = ref(1);
const isRefreshing = ref(false);
const loadStatus = ref('more');

const loadStatusText = computed(() => {
  if (loadStatus.value === 'loading') return '加载中...';
  if (loadStatus.value === 'noMore') return '— 到底啦 —';
  return '上拉加载更多';
});

const fetchWaterfallData = (isReset = false) => {
  if (loadStatus.value === 'noMore' && !isReset) return;
  loadStatus.value = 'loading';

  setTimeout(() => {
    const offset = pageNum.value * 10;
    const mockData = [
      { title: `空调深度清洗杀菌`, image: `https://picsum.photos/400/500?random=${offset + 1}`, tag: '不净全退', price: '89', sales: '2.1k' },
      { title: `指纹密码锁极速安装`, image: `https://picsum.photos/400/350?random=${offset + 2}`, tag: '专业认证', price: '120', sales: '856' },
      { title: `日常居家保洁3小时`, image: `https://picsum.photos/400/450?random=${offset + 3}`, tag: '自带工具', price: '135', sales: '3.4k' },
      { title: `家庭暗管漏水精准检测`, image: `https://picsum.photos/400/380?random=${offset + 4}`, tag: '不修不收', price: '150', sales: '1.2k' },
    ];
    if (isReset) { leftList.value = []; rightList.value = []; isRefreshing.value = false; }
    mockData.forEach((item) => leftList.value.length <= rightList.value.length ? leftList.value.push(item) : rightList.value.push(item));
    pageNum.value >= 3 ? (loadStatus.value = 'noMore') : (loadStatus.value = 'more');
  }, 800);
};

const handleReachBottom = () => { if (loadStatus.value !== 'loading' && loadStatus.value !== 'noMore') { pageNum.value++; fetchWaterfallData(false); } };
const handlePullDownRefresh = () => { if (!isRefreshing.value) { isRefreshing.value = true; pageNum.value = 1; fetchWaterfallData(true); } };

// 交互事件
const handleSearchClick = () => console.log('触发搜索');
const handleExpressRepair = () => { uni.vibrateShort(); console.log('跳转极速报修'); };
const handleBannerClick = (item) => console.log('点击 Banner');
const handleServiceClick = (name) => console.log('点击服务:', name);
const handleGoButler = () => { uni.vibrateShort(); console.log('跳转管家'); };
const handleEcologyClick = (item) => console.log('点击便当盒:', item.name);
const handleProjectClick = (item) => console.log('点击瀑布流:', item.title);

onLoad(() => fetchWaterfallData(true));
</script>

<style lang="scss" scoped>
/* 基础工具类 */
.flex-column { display: flex; flex-direction: column; }
.flex-row { display: flex; flex-direction: row; }
.flex-center { display: flex; align-items: center; justify-content: center; }
.flex-center-start { display: flex; align-items: center; justify-content: flex-start; }
.flex-between-center { display: flex; justify-content: space-between; align-items: center; }
.flex-between-start { display: flex; justify-content: space-between; align-items: flex-start; }
.flex-between-end { display: flex; justify-content: space-between; align-items: flex-end; }
.flex-1 { flex: 1; }

/* 品牌主题色 */
$brand-blue: #2B5CFF;
$bg-color: #F4F6F9;

.clean-card {
  background-color: #ffffff; border-radius: 20rpx;
  box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.03); overflow: hidden;
  transition: transform 0.15s ease, box-shadow 0.15s ease;
  &:active { transform: scale(0.98); box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.02); }
}

.page-container {
  height: 100vh; height: 100dvh; display: flex; flex-direction: column;
  background-color: $bg-color; overflow: hidden; position: relative;
  font-family: -apple-system, "PingFang SC", "Helvetica Neue", Arial, sans-serif;
}

/* 顶部科技蓝渐变背景 */
.global-gradient-bg {
  position: absolute; top: 0; left: 0; right: 0; height: 600rpx;
  background: linear-gradient(180deg, #1A4BFC 0%, rgba(43,92,255,0.7) 35%, $bg-color 100%);
  z-index: 1;
}

.fixed-title-layer {
  position: absolute; left: 0; right: 0; z-index: 120;
  display: flex; align-items: center; box-sizing: border-box; pointer-events: none;
  .header-name { font-size: 38rpx; color: #fff; font-weight: bold; margin-right: 16rpx; letter-spacing: 2rpx; text-shadow: 0 2rpx 8rpx rgba(0,0,0,0.1); }
  .header-slogan { font-size: 22rpx; color: #fff; font-weight: 500; background: rgba(255, 255, 255, 0.2); backdrop-filter: blur(4px); padding: 6rpx 16rpx; border-radius: 20rpx; }
}

.page-main { flex: 1; height: 0; position: relative; z-index: 100; }

.sticky-search-wrapper {
  position: -webkit-sticky; position: sticky; z-index: 110;
  .sticky-white-bg { position: absolute; bottom: 0; left: 0; right: 0; height: 600rpx; background-color: rgba(255, 255, 255, 0.98); box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.05); backdrop-filter: blur(10px); z-index: -1; pointer-events: none; transition: opacity 0.2s linear; }
  .search-layout-container { box-sizing: border-box; width: 100%; transition: padding 0.2s ease; }
  .search-box {
    width: 100%; position: relative; z-index: 2;
    background-color: rgba(255, 255, 255, 0.2); border: 1px solid rgba(255, 255, 255, 0.4);
    padding: 0 8rpx 0 16rpx; box-sizing: border-box; backdrop-filter: blur(12px); transition: all 0.3s ease;
    
    .location-tag { background: rgba(255,255,255,0.2); padding: 6rpx 16rpx; border-radius: 30rpx; text { color: #fff; font-size: 24rpx; font-weight: bold; } flex-shrink: 0; }
    .left-icon { width: 40rpx; flex-shrink: 0; }
    
    .search-swiper { height: 100%; margin-left: 12rpx; overflow: hidden; .placeholder { font-size: 26rpx; font-weight: 500; transition: color 0.2s ease; } }
    .search-btn {
      background: #ffffff; color: $brand-blue; font-size: 26rpx; font-weight: bold;
      padding: 0 32rpx; flex-shrink: 0; box-sizing: border-box; box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.06); transition: transform 0.1s ease;
      &:active { transform: scale(0.92); }
    }
    &.is-stuck {
      background-color: #F4F5F8; border-color: transparent;
      .search-btn { background: $brand-blue; color: #fff; box-shadow: 0 4rpx 12rpx rgba(43,92,255,0.3); }
    }
  }
}

/* 极速报修卡片 */
.express-repair-section {
  .express-card {
    background: linear-gradient(135deg, #1A4BFC 0%, #0C2B99 100%);
    border-radius: 20rpx; padding: 28rpx 32rpx; position: relative; overflow: hidden;
    box-shadow: 0 8rpx 24rpx rgba(26,75,252,0.2);
    &:active { transform: scale(0.98); }
    
    .card-left { z-index: 2; .title { font-size: 34rpx; font-weight: bold; color: #fff; margin-bottom: 6rpx; } .desc { font-size: 24rpx; color: rgba(255,255,255,0.8); } }
    .card-right {
      z-index: 2; background: rgba(255,255,255,0.15); backdrop-filter: blur(8px);
      padding: 14rpx 24rpx; border-radius: 40rpx; border: 1px solid rgba(255,255,255,0.2);
      .btn-text { font-size: 26rpx; font-weight: bold; color: #fff; margin-left: 8rpx; }
    }
    .glow-circle { position: absolute; right: -20%; top: -50%; width: 200rpx; height: 200rpx; background: radial-gradient(circle, rgba(255,255,255,0.15) 0%, transparent 70%); z-index: 1; }
  }
}

/* 金刚区 SVG 重构 */
.service-column-card {
  background-color: #ffffff; border-radius: 24rpx; padding: 32rpx 16rpx; box-sizing: border-box; box-shadow: 0 6rpx 24rpx rgba(0, 0, 0, 0.02); margin-bottom: 24rpx;
  .column-container {
    display: grid; grid-template-columns: repeat(5, 1fr); column-gap: 8rpx; width: 100%;
    .service-column {
      display: flex; flex-direction: column; align-items: center;
      .item-node { width: 100%; display: flex; flex-direction: column; align-items: center; transition: transform 0.15s, opacity 0.15s; &:active { transform: scale(0.92); opacity: 0.8; } .node-name { text-align: center; white-space: nowrap; } }
      .level-1 {
        margin-bottom: 20rpx;
        .icon-wrapper { width: 88rpx; height: 88rpx; background: #F8FAFF; border-radius: 24rpx; padding: 14rpx; margin-bottom: 12rpx; border: 1px solid #EEF2FF; }
        .icon-img { width: 100%; height: 100%; }
        .main-text { font-size: 24rpx; font-weight: bold; color: #222; }
      }
      .sub-list-box {
        gap: 16rpx;
        .level-2 { background: #F4F6F9; padding: 8rpx 0; border-radius: 12rpx; width: 85%; .sub-text { color: #666; font-size: 20rpx; font-weight: 500; } }
      }
    }
  }
}

.banner-section { .home-banner { height: 200rpx; border-radius: 20rpx; overflow: hidden; box-shadow: 0 6rpx 16rpx rgba(0, 0, 0, 0.04); } .banner-image { width: 100%; height: 100%; background-color: #eaebec; } }

/* SVIP 金管家 */
.butler-entrance {
  position: relative; height: 160rpx; background: linear-gradient(135deg, #1C1E26 0%, #2A2D3A 100%); border-radius: 24rpx; overflow: hidden; box-shadow: 0 10rpx 24rpx rgba(0, 0, 0, 0.1); border: 1px solid rgba(255, 224, 163, 0.15); transition: transform 0.2s; &:active { transform: scale(0.96); }
  .butler-content { width: 100%; height: 100%; padding: 0 32rpx; z-index: 2; position: absolute; }
  .left-info {
    .badge-icon { width: 88rpx; height: 88rpx; background: linear-gradient(135deg, #FFE0A3, #D4A955); border-radius: 28rpx; margin-right: 28rpx; box-shadow: 0 4rpx 12rpx rgba(212, 169, 85, 0.3); }
    .title-wrapper { margin-bottom: 8rpx; .title { font-size: 34rpx; color: #FFE0A3; font-weight: bold; letter-spacing: 2rpx; } .vip-tag { font-size: 20rpx; background: linear-gradient(90deg, #FFE0A3, #EED093); color: #1C1E26; font-weight: 900; padding: 4rpx 10rpx; border-radius: 8rpx; margin-left: 14rpx; } }
    .desc { font-size: 22rpx; color: rgba(255, 224, 163, 0.7); }
  }
  .right-btn { background: linear-gradient(90deg, #FFE0A3, #D4A955); padding: 14rpx 30rpx; border-radius: 40rpx; font-size: 24rpx; font-weight: bold; color: #1C1E26; }
  .glare-effect { position: absolute; top: 0; left: -100%; width: 50%; height: 100%; background: linear-gradient(90deg, transparent, rgba(255, 224, 163, 0.2), transparent); transform: skewX(-25deg); animation: flare 5s infinite linear; }
}

/* 生态便当盒 */
.ecology-section {
  .ecology-bento {
    display: flex; gap: 16rpx; height: 300rpx;
    .bento-text { z-index: 2; position: relative; .bento-title { font-size: 30rpx; font-weight: bold; display: block; margin-bottom: 6rpx; } .bento-desc { font-size: 22rpx; color: #8A8F99; display: block; } }
    .bento-left { flex: 1; padding: 24rpx; position: relative; justify-content: space-between; .bento-svg-large { width: 140rpx; height: 140rpx; align-self: flex-end; opacity: 0.9; } }
    .bento-right { flex: 1; gap: 16rpx; .bento-item-small { flex: 1; padding: 20rpx 24rpx; position: relative; .bento-svg-small { width: 80rpx; height: 80rpx; opacity: 0.9; } } }
  }
}

/* 瀑布流推荐 */
.hot-recommend-section {
  .section-header { margin-bottom: 30rpx; .title-left { .main-title { font-size: 36rpx; font-weight: 900; color: #111; } .tag-line { width: 6rpx; height: 26rpx; background-color: $brand-blue; border-radius: 4rpx; margin: 0 16rpx; } .sub-title { font-size: 24rpx; color: #888; font-weight: 500; } } }
  .waterfall-container {
    .waterfall-column { width: 48.5%; gap: 24rpx; }
    .waterfall-item {
      padding-bottom: 20rpx;
      .item-cover { width: 100%; display: block; background-color: #eaebec; min-height: 240rpx; }
      .item-info {
        padding: 20rpx 16rpx 0;
        .item-title { font-size: 28rpx; font-weight: bold; line-height: 1.4; color: #222; margin-bottom: 16rpx; display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden; }
        .item-tags { margin-bottom: 20rpx; display: flex; flex-wrap: wrap; gap: 10rpx; .tag { font-size: 20rpx; color: $brand-blue; background: rgba(43,92,255,0.08); padding: 4rpx 12rpx; border-radius: 8rpx; font-weight: 600; } }
        .item-bottom { .price-box { color: #FF4A4A; font-weight: bold; .symbol { font-size: 24rpx; margin-right: 4rpx; } .price { font-size: 38rpx; font-family: din; } } .sales { font-size: 22rpx; color: #aaa; } }
      }
    }
  }
}

.load-more-status { padding: 40rpx 0; .status-text { font-size: 24rpx; color: #999; } }
.safe-bottom-area { width: 100%; height: calc(140rpx + env(safe-area-inset-bottom)); }

@keyframes flare { 0% { left: -100%; } 20% { left: 150%; } 100% { left: 150%; } }
</style>