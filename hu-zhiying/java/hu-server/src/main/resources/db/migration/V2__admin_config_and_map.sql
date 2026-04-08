ALTER TABLE service_categories ADD COLUMN IF NOT EXISTS enabled BOOLEAN DEFAULT TRUE;
ALTER TABLE service_items ADD COLUMN IF NOT EXISTS enabled BOOLEAN DEFAULT TRUE;
ALTER TABLE products ADD COLUMN IF NOT EXISTS enabled BOOLEAN DEFAULT TRUE;
ALTER TABLE skus ADD COLUMN IF NOT EXISTS enabled BOOLEAN DEFAULT TRUE;
ALTER TABLE banners ADD COLUMN IF NOT EXISTS link VARCHAR(255);
ALTER TABLE banners ADD COLUMN IF NOT EXISTS sort_order INT DEFAULT 0;
ALTER TABLE banners ADD COLUMN IF NOT EXISTS enabled BOOLEAN DEFAULT TRUE;
ALTER TABLE notices ADD COLUMN IF NOT EXISTS content_text VARCHAR(255);
ALTER TABLE notices ADD COLUMN IF NOT EXISTS enabled BOOLEAN DEFAULT TRUE;
ALTER TABLE coupons ADD COLUMN IF NOT EXISTS enabled BOOLEAN DEFAULT TRUE;
ALTER TABLE member_levels ADD COLUMN IF NOT EXISTS enabled BOOLEAN DEFAULT TRUE;

