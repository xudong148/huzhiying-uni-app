<template>
  <view class="page-container flex-column">
    <view class="nav-bar">
      <view class="status-bar"></view>
      <view class="nav-header flex-center">
        <text class="nav-title">全部服务</text>
      </view>
    </view>

    <view class="search-section flex-center">
      <view class="search-box flex-center-start" @tap="handleSearch">
        <text class="search-icon">🔍</text>
        <text class="search-placeholder">搜一搜你想找的服务（如：修空调）</text>
      </view>
    </view>

    <view class="content-wrapper flex-row flex-1">
      
      <scroll-view 
        class="left-menu" 
        scroll-y 
        :scroll-top="leftScrollTop"
        scroll-with-animation
        :show-scrollbar="false"
      >
        <view 
          class="menu-item flex-center"
          v-for="(item, index) in categoryList" 
          :key="index"
          :class="{ 'menu-item-active': currentCategoryIndex === index }"
          @tap="switchCategory(index)"
        >
          <view class="active-line" v-if="currentCategoryIndex === index"></view>
          <text class="menu-text">{{ item.name }}</text>
        </view>
      </scroll-view>

      <scroll-view 
        class="right-content" 
        scroll-y 
        :scroll-into-view="scrollIntoId"
        scroll-with-animation
        :show-scrollbar="false"
        @scroll="handleRightScroll"
      >
        <view class="scroll-wrapper">
          <view 
            v-for="(category, catIdx) in categoryList" 
            :key="catIdx"
            :id="'section-' + catIdx"
            class="category-section"
          >
            <view class="right-banner" v-if="category.bannerUrl">
              <image 
                class="banner-img" 
                :src="category.bannerUrl" 
                mode="aspectFill"
              ></image>
            </view>

            <view class="service-group" v-for="(group, gIdx) in category.groups" :key="gIdx">
              <text class="group-title">{{ group.title }}</text>
              
              <view class="service-grid">
                <view 
                  class="service-item flex-column flex-center"
                  v-for="(service, sIdx) in group.list" 
                  :key="sIdx"
                  @tap="goToServiceDetail(service)"
                >
                  <view class="icon-wrapper flex-center">
                    <image :src="service.icon" mode="aspectFit" class="service-icon" lazy-load></image>
                  </view>
                  <text class="service-name">{{ service.name }}</text>
                </view>
              </view>
            </view>
          </view>
          
          <view class="safe-bottom-space"></view>
        </view>
      </scroll-view>
      
    </view>
  </view>
</template>

<script setup>
import { ref, onMounted, nextTick, getCurrentInstance } from 'vue';

const { proxy } = getCurrentInstance();

// ================= 状态数据 =================
const currentCategoryIndex = ref(0);
const scrollIntoId = ref('');
const leftScrollTop = ref(0);

// 状态锁与高度缓存
let isClickSwitching = false; 
let sectionHeights = []; 

