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

function normalizeEcosystemCards(cards = []) {
  return cards.map((card) => ({
    id: card.id,
    name: card.name,
    desc: card.desc || card.description || '',
    color: card.color || '#2B5CFF',
    icon: normalizeRuntimeUrl(card.icon) || '/static/icons/pipeline.svg',
    link: normalizeEcosystemLink(card.link || ''),
  }));
}

function normalizeEcosystemLink(link = '') {
  if (link === '/pages/category/index?scene=mall') {
    return '/pages/mall/index';
  }
  return link;
}

function normalizeProductList(products = []) {
  return products.map((item) => {
    const skus = item.skus || [];
    const firstSku = skus[0] || {};
    const price = item.discountPrice || item.price || firstSku.price || 0;
    const tagPrice = item.tagPrice || firstSku.tagPrice || price;
    return {
      ...item,
      title: item.name,
      summary: item.description || '',
      price,
      tagPrice,
      skuCount: skus.length,
      coverImage: normalizeRuntimeUrl(item.image || item.imageUrl) || '/static/icons/mall.svg',
    };
  });
}

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
      ecosystemCards: normalizeEcosystemCards(data.ecosystemCards || []),
      recommendationList: (data.recommendations || []).map((item) => ({
        ...item,
        image: normalizeRuntimeUrl(item.image) || '/static/icons/screwdriver.svg',
      })),
      notices: data.notices || [],
      banners: data.banners || [],
    },
  };
}

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

export async function getProductList() {
  const response = await request({
    url: '/api/products',
  });
  return {
    data: normalizeProductList(response.data || []),
  };
}

export function searchCatalog(keyword = '') {
  const query = keyword ? `?keyword=${encodeURIComponent(keyword)}` : '';
  return request({ url: `/api/search${query}` });
}

export async function getAcademyCategories() {
  const response = await request({ url: '/api/academy/categories' });
  return { data: response.data || [] };
}

export async function getAcademyArticles(categoryId) {
  const query = categoryId ? `?categoryId=${encodeURIComponent(categoryId)}` : '';
  const response = await request({ url: `/api/academy/articles${query}` });
  return {
    data: (response.data || []).map((item) => ({
      ...item,
      coverImage: normalizeRuntimeUrl(item.coverImage) || '/static/icons/school.svg',
    })),
  };
}

export async function getAcademyArticleDetail(id) {
  const response = await request({ url: `/api/academy/articles/${id}` });
  return {
    data: {
      ...response.data,
      coverImage: normalizeRuntimeUrl(response.data?.coverImage) || '/static/icons/school.svg',
    },
  };
}

export async function getCommunityPosts(cityName = '') {
  const query = cityName ? `?cityName=${encodeURIComponent(cityName)}` : '';
  const response = await request({ url: `/api/community/posts${query}` });
  return {
    data: (response.data || []).map((item) => ({
      ...item,
      coverImage: normalizeRuntimeUrl(item.coverImage) || '/static/icons/community.svg',
      images: (item.images || []).map(normalizeRuntimeUrl),
    })),
  };
}

export async function getCommunityPostDetail(id) {
  const response = await request({ url: `/api/community/posts/${id}` });
  const detail = response.data || {};
  return {
    data: {
      ...detail,
      post: detail.post
        ? {
            ...detail.post,
            coverImage: normalizeRuntimeUrl(detail.post.coverImage) || '/static/icons/community.svg',
            images: (detail.post.images || []).map(normalizeRuntimeUrl),
          }
        : null,
      comments: detail.comments || [],
    },
  };
}

export function createCommunityPost(payload) {
  return request({
    url: '/api/community/posts',
    method: 'POST',
    data: payload,
  });
}

export function createCommunityComment(postId, payload) {
  return request({
    url: `/api/community/posts/${postId}/comments`,
    method: 'POST',
    data: payload,
  });
}

export function likeCommunityPost(postId) {
  return request({
    url: `/api/community/posts/${postId}/like`,
    method: 'POST',
  });
}

export function reportCommunityPost(postId, payload) {
  return request({
    url: `/api/community/posts/${postId}/report`,
    method: 'POST',
    data: payload,
  });
}
