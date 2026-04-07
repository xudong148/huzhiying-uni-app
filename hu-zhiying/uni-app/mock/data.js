export const hotKeywords = ['空调不制冷', '智能锁安装', '厨房漏水', '深度保洁', '油烟机清洗'];

export const cityList = [
  { id: 1, name: '上海', district: '浦东新区', hot: true },
  { id: 2, name: '杭州', district: '滨江区', hot: true },
  { id: 3, name: '苏州', district: '工业园区', hot: false },
  { id: 4, name: '南京', district: '建邺区', hot: false },
];

export const categoryTree = [
  {
    id: 1,
    name: '家电清洗',
    icon: '/static/icons/washing-machine.svg',
    subs: ['空调清洗', '洗衣机清洗', '冰箱除味'],
    groups: [
      {
        title: '热门项目',
        list: [
          { id: 101, name: '空调深度清洗', icon: '/static/icons/washing-machine.svg', price: 89 },
          { id: 102, name: '洗衣机拆洗', icon: '/static/icons/washing-machine.svg', price: 129 },
          { id: 103, name: '油烟机蒸汽清洗', icon: '/static/icons/cleaning.svg', price: 168 },
        ],
      },
    ],
  },
  {
    id: 2,
    name: '专业维修',
    icon: '/static/icons/screwdriver.svg',
    subs: ['空调维修', '热水器维修', '电视维修'],
    groups: [
      {
        title: '故障快修',
        list: [
          { id: 201, name: '空调不制冷', icon: '/static/icons/screwdriver.svg', price: 58 },
          { id: 202, name: '热水器不出热水', icon: '/static/icons/screwdriver.svg', price: 69 },
          { id: 203, name: '冰箱不制冷', icon: '/static/icons/screwdriver.svg', price: 88 },
        ],
      },
    ],
  },
  {
    id: 3,
    name: '上门安装',
    icon: '/static/icons/installation.svg',
    subs: ['智能锁安装', '灯具安装', '浴霸安装'],
    groups: [
      {
        title: '安装服务',
        list: [
          { id: 301, name: '智能锁标准安装', icon: '/static/icons/installation.svg', price: 128 },
          { id: 302, name: '电视挂装', icon: '/static/icons/installation.svg', price: 139 },
          { id: 303, name: '厨卫五金安装', icon: '/static/icons/installation.svg', price: 98 },
        ],
      },
    ],
  },
  {
    id: 4,
    name: '保洁收纳',
    icon: '/static/icons/cleaning.svg',
    subs: ['日常保洁', '深度开荒', '衣橱收纳'],
    groups: [
      {
        title: '品质保洁',
        list: [
          { id: 401, name: '日常保洁 3 小时', icon: '/static/icons/cleaning.svg', price: 135 },
          { id: 402, name: '全屋深度开荒', icon: '/static/icons/cleaning.svg', price: 388 },
          { id: 403, name: '收纳整理', icon: '/static/icons/cleaning.svg', price: 199 },
        ],
      },
    ],
  },
  {
    id: 5,
    name: '管道疏通',
    icon: '/static/icons/pipeline.svg',
    subs: ['马桶疏通', '地漏疏通', '漏水检测'],
    groups: [
      {
        title: '应急处理',
        list: [
          { id: 501, name: '马桶极速疏通', icon: '/static/icons/pipeline.svg', price: 118 },
          { id: 502, name: '地漏返味检测', icon: '/static/icons/pipeline.svg', price: 98 },
          { id: 503, name: '暗管漏水检测', icon: '/static/icons/pipeline.svg', price: 168 },
        ],
      },
    ],
  },
];

export const ecologyEntries = [
  {
    id: 1,
    name: '小应学堂',
    desc: '师傅培训与故障案例',
    icon: '/static/icons/school.svg',
    color: '#2B5CFF',
  },
  {
    id: 2,
    name: '小应商城',
    desc: '五金耗材与安装套餐',
    icon: '/static/icons/mall.svg',
    color: '#FF7D00',
  },
  {
    id: 3,
    name: '同城圈子',
    desc: '邻里互助和口碑内容',
    icon: '/static/icons/community.svg',
    color: '#00B578',
  },
];

export const recommendationList = [
  {
    id: 1,
    title: '空调深度清洗 杀菌除味',
    tag: '不净全退',
    price: 89,
    sales: '2.1k',
    image: 'https://picsum.photos/480/640?random=101',
  },
  {
    id: 2,
    title: '智能锁安装 含调试与联网',
    tag: '品牌认证',
    price: 128,
    sales: '856',
    image: 'https://picsum.photos/480/560?random=102',
  },
  {
    id: 3,
    title: '全屋深度保洁 四区联动',
    tag: '自带工具',
    price: 388,
    sales: '1.8k',
    image: 'https://picsum.photos/480/700?random=103',
  },
  {
    id: 4,
    title: '暗管漏水检测 精准定位',
    tag: '不修不收',
    price: 168,
    sales: '1.2k',
    image: 'https://picsum.photos/480/620?random=104',
  },
];