// ================= 你提供的超长模拟数据大全 (保留全部) =================
const categoryList = ref([
  {
    name: '家电维修',
    bannerUrl: 'https://picsum.photos/600/200?random=101',
    groups: [
      {
        title: '制冷家电',
        list: [
          { name: '空调加氟', icon: 'https://picsum.photos/100/100?random=111' },
          { name: '空调不制冷', icon: 'https://picsum.photos/100/100?random=112' },
          { name: '空调漏水', icon: 'https://picsum.photos/100/100?random=113' },
          { name: '空调移机', icon: 'https://picsum.photos/100/100?random=114' },
          { name: '冰箱不制冷', icon: 'https://picsum.photos/100/100?random=115' },
          { name: '冰箱漏水', icon: 'https://picsum.photos/100/100?random=116' },
          { name: '冷柜维修', icon: 'https://picsum.photos/100/100?random=117' },
          { name: '制冰机维修', icon: 'https://picsum.photos/100/100?random=118' },
        ]
      },
      {
        title: '厨卫家电',
        list: [
          { name: '洗衣机维修', icon: 'https://picsum.photos/100/100?random=121' },
          { name: '热水器不热', icon: 'https://picsum.photos/100/100?random=122' },
          { name: '燃气灶打不着', icon: 'https://picsum.photos/100/100?random=123' },
          { name: '油烟机不吸', icon: 'https://picsum.photos/100/100?random=124' },
          { name: '洗碗机维修', icon: 'https://picsum.photos/100/100?random=125' },
          { name: '微波炉维修', icon: 'https://picsum.photos/100/100?random=126' },
          { name: '净水器滤芯', icon: 'https://picsum.photos/100/100?random=127' },
          { name: '集成灶维修', icon: 'https://picsum.photos/100/100?random=128' },
        ]
      },
      {
        title: '影音小家电',
        list: [
          { name: '电视黑屏', icon: 'https://picsum.photos/100/100?random=131' },
          { name: '投影仪维修', icon: 'https://picsum.photos/100/100?random=132' },
          { name: '音响维修', icon: 'https://picsum.photos/100/100?random=133' },
          { name: '扫地机维修', icon: 'https://picsum.photos/100/100?random=134' },
          { name: '戴森维修', icon: 'https://picsum.photos/100/100?random=135' },
          { name: '破壁机维修', icon: 'https://picsum.photos/100/100?random=136' },
        ]
      }
    ]
  },
  {
    name: '上门安装',
    bannerUrl: 'https://picsum.photos/600/200?random=102',
    groups: [
      {
        title: '灯具卫浴',
        list: [
          { name: '吸顶灯安装', icon: 'https://picsum.photos/100/100?random=211' },
          { name: '水晶灯组装', icon: 'https://picsum.photos/100/100?random=212' },
          { name: '无主灯布线', icon: 'https://picsum.photos/100/100?random=213' },
          { name: '普通马桶安装', icon: 'https://picsum.photos/100/100?random=214' },
          { name: '智能马桶盖', icon: 'https://picsum.photos/100/100?random=215' },
          { name: '花洒安装', icon: 'https://picsum.photos/100/100?random=216' },
          { name: '浴霸安装', icon: 'https://picsum.photos/100/100?random=217' },
          { name: '浴室柜安装', icon: 'https://picsum.photos/100/100?random=218' },
        ]
      },
      {
        title: '家具组装',
        list: [
          { name: '电视挂装', icon: 'https://picsum.photos/100/100?random=221' },
          { name: '板式床组装', icon: 'https://picsum.photos/100/100?random=222' },
          { name: '高低床组装', icon: 'https://picsum.photos/100/100?random=223' },
          { name: '衣柜组装', icon: 'https://picsum.photos/100/100?random=224' },
          { name: '餐桌椅组装', icon: 'https://picsum.photos/100/100?random=225' },
          { name: '办公桌椅', icon: 'https://picsum.photos/100/100?random=226' },
          { name: '晾衣架安装', icon: 'https://picsum.photos/100/100?random=227' },
          { name: '窗帘杆安装', icon: 'https://picsum.photos/100/100?random=228' },
        ]
      },
      {
        title: '门窗锁具',
        list: [
          { name: '智能锁安装', icon: 'https://picsum.photos/100/100?random=231' },
          { name: '木门安装', icon: 'https://picsum.photos/100/100?random=232' },
          { name: '防盗窗安装', icon: 'https://picsum.photos/100/100?random=233' },
          { name: '纱窗定制', icon: 'https://picsum.photos/100/100?random=234' },
        ]
      }
    ]
  },
  {
    name: '清洗保养',
    bannerUrl: 'https://picsum.photos/600/200?random=103',
    groups: [
      {
        title: '家电清洗',
        list: [
          { name: '空调深度洗', icon: 'https://picsum.photos/100/100?random=311' },
          { name: '中央空调清洗', icon: 'https://picsum.photos/100/100?random=312' },
          { name: '油烟机拆洗', icon: 'https://picsum.photos/100/100?random=313' },
          { name: '集成灶拆洗', icon: 'https://picsum.photos/100/100?random=314' },
          { name: '洗衣机拆洗', icon: 'https://picsum.photos/100/100?random=315' },
          { name: '冰箱除味杀菌', icon: 'https://picsum.photos/100/100?random=316' },
          { name: '热水器除垢', icon: 'https://picsum.photos/100/100?random=317' },
          { name: '饮水机清洗', icon: 'https://picsum.photos/100/100?random=318' },
        ]
      },
      {
        title: '软装清洗',
        list: [
          { name: '沙发清洗', icon: 'https://picsum.photos/100/100?random=321' },
          { name: '床垫除螨', icon: 'https://picsum.photos/100/100?random=322' },
          { name: '地毯清洗', icon: 'https://picsum.photos/100/100?random=323' },
          { name: '窗帘清洗', icon: 'https://picsum.photos/100/100?random=324' },
          { name: '水晶灯清洗', icon: 'https://picsum.photos/100/100?random=325' },
        ]
      }
    ]
  },
  {
    name: '便民急修',
    bannerUrl: 'https://picsum.photos/600/200?random=106',
    groups: [
      {
        title: '急难险重',
        list: [
          { name: '马桶极速疏通', icon: 'https://picsum.photos/100/100?random=611' },
          { name: '地漏下水疏通', icon: 'https://picsum.photos/100/100?random=612' },
          { name: '防盗门开锁', icon: 'https://picsum.photos/100/100?random=615' },
        ]
      }
    ]
  }
]);

