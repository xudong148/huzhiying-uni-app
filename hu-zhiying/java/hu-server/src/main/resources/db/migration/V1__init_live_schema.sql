CREATE TABLE IF NOT EXISTS users (
  id BIGINT PRIMARY KEY,
  nickname VARCHAR(64) NOT NULL,
  mobile VARCHAR(32) NOT NULL,
  role_code VARCHAR(32) NOT NULL,
  avatar VARCHAR(255),
  level_name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS addresses (
  id BIGINT PRIMARY KEY,
  user_id BIGINT NOT NULL,
  tag_name VARCHAR(32),
  contact_name VARCHAR(64) NOT NULL,
  mobile VARCHAR(32) NOT NULL,
  detail_address VARCHAR(255) NOT NULL,
  city_name VARCHAR(64),
  district_name VARCHAR(64),
  latitude DOUBLE,
  longitude DOUBLE,
  is_default BOOLEAN
);

CREATE TABLE IF NOT EXISTS master_profiles (
  id BIGINT PRIMARY KEY,
  user_id BIGINT NOT NULL,
  real_name VARCHAR(64) NOT NULL,
  skill_tags VARCHAR(255),
  service_area VARCHAR(255),
  deposit DECIMAL(12,2),
  credit_score INT,
  online BOOLEAN,
  listening BOOLEAN,
  max_distance_km INT,
  privacy_number BOOLEAN
);

CREATE TABLE IF NOT EXISTS service_categories (
  id BIGINT PRIMARY KEY,
  name VARCHAR(64) NOT NULL,
  icon VARCHAR(255),
  sort_order INT
);

CREATE TABLE IF NOT EXISTS service_items (
  id BIGINT PRIMARY KEY,
  category_id BIGINT NOT NULL,
  name VARCHAR(128) NOT NULL,
  subtitle VARCHAR(255),
  base_price DECIMAL(12,2),
  door_price DECIMAL(12,2),
  guide_price VARCHAR(64),
  warranty_text VARCHAR(128),
  guarantees_text TEXT,
  tags_text TEXT,
  image_urls TEXT,
  process_steps TEXT
);

CREATE TABLE IF NOT EXISTS products (
  id BIGINT PRIMARY KEY,
  name VARCHAR(128) NOT NULL,
  description_text VARCHAR(255),
  price DECIMAL(12,2),
  create_install_order BOOLEAN,
  image_url VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS skus (
  id BIGINT PRIMARY KEY,
  product_id BIGINT NOT NULL,
  name VARCHAR(128) NOT NULL,
  price DECIMAL(12,2),
  stock INT
);

CREATE TABLE IF NOT EXISTS service_orders (
  id VARCHAR(64) PRIMARY KEY,
  service_item_id BIGINT,
  title VARCHAR(128) NOT NULL,
  status VARCHAR(32) NOT NULL,
  payment_status VARCHAR(32) NOT NULL,
  user_id BIGINT NOT NULL,
  address_id BIGINT NOT NULL,
  master_user_id BIGINT,
  appointment VARCHAR(64),
  amount DECIMAL(12,2),
  dispatch_mode VARCHAR(32),
  eta_text VARCHAR(64),
  description_text TEXT,
  emergency BOOLEAN,
  night_service BOOLEAN,
  created_at TIMESTAMP,
  updated_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS product_orders (
  id VARCHAR(64) PRIMARY KEY,
  product_id BIGINT NOT NULL,
  title VARCHAR(128) NOT NULL,
  status VARCHAR(32) NOT NULL,
  payment_status VARCHAR(32) NOT NULL,
  user_id BIGINT NOT NULL,
  address_id BIGINT NOT NULL,
  amount DECIMAL(12,2),
  create_install_order BOOLEAN,
  install_service_order_id VARCHAR(64),
  created_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS dispatch_tasks (
  id VARCHAR(64) PRIMARY KEY,
  order_id VARCHAR(64) NOT NULL,
  title VARCHAR(128) NOT NULL,
  income DECIMAL(12,2),
  distance_text VARCHAR(32),
  area_text VARCHAR(64),
  dispatch_mode VARCHAR(32),
  current_master_user_id BIGINT,
  task_status VARCHAR(32),
  tags_text TEXT,
  created_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS work_step_records (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  order_id VARCHAR(64) NOT NULL,
  step_key VARCHAR(64),
  label_text VARCHAR(64),
  description_text VARCHAR(255),
  done BOOLEAN,
  step_time TIMESTAMP
);

CREATE TABLE IF NOT EXISTS quotations (
  id VARCHAR(64) PRIMARY KEY,
  order_id VARCHAR(64) NOT NULL,
  total_amount DECIMAL(12,2),
  status VARCHAR(32),
  remark_text VARCHAR(255),
  created_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS quotation_items (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  quotation_id VARCHAR(64) NOT NULL,
  name VARCHAR(128) NOT NULL,
  amount DECIMAL(12,2)
);

CREATE TABLE IF NOT EXISTS wallet_accounts (
  id BIGINT PRIMARY KEY,
  master_user_id BIGINT NOT NULL,
  available_amount DECIMAL(12,2),
  frozen_amount DECIMAL(12,2),
  today_income DECIMAL(12,2)
);

CREATE TABLE IF NOT EXISTS wallet_transactions (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  wallet_account_id BIGINT NOT NULL,
  title VARCHAR(255) NOT NULL,
  amount DECIMAL(12,2),
  transaction_time VARCHAR(64)
);

CREATE TABLE IF NOT EXISTS message_sessions (
  id VARCHAR(64) PRIMARY KEY,
  order_id VARCHAR(64),
  title VARCHAR(128) NOT NULL,
  participant_user_id BIGINT
);

CREATE TABLE IF NOT EXISTS message_items (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  session_id VARCHAR(64) NOT NULL,
  sender_code VARCHAR(64),
  message_type VARCHAR(32),
  content_text VARCHAR(255),
  message_time VARCHAR(64)
);

CREATE TABLE IF NOT EXISTS banners (
  id BIGINT PRIMARY KEY,
  title VARCHAR(128) NOT NULL,
  subtitle VARCHAR(255),
  image VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS notices (
  id BIGINT PRIMARY KEY,
  title VARCHAR(255) NOT NULL,
  level_code VARCHAR(32)
);

CREATE TABLE IF NOT EXISTS arbitration_cases (
  id VARCHAR(64) PRIMARY KEY,
  order_id VARCHAR(64) NOT NULL,
  reason_text VARCHAR(255),
  status_text VARCHAR(64)
);

CREATE TABLE IF NOT EXISTS coupons (
  id BIGINT PRIMARY KEY,
  title VARCHAR(128) NOT NULL,
  amount DECIMAL(12,2),
  threshold_text VARCHAR(64),
  expire_at VARCHAR(32)
);

CREATE TABLE IF NOT EXISTS member_levels (
  id BIGINT PRIMARY KEY,
  name VARCHAR(64) NOT NULL,
  benefit_text VARCHAR(255),
  points_required INT
);

INSERT INTO users (id, nickname, mobile, role_code, avatar, level_name) VALUES
  (10001, '平台用户', '13900001234', 'USER', '/static/user.png', '平台会员'),
  (20001, '服务技师', '13700004567', 'MASTER', '/static/user.png', '平台认证技师'),
  (90001, '运营后台', '400-000-0000', 'ADMIN', '/static/user.png', '超级管理员');

INSERT INTO addresses (id, user_id, tag_name, contact_name, mobile, detail_address, city_name, district_name, latitude, longitude, is_default) VALUES
  (1, 10001, '家', '联系人', '13900001234', '上海市浦东新区张江高科技园区 88 号 12 幢 1602', '上海', '浦东新区', 31.2253, 121.5443, TRUE),
  (2, 10001, '公司', '联系人', '13900001234', '上海市徐汇区桂平路 410 号 10 楼', '上海', '徐汇区', 31.1692, 121.4191, FALSE);

INSERT INTO master_profiles (id, user_id, real_name, skill_tags, service_area, deposit, credit_score, online, listening, max_distance_km, privacy_number) VALUES
  (1, 20001, '服务技师', '空调维修|智能锁安装', '浦东新区|徐汇区', 3000.00, 98, TRUE, TRUE, 20, TRUE);

INSERT INTO service_categories (id, name, icon, sort_order) VALUES
  (2, '专业维修', '/static/icons/screwdriver.svg', 1),
  (3, '上门安装', '/static/icons/installation.svg', 2),
  (4, '保洁收纳', '/static/icons/cleaning.svg', 3);

INSERT INTO service_items (id, category_id, name, subtitle, base_price, door_price, guide_price, warranty_text, guarantees_text, tags_text, image_urls, process_steps) VALUES
  (201, 2, '空调不制冷上门维修', '基础检测 + 故障排查', 58.00, 30.00, '58 - 299', '90 天平台质保', '不修不收|迟到赔付|实名认证|保证金先赔', '空调维修|快速上门', '/seed-media/service-aircon.svg|/seed-media/service-aircon.svg|/seed-media/service-aircon.svg', '在线描述故障并预约上门时间|平台智能派单或师傅抢单|师傅上门检测并出具处理方案|增项需用户确认后施工|完工验收与电子报告归档'),
  (301, 3, '智能锁标准安装', '含调试联网', 128.00, 0.00, '128 - 388', '30 天安装质保', '品牌认证|安装质保', '安装服务|配件自带', '/seed-media/service-lock.svg|/seed-media/service-lock.svg', '确认门型与锁型|预约安装时间|现场开孔和调试|联网测试|交付讲解'),
  (401, 4, '日常保洁 3 小时', '厨房 / 卫生间 / 客厅', 135.00, 0.00, '135 - 388', '服务完成后可追溯', '自带工具|平台背书', '保洁|高频服务', '/seed-media/service-cleaning.svg|/seed-media/service-cleaning.svg', '确认保洁范围|师傅上门签到|分区作业|用户验收');

INSERT INTO products (id, name, description_text, price, create_install_order, image_url) VALUES
  (1001, '智能锁 Pro 套装', '支持购买后自动生成安装工单', 1699.00, TRUE, '/seed-media/product-lock.svg'),
  (1002, '空调清洗年卡', '全年 2 次深度清洗', 499.00, FALSE, '/seed-media/product-card.svg');

INSERT INTO skus (id, product_id, name, price, stock) VALUES
  (1, 1001, '雅黑标准款', 1699.00, 36),
  (2, 1002, '家庭卡', 499.00, 88);

INSERT INTO service_orders (id, service_item_id, title, status, payment_status, user_id, address_id, master_user_id, appointment, amount, dispatch_mode, eta_text, description_text, emergency, night_service, created_at, updated_at) VALUES
  ('SO20260408001', 201, '空调不制冷上门维修', 'PENDING_ACCEPT', 'PARTIAL_PAID', 10001, 1, NULL, '今天 14:00-16:00', 88.00, 'ROB', '26 分钟', '客厅空调制冷效果较差，外机有异响。', FALSE, FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('SO20260407009', 301, '智能锁标准安装', 'WAITING_SUPPLEMENT_PAYMENT', 'PARTIAL_PAID', 10001, 1, 20001, '明天 09:00-11:00', 258.00, 'FORCE_ASSIGN', '15 分钟', '需要安装智能锁并完成联网调试。', FALSE, FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO product_orders (id, product_id, title, status, payment_status, user_id, address_id, amount, create_install_order, install_service_order_id, created_at) VALUES
  ('PO20260406018', 1001, '智能锁 Pro 套装', 'PENDING_SHIPMENT', 'PAID', 10001, 1, 1699.00, TRUE, 'SO20260407009', CURRENT_TIMESTAMP);

INSERT INTO dispatch_tasks (id, order_id, title, income, distance_text, area_text, dispatch_mode, current_master_user_id, task_status, tags_text, created_at) VALUES
  ('DISP-001', 'SO20260408001', '空调不制冷上门维修', 168.00, '3.2km', '浦东新区', 'ROB', NULL, 'PENDING', '空调维修|预计 60 分钟', CURRENT_TIMESTAMP),
  ('DISP-002', 'SO20260407009', '智能锁标准安装', 198.00, '5.6km', '徐汇区', 'FORCE_ASSIGN', 20001, 'ASSIGNED', '安装服务|配件自带', CURRENT_TIMESTAMP);

INSERT INTO work_step_records (order_id, step_key, label_text, description_text, done, step_time) VALUES
  ('SO20260408001', 'created', '订单创建', '用户已完成预付款并提交故障描述', TRUE, CURRENT_TIMESTAMP),
  ('SO20260408001', 'dispatch', '派单中', '系统已推送给 20km 内匹配师傅', TRUE, CURRENT_TIMESTAMP),
  ('SO20260408001', 'accepted', '师傅接单', '等待师傅出发', FALSE, CURRENT_TIMESTAMP),
  ('SO20260407009', 'created', '订单创建', '用户已提交安装工单', TRUE, CURRENT_TIMESTAMP),
  ('SO20260407009', 'assigned', '强派完成', '平台已将订单指派给服务技师', TRUE, CURRENT_TIMESTAMP),
  ('SO20260407009', 'quotation', '增项报价', '现场门体厚度超出标准安装范围，等待用户确认', TRUE, CURRENT_TIMESTAMP);

INSERT INTO quotations (id, order_id, total_amount, status, remark_text, created_at) VALUES
  ('QT20260408001', 'SO20260407009', 170.00, 'PENDING_CONFIRM', '现场门体厚度超出标准安装范围，需要新增配件和工时。', CURRENT_TIMESTAMP);

INSERT INTO quotation_items (quotation_id, name, amount) VALUES
  ('QT20260408001', '锁体加固件', 70.00),
  ('QT20260408001', '门体加厚打磨', 100.00);

INSERT INTO wallet_accounts (id, master_user_id, available_amount, frozen_amount, today_income) VALUES
  (1, 20001, 12860.00, 3000.00, 860.00);

INSERT INTO wallet_transactions (wallet_account_id, title, amount, transaction_time) VALUES
  (1, 'SO20260406018 智能锁安装结算', 268.00, '今天 10:18'),
  (1, 'SO20260405022 空调维修结算', 198.00, '昨天 21:10'),
  (1, '保证金冻结', -3000.00, '2026-04-01');

INSERT INTO message_sessions (id, order_id, title, participant_user_id) VALUES
  ('MS-001', 'SO20260407009', '智能锁安装沟通', 20001);

INSERT INTO message_items (session_id, sender_code, message_type, content_text, message_time) VALUES
  ('MS-001', 'system', 'system', '订单已分配给服务技师，预计 26 分钟到达。', '14:02'),
  ('MS-001', 'master', 'text', '您好，我已经出发，麻烦确认一下门禁方式。', '14:06'),
  ('MS-001', 'user', 'text', '到楼下给我打电话，我下来接您。', '14:07');

INSERT INTO banners (id, title, subtitle, image) VALUES
  (1, '家电维修专场', '不修不收，90 天质保', '/seed-media/banner-home.svg');

INSERT INTO notices (id, title, level_code) VALUES
  (1, '夜间服务费：22:00 后自动加收 30%', 'warning'),
  (2, '新用户组合券已到账，首页可直接领取', 'promo');

INSERT INTO arbitration_cases (id, order_id, reason_text, status_text) VALUES
  ('ARB-001', 'SO20260407009', '乱收费', '待裁决');

INSERT INTO coupons (id, title, amount, threshold_text, expire_at) VALUES
  (1, '新用户上门立减券', 30.00, '满 99 减 30', '2026-04-30'),
  (2, '家电清洗专享券', 50.00, '满 199 减 50', '2026-05-20');

INSERT INTO member_levels (id, name, benefit_text, points_required) VALUES
  (1, 'SVIP 金卡', '服务 9 折 / 专属客服 / 优先派单', 2000);