export const goodsDetail = {
  id: 201,
  title: '空调不制冷 上门维修',
  subtitle: '基础检测 + 故障排查 + 维保建议',
  basePrice: 58,
  doorPrice: 30,
  guidePrice: '58 - 299',
  warranty: '90 天平台质保',
  images: [
    'https://picsum.photos/960/720?random=211',
    'https://picsum.photos/960/720?random=212',
    'https://picsum.photos/960/720?random=213',
  ],
  guarantees: ['不修不收', '迟到赔', '实名认证', '保证金先赔'],
  process: [
    '在线描述故障并预约上门时间',
    '平台智能派单或师傅抢单',
    '师傅上门检测并出具处理方案',
    '如有增项需经用户确认后施工',
    '完工验收与电子报告归档',
  ],
  comments: [
    {
      id: 1,
      user: '赵女士',
      score: 5,
      content: '响应很快，到场前电话确认，增项也讲得很清楚。',
      images: ['https://picsum.photos/360/240?random=214'],
      date: '2026-04-06',
    },
    {
      id: 2,
      user: '吴先生',
      score: 4,
      content: '收费透明，现场解决了冷媒不足的问题。',
      images: [],
      date: '2026-04-02',
    },
  ],
};

export const addressList = [
  {
    id: 1,
    tag: '家',
    name: '周女士',
    mobile: '138****5288',
    address: '上海市浦东新区张江高科园区 88 号 2 幢 1602',
    latitude: 31.2253,
    longitude: 121.5443,
    default: true,
  },
  {
    id: 2,
    tag: '公司',
    name: '周女士',
    mobile: '138****5288',
    address: '上海市徐汇区桂平路 410 号 10 楼',
    latitude: 31.1692,
    longitude: 121.4191,
    default: false,
  },
];

export const timeSlots = [
  '今天 14:00-16:00',
  '今天 16:00-18:00',
  '今晚 19:00-21:00',
  '明天 09:00-11:00',
];

export const orderList = [
  {
    id: 'SO20260408001',
    type: 'service',
    title: '空调不制冷 上门维修',
    status: '待接单',
    statusText: '附近师傅正在抢单',
    appointment: '今天 14:00-16:00',
    address: '浦东新区张江高科园区 88 号',
    price: 88,
    tags: ['预付上门费', '不修不收'],
  },
  {
    id: 'SO20260407009',
    type: 'service',
    title: '智能锁安装',
    status: '待补款',
    statusText: '师傅已提交增项报价',
    appointment: '明天 09:00-11:00',
    address: '徐汇区桂平路 410 号',
    price: 258,
    tags: ['用户待确认', '安装服务'],
  },
  {
    id: 'PO20260406018',
    type: 'product',
    title: '智能锁 Pro 套装',
    status: '待发货',
    statusText: '仓库已打单待出库',
    appointment: '预计后天送达',
    address: '浦东新区张江高科园区 88 号',
    price: 1699,
    tags: ['商城订单'],
  },
];

export const orderTimeline = [
  { key: 'created', label: '订单创建', desc: '用户已完成预付款并提交故障描述', done: true },
  { key: 'dispatch', label: '派单中', desc: '系统已推送给 20km 内匹配师傅', done: true },
  { key: 'accepted', label: '师傅接单', desc: '张师傅已接单并准备出发', done: true },
  { key: 'arrival', label: '上门检测', desc: '等待师傅到达并提交检测结果', done: false },
  { key: 'finish', label: '服务完成', desc: '完工后生成电子服务报告', done: false },
];

export const quotationDetail = {
  id: 'QT20260408001',
  orderId: 'SO20260407009',
  total: 170,
  items: [
    { id: 1, name: '锁体加固件', amount: 70 },
    { id: 2, name: '门体加厚打磨', amount: 100 },
  ],
  remark: '现场门体厚度超出标准安装范围，需要增加配件与打磨工时。',
};

export const couponList = [
  {
    id: 1,
    title: '新用户上门立减券',
    amount: 30,
    threshold: '满 99 元可用',
    expireAt: '2026-04-30',
  },
  {
    id: 2,
    title: '家电清洗专享券',
    amount: 50,
    threshold: '满 199 元可用',
    expireAt: '2026-05-20',
  },
];

export const chatMessages = [
  { id: 1, type: 'system', content: '订单已分配给张师傅，预计 26 分钟到达。', time: '14:02' },
  { id: 2, type: 'master', content: '您好，我已经出发，麻烦确认一下门禁方式。', time: '14:06' },
  { id: 3, type: 'user', content: '到楼下给我打电话，我下来接您。', time: '14:07' },
];

export const masterOrders = [
  {
    id: 'DISP-001',
    title: '空调不制冷 上门维修',
    income: 168,
    distance: '3.2km',
    address: '浦东新区张江高科园区',
    tags: ['空调维修', '预估 60 分钟'],
  },
  {
    id: 'DISP-002',
    title: '智能锁标准安装',
    income: 198,
    distance: '5.6km',
    address: '徐汇区漕河泾园区',
    tags: ['安装服务', '配件自带'],
  },
];

export const walletData = {
  balance: 12860,
  frozen: 3000,
  todayIncome: 860,
  transactions: [
    { id: 1, title: 'SO20260406018 智能锁安装结算', amount: 268, time: '今天 10:18' },
    { id: 2, title: 'SO20260405022 空调维修结算', amount: 198, time: '昨天 21:10' },
    { id: 3, title: '保证金冻结', amount: -3000, time: '2026-04-01' },
  ],
};

export const adminDashboard = {
  gmv: 256800,
  serviceOrders: 328,
  productOrders: 96,
  onlineMasters: 56,
};
