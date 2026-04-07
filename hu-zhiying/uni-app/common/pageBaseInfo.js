/*
 * @Author: 许栋
 * @Date: 2026-03-23 14:03:24
 * @LastEditors: Do not edit
 * @LastEditTime: 2026-03-23 16:43:40
 * @Description: 页面基础信息
 * @FilePath: \呼之应\common\pageBaseInfo.js
 */

/* 获取页面的参数相关的js  如在小程序下胶囊的信息 顶部及顶部的安全距离 */

function getWindowBaseInfo() {
  const systemInfo = uni.getSystemInfoSync();
  const windowInfo =
    typeof uni.getWindowInfo === 'function' ? uni.getWindowInfo() : systemInfo;

  return {
    systemInfo,
    windowInfo,
  };
}

function createNavInfo(windowInfo, capsuleInfo) {
  const statusBarHeight = windowInfo.statusBarHeight || 0;
  const defaultContentHeight = 32;
  const contentHeight = capsuleInfo.height || defaultContentHeight;
  const sidePadding = capsuleInfo.pagePaddingValue || 0;
  const topGap = capsuleInfo.top
    ? Math.max(capsuleInfo.top - statusBarHeight, 0)
    : 6;
  const barHeight = capsuleInfo.height ? contentHeight + topGap * 2 : 44;
  const contentTop =
    capsuleInfo.top ||
    statusBarHeight + Math.max((barHeight - contentHeight) / 2, 0);

  return {
    statusBarHeight,
    topGap,
    contentTop,
    contentHeight,
    barHeight,
    barBottom: statusBarHeight + barHeight,
    windowWidth: windowInfo.windowWidth || 0,
    windowHeight: windowInfo.windowHeight || 0,
    sidePadding,
    hasCapsule: !!capsuleInfo.width,
    capsuleLeft: capsuleInfo.left || 0,
    capsuleRight: capsuleInfo.right || 0,
    capsuleWidth: capsuleInfo.width || 0,
    stickySearchRight:
      capsuleInfo.left && windowInfo.windowWidth
        ? Math.max(windowInfo.windowWidth - capsuleInfo.left + sidePadding, 0)
        : sidePadding,
  };
}

function getPageBaseInfo() {
  const { systemInfo, windowInfo } = getWindowBaseInfo();
  let capsuleInfo = {
    pagePadding: '20rpx',
    pagePaddingValue: 0,
    capsuleAddHeight: 0,
    width: 0,
    height: 0,
    top: 0,
    left: 0,
    right: 0,
    bottom: 0,
  };

  /*#ifdef MP-WEIXIN*/
  const menuButtonInfo = uni.getMenuButtonBoundingClientRect();
  const pagePaddingValue = Math.max(
    windowInfo.windowWidth - menuButtonInfo.right,
    0,
  );
  capsuleInfo = {
    pagePadding: pagePaddingValue + 'px',
    pagePaddingValue,
    capsuleAddHeight: 10,
    ...menuButtonInfo,
  };
  /*#endif*/

  return {
    systemInfo,
    windowInfo,
    capsuleInfo,
    navInfo: createNavInfo(windowInfo, capsuleInfo),
  };
}
const baseInfo = getPageBaseInfo();
const pageBaseInfo = {
  // 当前的运行环境
  env: baseInfo.systemInfo.platform,
  //获取小程序内胶囊的信息
  ...baseInfo,
};
export { pageBaseInfo };
