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

function defaultFeedImage(type) {
  return type === 'product' ? '/static/icons/mall.svg' : '/static/icons/screwdriver.svg';
}

function mapCategoryTree(categories = []) {
  return categories.map((category) => ({
    id: category.id,
    name: category.name,
    icon: category.icon || '/static/icons/pipeline.svg',
    subs: (category.services || []).map((service) => service.name),
    groups: [
      {
        title: '精选服务',
        list: (category.services || []).map((service) => ({
          id: service.id,
          name: service.name,
          icon: category.icon || '/static/icons/pipeline.svg',
          price: service.basePrice,
        })),
      },
    ],
  }));
}

/**
 * 获取首页数据。
 */
export async function getHomeData() {
  const [categoryRes, searchRes, userRes] = await Promise.all([
    request({ url: '/api/categories' }),
    request({ url: '/api/search' }),
    request({ url: '/api/users/me' }),
  ]);

  const categories = mapCategoryTree(categoryRes.data || []);
  const docs = searchRes.data || [];
  const keywords = docs.slice(0, 5).map((item) => item.title);
  const feed = docs.slice(0, 8).map((item, index) => ({
    id: Number(String(item.id).replace(/\D/g, '')) || index + 1,
    title: item.title,
    tag: item.type === 'product' ? '商城推荐' : '上门服务',
    price: item.price,
    sales: `${index + 1}.2k`,
    image: normalizeRuntimeUrl(item.icon) || defaultFeedImage(item.type),
    type: item.type,
  }));

  return {
    data: {
      hotKeywords: keywords,
      categories,
      recommendationList: feed,
      notices: userRes.data?.notices || [],
      banners: userRes.data?.banners || [],
    },
  };
}

/**
 * 获取类目树。
 */
export async function getCategoryTree() {
  const response = await request({ url: '/api/categories' });
  return {
    data: mapCategoryTree(response.data || []),
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
