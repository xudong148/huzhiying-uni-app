CREATE TABLE IF NOT EXISTS ecosystem_cards (
  id BIGINT PRIMARY KEY,
  name VARCHAR(64) NOT NULL,
  description_text VARCHAR(255) NOT NULL,
  icon VARCHAR(255) NOT NULL,
  color VARCHAR(32) NOT NULL,
  link VARCHAR(255) NOT NULL,
  sort_order INT NOT NULL DEFAULT 0,
  enabled BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS academy_categories (
  id BIGINT PRIMARY KEY,
  name VARCHAR(64) NOT NULL,
  description_text VARCHAR(255),
  icon VARCHAR(255),
  sort_order INT NOT NULL DEFAULT 0,
  enabled BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS academy_articles (
  id BIGINT PRIMARY KEY,
  category_id BIGINT NOT NULL,
  title VARCHAR(128) NOT NULL,
  summary_text VARCHAR(255),
  cover_image VARCHAR(255),
  content_text TEXT NOT NULL,
  author_name VARCHAR(64),
  sort_order INT NOT NULL DEFAULT 0,
  published BOOLEAN NOT NULL DEFAULT TRUE,
  enabled BOOLEAN NOT NULL DEFAULT TRUE,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS community_posts (
  id BIGINT PRIMARY KEY,
  user_id BIGINT NOT NULL,
  city_name VARCHAR(64) NOT NULL,
  title VARCHAR(128) NOT NULL,
  content_text TEXT NOT NULL,
  images_text TEXT,
  like_count INT NOT NULL DEFAULT 0,
  comment_count INT NOT NULL DEFAULT 0,
  status_code VARCHAR(32) NOT NULL DEFAULT 'PUBLISHED',
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS community_comments (
  id BIGINT PRIMARY KEY,
  post_id BIGINT NOT NULL,
  user_id BIGINT NOT NULL,
  content_text VARCHAR(255) NOT NULL,
  created_at TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS community_reports (
  id BIGINT PRIMARY KEY,
  post_id BIGINT NOT NULL,
  reporter_user_id BIGINT NOT NULL,
  reason_text VARCHAR(255) NOT NULL,
  status_code VARCHAR(32) NOT NULL DEFAULT 'PENDING',
  created_at TIMESTAMP NOT NULL,
  handled_at TIMESTAMP NULL
);

CREATE INDEX idx_academy_articles_category ON academy_articles (category_id, published, enabled);
CREATE INDEX idx_community_posts_city ON community_posts (city_name, status_code, created_at);
CREATE INDEX idx_community_comments_post ON community_comments (post_id, id);
CREATE INDEX idx_community_reports_post ON community_reports (post_id, status_code);

DELETE FROM ecosystem_cards WHERE id IN (1, 2, 3);
INSERT INTO ecosystem_cards (id, name, description_text, icon, color, link, sort_order, enabled) VALUES
  (1, '小应学堂', '金牌技师进阶课与故障案例库', 'https://api.iconify.design/fluent-emoji-flat:graduation-cap.svg', '#2B5CFF', '/pages/academy/index', 10, TRUE),
  (2, '精选商城', '五金耗材与安装配件一站购', 'https://api.iconify.design/fluent-emoji-flat:shopping-cart.svg', '#FF7D00', '/pages/mall/index', 20, TRUE),
  (3, '同城圈子', '本地生活互助与口碑分享', 'https://api.iconify.design/fluent-emoji-flat:handshake.svg', '#00B578', '/pages/community/index', 30, TRUE);

DELETE FROM academy_categories WHERE id IN (1, 2, 3);
INSERT INTO academy_categories (id, name, description_text, icon, sort_order, enabled) VALUES
  (1, '空调与家电', '高频家电故障分析与维护知识', 'https://api.iconify.design/solar:snowflake-bold-duotone.svg?color=%232B5CFF&secondary=%238AB4F8', 10, TRUE),
  (2, '安装与交付', '锁具、灯具、卫浴安装规范', 'https://api.iconify.design/solar:box-bold-duotone.svg?color=%232B5CFF&secondary=%238AB4F8', 20, TRUE),
  (3, '运营与沟通', '上门服务话术、报价与售后处理', 'https://api.iconify.design/solar:chat-round-dots-bold-duotone.svg?color=%232B5CFF&secondary=%238AB4F8', 30, TRUE);

DELETE FROM academy_articles WHERE id IN (1001, 1002, 1003);
INSERT INTO academy_articles (id, category_id, title, summary_text, cover_image, content_text, author_name, sort_order, published, enabled, created_at, updated_at) VALUES
  (1001, 1, '空调不制冷的 6 个现场排查步骤', '先看电源、滤网和外机，再判断是否需要补氟或更换部件。', 'https://picsum.photos/seed/academy-1001/900/540', '适用场景：家用挂机、柜机出现出风不冷或制冷慢。排查建议：1. 确认模式和设定温度。2. 检查滤网和蒸发器积灰。3. 观察外机风扇和压缩机是否启动。4. 查看排水是否异常。5. 记录环境温度与运行电流。6. 最后再判断是否需要补氟或更换核心部件。', '呼之应学院', 10, TRUE, TRUE, TIMESTAMP '2026-04-08 09:00:00', TIMESTAMP '2026-04-08 09:00:00'),
  (1002, 2, '智能锁安装交付清单', '交付前要核对门体、锁体、联网、应急供电和用户交接。', 'https://picsum.photos/seed/academy-1002/900/540', '交付前确认门体厚度、导向片尺寸和天地钩状态。安装中保持孔位平整，避免线路夹伤。交付时完成联网、指纹录入、密码重置和用户教学。', '呼之应学院', 20, TRUE, TRUE, TIMESTAMP '2026-04-08 10:00:00', TIMESTAMP '2026-04-08 10:00:00'),
  (1003, 3, '上门报价的边界与沟通模板', '说明基础项、增项触发条件和用户确认口径，减少争议。', 'https://picsum.photos/seed/academy-1003/900/540', '报价原则：先明确基础服务，再说明增项条件。沟通模板：先说已确认的问题，再说为什么会有增项，最后给出价格和用户确认动作。', '呼之应学院', 30, TRUE, TRUE, TIMESTAMP '2026-04-08 11:00:00', TIMESTAMP '2026-04-08 11:00:00');

DELETE FROM community_comments WHERE post_id IN (2001, 2002);
DELETE FROM community_reports WHERE id IN (3001);
DELETE FROM community_posts WHERE id IN (2001, 2002);

INSERT INTO community_posts (id, user_id, city_name, title, content_text, images_text, like_count, comment_count, status_code, created_at, updated_at) VALUES
  (2001, 1, '上海', '老小区空调外机噪音大怎么处理？', '最近楼上外机噪音比较大，物业建议先检查支架和减震垫。有遇到类似问题的师傅或者用户吗？', 'https://picsum.photos/seed/community-2001/640/420', 12, 2, 'PUBLISHED', TIMESTAMP '2026-04-08 08:20:00', TIMESTAMP '2026-04-08 08:20:00'),
  (2002, 1, '上海', '智能锁换电池后还提示低电量', '家里智能锁换了新电池还是提示低电量，怀疑是电池仓接触不稳，有没有排查建议？', 'https://picsum.photos/seed/community-2002/640/420', 8, 1, 'PUBLISHED', TIMESTAMP '2026-04-08 12:10:00', TIMESTAMP '2026-04-08 12:10:00');

INSERT INTO community_reports (id, post_id, reporter_user_id, reason_text, status_code, created_at, handled_at) VALUES
  (3001, 2002, 1, '标题与问题描述不完全匹配', 'PENDING', TIMESTAMP '2026-04-08 13:10:00', NULL);

INSERT INTO community_comments (id, post_id, user_id, content_text, created_at) VALUES
  (5001, 2001, 90001, '先检查外机支架是否松动，再看铜管和墙体有没有共振。', TIMESTAMP '2026-04-08 08:35:00'),
  (5002, 2001, 1, '收到，今晚先让物业帮忙看支架。', TIMESTAMP '2026-04-08 08:42:00'),
  (5003, 2002, 90001, '可以先用酒精棉清理电池触点，再重新装电池试一下。', TIMESTAMP '2026-04-08 12:30:00');