CREATE TABLE IF NOT EXISTS agreements (
  id BIGINT PRIMARY KEY,
  title VARCHAR(128) NOT NULL,
  version VARCHAR(64),
  content_text TEXT,
  enabled BOOLEAN DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS roles (
  id BIGINT PRIMARY KEY,
  code VARCHAR(64) NOT NULL,
  name VARCHAR(128) NOT NULL,
  description_text VARCHAR(255),
  enabled BOOLEAN DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS menus (
  id BIGINT PRIMARY KEY,
  name VARCHAR(128) NOT NULL,
  path VARCHAR(255) NOT NULL,
  icon VARCHAR(128),
  sort_order INT DEFAULT 0,
  enabled BOOLEAN DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS permissions (
  id BIGINT PRIMARY KEY,
  code VARCHAR(128) NOT NULL,
  name VARCHAR(128) NOT NULL,
  description_text VARCHAR(255),
  enabled BOOLEAN DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS pricing_rules (
  id BIGINT PRIMARY KEY,
  category_id BIGINT NOT NULL,
  label_text VARCHAR(128) NOT NULL,
  base_price DECIMAL(12,2),
  coefficient VARCHAR(255),
  guide_price VARCHAR(64),
  enabled BOOLEAN DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS dispatch_zones (
  id BIGINT PRIMARY KEY,
  city_name VARCHAR(64) NOT NULL,
  district_name VARCHAR(64) NOT NULL,
  sort_order INT DEFAULT 0,
  enabled BOOLEAN DEFAULT TRUE
);

UPDATE users
SET nickname = '周女士', level_name = 'SVIP 预备用户'
WHERE id = 10001;

UPDATE users
SET nickname = '张师傅', level_name = '认证工程师'
WHERE id = 20001;

UPDATE users
SET nickname = '运营后台', level_name = '超级管理员'
WHERE id = 90001;

UPDATE addresses
SET tag_name = '家',
    contact_name = '周女士',
    detail_address = '上海市浦东新区张江高科技园区 88 号 12 幢 1602',
    city_name = '上海',
    district_name = '浦东新区'
WHERE id = 1;

UPDATE addresses
SET tag_name = '公司',
    contact_name = '周女士',
    detail_address = '上海市徐汇区桂平路 410 号 10 楼',
    city_name = '上海',
    district_name = '徐汇区'
WHERE id = 2;

UPDATE master_profiles
SET real_name = '张师傅',
    skill_tags = '空调维修|智能锁安装',
    service_area = '浦东新区|徐汇区'
WHERE id = 1;

UPDATE service_categories SET name = '专业维修', enabled = TRUE WHERE id = 2;
UPDATE service_categories SET name = '上门安装', enabled = TRUE WHERE id = 3;
UPDATE service_categories SET name = '保洁收纳', enabled = TRUE WHERE id = 4;

UPDATE service_items
SET name = '空调不制冷上门维修',
    subtitle = '基础检测 + 故障排查',
    guide_price = '58 - 299',
    warranty_text = '90 天平台质保',
    guarantees_text = '不修不收费|迟到赔付|实名认证|保证金先赔',
    tags_text = '空调维修|极速上门',
    process_steps = '在线描述故障并预约上门时间|平台智能派单或师傅抢单|师傅上门检测并给出处置方案|增项需用户确认后施工|完工验收与电子报告归档',
    enabled = TRUE
WHERE id = 201;

UPDATE service_items
SET name = '智能锁标准安装',
    subtitle = '含调试联网',
    guide_price = '128 - 388',
    warranty_text = '30 天安装质保',
    guarantees_text = '品牌认证|安装质保',
    tags_text = '安装服务|配件自带',
    process_steps = '确认门型与锁型|预约安装时间|现场开孔和调试|联网测试|交付讲解',
    enabled = TRUE
WHERE id = 301;

UPDATE service_items
SET name = '日常保洁 3 小时',
    subtitle = '厨房 / 卫生间 / 客厅',
    guide_price = '135 - 388',
    warranty_text = '服务完成后可追溯',
    guarantees_text = '自带工具|平台背书',
    tags_text = '保洁|高频服务',
    process_steps = '确认保洁范围|师傅上门签到|分区作业|用户验收',
    enabled = TRUE
WHERE id = 401;

UPDATE products
SET name = '智能锁 Pro 套装',
    description_text = '支持购买后自动生成安装工单',
    enabled = TRUE
WHERE id = 1001;

UPDATE products
SET name = '空调清洗年卡',
    description_text = '全年 2 次深度清洗',
    enabled = TRUE
WHERE id = 1002;

UPDATE skus SET name = '雅黑标准款', enabled = TRUE WHERE id = 1;
UPDATE skus SET name = '家庭版', enabled = TRUE WHERE id = 2;

UPDATE service_orders
SET title = '空调不制冷上门维修',
    appointment = '今天 14:00-16:00',
    eta_text = '26 分钟',
    description_text = '客厅空调制冷效果较差，外机有异响。'
WHERE id = 'SO20260408001';

UPDATE service_orders
SET title = '智能锁标准安装',
    appointment = '明天 09:00-11:00',
    eta_text = '15 分钟',
    description_text = '需要安装智能锁并完成联网调试。'
WHERE id = 'SO20260407009';

UPDATE product_orders
SET title = '智能锁 Pro 套装'
WHERE id = 'PO20260406018';

UPDATE dispatch_tasks
SET title = '空调不制冷上门维修',
    area_text = '浦东新区',
    tags_text = '空调维修|预计 60 分钟'
WHERE id = 'DISP-001';

UPDATE dispatch_tasks
SET title = '智能锁标准安装',
    area_text = '徐汇区',
    tags_text = '安装服务|配件自带'
WHERE id = 'DISP-002';

UPDATE work_step_records
SET label_text = '订单创建',
    description_text = '用户已完成预付款并提交故障描述'
WHERE order_id = 'SO20260408001' AND step_key = 'created';

UPDATE work_step_records
SET label_text = '派单中',
    description_text = '系统已推送给 20km 内匹配师傅'
WHERE order_id = 'SO20260408001' AND step_key = 'dispatch';

UPDATE work_step_records
SET label_text = '师傅接单',
    description_text = '等待师傅出发'
WHERE order_id = 'SO20260408001' AND step_key = 'accepted';

UPDATE work_step_records
SET label_text = '订单创建',
    description_text = '用户已提交安装工单'
WHERE order_id = 'SO20260407009' AND step_key = 'created';

UPDATE work_step_records
SET label_text = '强派完成',
    description_text = '平台已将订单指派给张师傅'
WHERE order_id = 'SO20260407009' AND step_key = 'assigned';

UPDATE work_step_records
SET label_text = '增项报价',
    description_text = '现场门体厚度超出标准安装范围，等待用户确认'
WHERE order_id = 'SO20260407009' AND step_key = 'quotation';

UPDATE quotations
SET remark_text = '现场门体厚度超出标准安装范围，需要新增配件和工时。'
WHERE id = 'QT20260408001';

UPDATE quotation_items SET name = '锁体加固件' WHERE id = 1;
UPDATE quotation_items SET name = '门体加厚打磨' WHERE id = 2;

UPDATE wallet_transactions
SET title = 'SO20260406018 智能锁安装结算'
WHERE id = 1;

UPDATE wallet_transactions
SET title = 'SO20260405022 空调维修结算'
WHERE id = 2;

UPDATE wallet_transactions
SET title = '保证金冻结'
WHERE id = 3;

UPDATE message_sessions
SET title = '智能锁安装沟通'
WHERE id = 'MS-001';

UPDATE message_items
SET content_text = '订单已分配给张师傅，预计 26 分钟到达。'
WHERE id = 1;

UPDATE message_items
SET content_text = '您好，我已经出发，麻烦确认一下门禁方式。'
WHERE id = 2;

UPDATE message_items
SET content_text = '到楼下给我打电话，我下来接您。'
WHERE id = 3;

UPDATE banners
SET title = '家电维修专场',
    subtitle = '不修不收费，90 天质保',
    image = 'https://picsum.photos/960/360?random=901',
    link = '/pages/goods/detail?id=201',
    sort_order = 10,
    enabled = TRUE
WHERE id = 1;

UPDATE notices
SET title = '夜间服务费：22:00 后自动加收 30%',
    content_text = '具体收费以页面公示和实际检测结果为准。',
    level_code = 'warning',
    enabled = TRUE
WHERE id = 1;

UPDATE notices
SET title = '新用户组合券已到账，首页可直接领取',
    content_text = '平台活动每周更新，请留意首页内容位。',
    level_code = 'promo',
    enabled = TRUE
WHERE id = 2;

UPDATE arbitration_cases
SET reason_text = '乱收费',
    status_text = '待裁决'
WHERE id = 'ARB-001';

UPDATE coupons
SET title = '新用户上门立减券',
    threshold_text = '满 99 减 30',
    enabled = TRUE
WHERE id = 1;

UPDATE coupons
SET title = '家电清洗专享券',
    threshold_text = '满 199 减 50',
    enabled = TRUE
WHERE id = 2;

UPDATE member_levels
SET name = 'SVIP 金卡',
    benefit_text = '服务 9 折 / 专属客服 / 优先派单',
    enabled = TRUE
WHERE id = 1;

INSERT INTO agreements (id, title, version, content_text, enabled) VALUES
  (1, '用户服务协议', '2026.04', '平台为用户提供上门服务撮合、履约跟踪和售后仲裁能力。', TRUE),
  (2, '隐私政策', '2026.04', '平台仅在提供服务和合规要求范围内使用用户数据。', TRUE);

INSERT INTO roles (id, code, name, description_text, enabled) VALUES
  (1, 'admin', '平台管理员', '拥有后台所有配置与审核权限', TRUE),
  (2, 'dispatcher', '调度专员', '负责订单派单、改派和履约跟踪', TRUE),
  (3, 'finance', '财务专员', '负责退款审核和结算审核', TRUE);

INSERT INTO menus (id, name, path, icon, sort_order, enabled) VALUES
  (1, '仪表盘', '/dashboard', 'mdi:view-dashboard', 10, TRUE),
  (2, '订单调度中心', '/dispatch', 'mdi:map-marker-radius', 20, TRUE),
  (3, '订单管理', '/orders', 'mdi:clipboard-text-outline', 30, TRUE),
  (4, '师傅管理', '/masters', 'mdi:account-hard-hat', 40, TRUE),
  (5, '定价与类目', '/pricing', 'mdi:cash-register', 50, TRUE),
  (6, '财务结算', '/finance', 'mdi:wallet-outline', 60, TRUE),
  (7, '仲裁中心', '/arbitration', 'mdi:scale-balance', 70, TRUE),
  (8, '优惠券与会员', '/marketing', 'mdi:ticket-percent-outline', 80, TRUE),
  (9, '内容运营', '/content', 'mdi:bullhorn-outline', 90, TRUE),
  (10, '系统权限', '/system', 'mdi:shield-account-outline', 100, TRUE);

INSERT INTO permissions (id, code, name, description_text, enabled) VALUES
  (1, 'dispatch:force-assign', '订单强派', '允许管理员对订单执行强制派单', TRUE),
  (2, 'order:cancel', '订单强制取消', '允许管理员强制取消订单', TRUE),
  (3, 'finance:refund-audit', '退款审核', '允许财务审核退款申请', TRUE),
  (4, 'content:publish', '内容发布', '允许运营发布 Banner 和公告', TRUE);

INSERT INTO pricing_rules (id, category_id, label_text, base_price, coefficient, guide_price, enabled) VALUES
  (1, 2, '空调维修基础价', 58.00, '加急 +20%，夜间 +30%', '58 - 299', TRUE),
  (2, 3, '智能锁安装基础价', 128.00, '加急 +15%，高峰时段 +10%', '128 - 388', TRUE),
  (3, 4, '保洁服务基础价', 135.00, '高峰日 +12%', '135 - 388', TRUE);

INSERT INTO dispatch_zones (id, city_name, district_name, sort_order, enabled) VALUES
  (1, '上海', '浦东新区', 10, TRUE),
  (2, '上海', '徐汇区', 20, TRUE),
  (3, '杭州', '西湖区', 30, TRUE),
  (4, '苏州', '工业园区', 40, TRUE);
