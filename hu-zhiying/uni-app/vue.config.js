/*
 * @Author: 许栋
 * @Date: 2026-03-23 15:48:58
 * @LastEditors: Do not edit
 * @LastEditTime: 2026-03-23 15:49:00
 * @Description:
 * @FilePath: \呼之应\vue.config.js
 */
module.exports = {
  css: {
    loaderOptions: {
      scss: {
        additionalData: `@import "@/uni.scss";`,
      },
    },
  },
};
