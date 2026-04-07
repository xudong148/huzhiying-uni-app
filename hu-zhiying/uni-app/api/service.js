import { categoryTree, goodsDetail, hotKeywords, recommendationList } from '../mock/data';

const clone = (payload) => JSON.parse(JSON.stringify(payload));

export function getHomeData() {
  return Promise.resolve({
    data: {
      hotKeywords: clone(hotKeywords),
      categories: clone(categoryTree),
      recommendationList: clone(recommendationList),
    },
  });
}

export function getCategoryTree() {
  return Promise.resolve({
    data: clone(categoryTree),
  });
}

export function getServiceDetail() {
  return Promise.resolve({
    data: clone(goodsDetail),
  });
}