// ================= 生命周期与节点计算 =================
onMounted(() => {
  nextTick(() => {
    // 延迟一点计算，确保图片或DOM渲染完成
    setTimeout(() => {
      calculateHeights();
    }, 500);
  });
});

// 计算右侧各个分类模块的高度区间
const calculateHeights = () => {
  const query = uni.createSelectorQuery().in(proxy);
  query.selectAll('.category-section').boundingClientRect((rects) => {
    if (rects && rects.length) {
      let height = 0;
      sectionHeights = [0];
      rects.forEach((rect) => {
        height += rect.height;
        sectionHeights.push(height);
      });
    }
  }).exec();
};

// ================= 交互逻辑 =================

// 点击左侧分类
const switchCategory = (index) => {
  if (currentCategoryIndex.value === index) return;
  
  isClickSwitching = true;
  currentCategoryIndex.value = index;
  
  // 利用 scroll-into-view 精准定位到右侧对应的 id
  scrollIntoId.value = 'section-' + index;
  
  // 调整左侧滚动位置，让选中的菜单尽量居中 (这里假设 item 高度约 55px)
  leftScrollTop.value = index * 55 - 150;
  
  // 动画结束后解除状态锁
  setTimeout(() => {
    isClickSwitching = false;
  }, 350);
};

// 监听右侧自然滚动，反向控制左侧高亮
const handleRightScroll = (e) => {
  if (isClickSwitching || sectionHeights.length === 0) return;
  
  const scrollTop = e.detail.scrollTop;
  // 遍历高度区间，判断当前位置
  for (let i = 0; i < sectionHeights.length - 1; i++) {
    // 偏移量 20 用于平滑过渡判断
    if (scrollTop >= sectionHeights[i] - 20 && scrollTop < sectionHeights[i + 1] - 20) {
      if (currentCategoryIndex.value !== i) {
        currentCategoryIndex.value = i;
        leftScrollTop.value = i * 55 - 150;
      }
      break;
    }
  }
};

const handleSearch = () => {
  console.log('跳转搜索页');
};

const goToServiceDetail = (service) => {
  console.log('选择具体服务：', service.name);
};

</script>

<style lang="scss" scoped>
/* 基础 Flex 工具 */
.flex-column { display: flex; flex-direction: column; }
.flex-row { display: flex; flex-direction: row; }
.flex-center { display: flex; align-items: center; justify-content: center; }
.flex-center-start { display: flex; align-items: center; justify-content: flex-start; }
.flex-1 { flex: 1; }

/* 页面框架变量 */
$bg-color: #F6F7F9;        /* 背景灰 */
$sidebar-bg: #F8F8F8;      /* 侧边栏浅灰 */
$text-main: #1C1E21;       /* 主文本黑 */
$text-sub: #8B8F9A;        /* 副文本灰 */
// 融合你喜欢的品质感配色，这里采用黑金/暗金基调的指示色
$brand-color: #C5A162;     

.page-container {
  height: 100vh;
  height: 100dvh;
  background-color: $bg-color;
  overflow: hidden;
}

