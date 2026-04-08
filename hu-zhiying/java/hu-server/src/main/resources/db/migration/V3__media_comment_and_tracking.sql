CREATE TABLE media_files (
  id BIGINT PRIMARY KEY,
  biz_type VARCHAR(64),
  biz_id VARCHAR(64),
  original_name VARCHAR(255) NOT NULL,
  content_type VARCHAR(128) NOT NULL,
  size_bytes BIGINT NOT NULL,
  storage_path VARCHAR(512) NOT NULL,
  access_url VARCHAR(255) NOT NULL,
  created_at TIMESTAMP NOT NULL
);

CREATE TABLE comments (
  id BIGINT PRIMARY KEY,
  service_item_id BIGINT NOT NULL,
  user_name VARCHAR(64) NOT NULL,
  score INT NOT NULL,
  content_text VARCHAR(255) NOT NULL,
  images_text TEXT,
  tags_text TEXT,
  created_at TIMESTAMP NOT NULL
);

CREATE TABLE order_track_points (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  order_id VARCHAR(64) NOT NULL,
  point_type VARCHAR(64) NOT NULL,
  label_text VARCHAR(64) NOT NULL,
  description_text VARCHAR(255) NOT NULL,
  latitude DOUBLE,
  longitude DOUBLE,
  created_at TIMESTAMP NOT NULL
);

INSERT INTO comments (id, service_item_id, user_name, score, content_text, images_text, tags_text, created_at) VALUES
  (1, 201, '赵女士', 5, '师傅响应很快，现场报价也很清楚。', '/seed-media/comment-service.svg', '响应快|报价透明', CURRENT_TIMESTAMP),
  (2, 201, '吴先生', 4, '收费透明，现场解决了冷媒不足的问题。', '', '专业可靠|准时上门', CURRENT_TIMESTAMP),
  (3, 301, '林女士', 5, '安装很细致，联网调试一次完成。', '', '安装规范|调试专业', CURRENT_TIMESTAMP);

INSERT INTO order_track_points (order_id, point_type, label_text, description_text, latitude, longitude, created_at) VALUES
  ('SO20260408001', 'CREATED', '订单创建', '用户已提交报修工单', 31.2253, 121.5443, CURRENT_TIMESTAMP),
  ('SO20260408001', 'CLAIM', '师傅接单', '平台已通知张师傅准备出发', 31.2286, 121.5488, CURRENT_TIMESTAMP),
  ('SO20260407009', 'ARRIVED', '到达现场', '师傅已到达并准备开始安装', 31.2231, 121.5412, CURRENT_TIMESTAMP);
