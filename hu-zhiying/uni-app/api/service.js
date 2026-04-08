import { categoryTree, goodsDetail, hotKeywords, recommendationList } from '../mock/data';
import { request, shouldUseMock } from '../utils/request';

const clone = (payload) => JSON.parse(JSON.stringify(payload));

const mockProductDetail = {
  id: 1001,
  title: '智能锁 Pro 套装',
  subtitle: '支持购买后自动生成安装工单，含联网调试与基础教学',
  price: 1699,
  images: ['https://picsum.photos/960/720?random=501'],
  createInstallOrder: true,
  deliveryDesc: '支付成功后自动生成安装工单，平台同步派单',
  highlights: ['正品保障', '包安装调试', '电子质保单'],
  skus: [
    { id: 1, name: '雅黑标准款', price: 1699, stock: 36 },
    { id: 2, name: '星空灰旗舰款', price: 1899, stock: 18 },
  ],
};

function mapCategoryTree(categories = []) {
  return categories.map((category) => ({
    id: category.id,
    name: category.name,
    icon: category.icon,
    subs: (category.services || []).map((service) => service.name),
    groups: [
      {
        title: '精选服务',
        list: (category.services || []).map((service) => ({
          id: service.id,
          name: service.name,
          icon: category.icon,
          price: service.basePrice,
        })),
      },
    ],
  }));
}

export async function getHomeData() {
  if (shouldUseMock()) {
    return {
      data: {
        hotKeywords: clone(hotKeywords),
        categories: clone(categoryTree),
        recommendationList: clone(recommendationList),
      },
    };
  }

  const [categoryRes, searchRes, userRes] = await Promise.all([
    request({ url: '/api/categories' }),
    request({ url: '/api/search' }),
    request({ url: '/api/users/me' }),
  ]);

  const categories = mapCategoryTree(categoryRes.data || []);
  const docs = searchRes.data || [];
  const keywords = docs.slice(0, 5).map((item) => item.title);
  const feed = docs.slice(0, 6).map((item, index) => ({
    id: Number(String(item.id).replace(/\D/g, '')) || index + 1,
    title: item.title,
    tag: item.type === 'product' ? '商城推荐' : '上门服务',
    price: item.price,
    sales: `${index + 1}.2k`,
    image: `https://picsum.photos/480/${620 + index * 20}?random=${index + 101}`,
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

export async function getCategoryTree() {
  if (shouldUseMock()) {
    return { data: clone(categoryTree) };
  }

  const response = await request({ url: '/api/categories' });
  return {
    data: mapCategoryTree(response.data || []),
  };
}

export async function getServiceDetail(serviceItemId = 201) {
  if (shouldUseMock()) {
    return { data: clone(goodsDetail) };
  }

  const response = await request({
    url: `/api/services/${serviceItemId}`,
  });
  return {
    data: response.data,
  };
}

export async function getProductDetail(productId = 1001) {
  if (shouldUseMock()) {
    return { data: clone(mockProductDetail) };
  }

  const response = await request({
    url: `/api/products/${productId}`,
  });
  return {
    data: response.data,
  };
}

export async function searchCatalog(keyword = '') {
  if (shouldUseMock()) {
    const list = [
      ...recommendationList.map((item) => ({
        id: `service-${item.id}`,
        type: 'service',
        title: item.title,
        summary: item.tag,
        price: item.price,
        icon: '/static/icons/screwdriver.svg',
      })),
      {
        id: 'product-1001',
        type: 'product',
        title: '智能锁 Pro 套装',
        summary: '购买后自动生成安装工单',
        price: 1699,
        icon: '/static/icons/mall.svg',
      },
    ].filter((item) => !keyword || item.title.includes(keyword) || item.summary.includes(keyword));
    return { data: list };
  }

  const query = keyword ? `?keyword=${encodeURIComponent(keyword)}` : '';
  return request({ url: `/api/search${query}` });
}