/* 导航栏 */
.nav-bar {
  background-color: #FFFFFF;
  .status-bar { height: var(--status-bar-height); }
  .nav-header {
    height: 88rpx;
    .nav-title {
      font-size: 34rpx;
      font-weight: 600;
      color: $text-main;
    }
  }
}

/* 搜索栏：悬浮质感 */
.search-section {
  padding: 16rpx 24rpx 24rpx;
  background-color: #FFFFFF;
  z-index: 10;
  
  .search-box {
    width: 100%;
    height: 72rpx;
    background-color: #F4F5F8;
    border-radius: 36rpx;
    padding: 0 32rpx;
    
    .search-icon { font-size: 28rpx; margin-right: 8rpx; color: $text-sub;}
    .search-placeholder {
      font-size: 26rpx;
      color: #9CA3AF;
    }
  }
}

/* 核心内容区 */
.content-wrapper {
  height: 0; 
  border-top: 1rpx solid #F0F1F4;
}

/* ================= 左侧菜单 ================= */
.left-menu {
  width: 180rpx; /* 稍微调窄一点，给右侧留更多空间 */
  height: 100%;
  background-color: $sidebar-bg;
  
  .menu-item {
    width: 100%;
    height: 110rpx;
    position: relative;
    transition: background-color 0.2s;
    
    .menu-text {
      font-size: 26rpx;
      color: $text-sub;
      font-weight: 500;
      transition: all 0.2s;
    }
    
    .active-line {
      position: absolute;
      left: 0;
      top: 50%;
      transform: translateY(-50%);
      width: 8rpx;
      height: 36rpx;
      background-color: $brand-color; /* 替换为有质感的颜色 */
      border-radius: 0 6rpx 6rpx 0;
    }
  }
  
  .menu-item-active {
    background-color: #FFFFFF;
    
    .menu-text {
      color: $text-main;
      font-weight: bold;
      font-size: 28rpx;
    }
  }
}

/* ================= 右侧内容 ================= */
.right-content {
  flex: 1;
  height: 100%;
  background-color: #FFFFFF;
  box-sizing: border-box;

  .scroll-wrapper {
    padding: 24rpx;
  }

  /* 每个大分类作为一个 Section */
  .category-section {
    margin-bottom: 40rpx;
    /* 解决 scroll-into-view 时贴顶过紧的问题 */
    padding-top: 10rpx; 
  }

  .right-banner {
    width: 100%;
    height: 180rpx;
    border-radius: 16rpx;
    overflow: hidden;
    margin-bottom: 30rpx;
    box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.03); /* 轻微投影增加品质感 */
    
    .banner-img {
      width: 100%;
      height: 100%;
      background-color: #EAEBEC;
    }
  }

  .service-group {
    margin-bottom: 32rpx;

    .group-title {
      font-size: 28rpx;
      font-weight: bold;
      color: $text-main;
      margin-bottom: 24rpx;
      display: block;
      /* 给标题加个极简的点缀 */
      padding-left: 12rpx;
      position: relative;
      &::before {
        content: '';
        position: absolute;
        left: 0;
        top: 50%;
        transform: translateY(-50%);
        width: 4rpx;
        height: 24rpx;
        background-color: $brand-color;
        border-radius: 4rpx;
      }
    }

    .service-grid {
      display: grid;
      grid-template-columns: repeat(3, 1fr);
      row-gap: 32rpx;
      column-gap: 16rpx;

      .service-item {
        &:active {
          opacity: 0.7;
          transform: scale(0.95);
          transition: all 0.1s;
        }

        .icon-wrapper {
          width: 96rpx;
          height: 96rpx;
          background-color: #F8F9FB; /* 稍微提亮一点背景 */
          border-radius: 24rpx; /* 圆角改大，更显现代 */
          margin-bottom: 14rpx;
          
          .service-icon {
            width: 56rpx;
            height: 56rpx;
          }
        }

        .service-name {
          font-size: 24rpx;
          color: #4A4C52;
          text-align: center;
          white-space: nowrap;
        }
      }
    }
  }
  
  .safe-bottom-space {
    height: 60vh; /* 确保最后几个分类也能被滚动锚定到最上面 */
    width: 100%;
  }
}
</style>