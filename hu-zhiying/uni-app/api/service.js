import { buildAbsoluteUrl, request } from '../utils/request';

function normalizeRuntimeUrl(url) {
  if (!url) {
    return url;
  }
  if (/^https?:\/\//.test(url) || url.startsWith('/static/')) {
    return url;
  }
  if (url.startsWith('/')) {
    return buildAbsoluteUrl(url);
  }
  return url;
}

function normalizeCategoryTree(categories = []) {
  return categories.map((category) => ({
    id: category.id,
    name: category.name,
    icon: normalizeRuntimeUrl(category.icon) || '/static/icons/pipeline.svg',
    subs: category.subs || [],
  }));
}

/**
 * 获取首页聚合数据。
 */
export async function getHomeData() {
  const response = await request({ url: '/api/home' });
  const data = response.data || {};

  return {
    data: {
      location: data.location || '上海',
      serviceCities: data.serviceCities || [],
      hotKeywords: data.hotKeywords || [],
      categories: normalizeCategoryTree(data.categoryNav || []),
      svipCard: data.svipCard || null,
      ecosystemCards: data.ecosystemCards || [],
      recommendationList: (data.recommendations || []).map((item) => ({
        ...item,
        image: normalizeRuntimeUrl(item.image) || '/static/icons/screwdriver.svg',
      })),
      notices: data.notices || [],
      banners: data.banners || [],
    },
  };
}

/**
 * 获取类目树。
 */
export async function getCategoryTree() {
  const response = await request({ url: '/api/categories' });
  return {
    data: (response.data || []).map((category) => ({
      id: category.id,
      name: category.name,
      icon: normalizeRuntimeUrl(category.icon) || '/static/icons/pipeline.svg',
      subs: (category.services || []).map((service) => service.name),
      groups: [
        {
          title: '精选服务',
          list: (category.services || []).map((service) => ({
            id: service.id,
            name: service.name,
            icon: normalizeRuntimeUrl(category.icon) || '/static/icons/pipeline.svg',
            price: service.basePrice,
          })),
        },
      ],
    })),
  };
}

/**
 * 获取服务详情。
 */
export async function getServiceDetail(serviceItemId = 201) {
  const response = await request({
    url: `/api/services/${serviceItemId}`,
  });
  return {
    data: {
      ...response.data,
      images: (response.data?.images || []).map(normalizeRuntimeUrl),
    },
  };
}

/**
 * 获取服务评论。
 */
export function getServiceComments(serviceItemId = 201) {
  return request({
    url: `/api/services/${serviceItemId}/comments`,
  }).then((response) => ({
    data: (response.data || []).map((item) => ({
      ...item,
      images: (item.images || []).map(normalizeRuntimeUrl),
    })),
  }));
}

/**
 * 获取商品详情。
 */
export async function getProductDetail(productId = 1001) {
  const response = await request({
    url: `/api/products/${productId}`,
  });
  return {
    data: {
      ...response.data,
      images: (response.data?.images || []).map(normalizeRuntimeUrl),
    },
  };
}

/**
 * 搜索服务和商品。
 */
export function searchCatalog(keyword = '') {
  const query = keyword ? `?keyword=${encodeURIComponent(keyword)}` : '';
  return request({ url: `/api/search${query}` });
}
