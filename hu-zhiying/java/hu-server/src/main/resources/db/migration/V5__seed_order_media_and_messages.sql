INSERT INTO message_sessions (id, order_id, title, participant_user_id) VALUES
  ('MS-002', 'SO20260408001', '空调维修沟通', 90001);

INSERT INTO message_items (session_id, sender_code, message_type, content_text, message_time) VALUES
  ('MS-002', 'system', 'system', '平台已受理报修请求，正在为您同步服务进度。', '13:48'),
  ('MS-002', 'customer_service', 'text', '您好，已为您锁定今天 14:00-16:00 的服务时段，有新进展会第一时间通知。', '13:50'),
  ('MS-002', 'user', 'text', '好的，师傅到楼下前麻烦提前联系我。', '13:52');

INSERT INTO media_files (id, biz_type, biz_id, original_name, content_type, size_bytes, storage_path, access_url, created_at) VALUES
  (9001, 'order_evidence', 'SO20260408001', '空调故障照片.svg', 'image/svg+xml', 12288, '/seed-media/service-aircon.svg', '/seed-media/service-aircon.svg', CURRENT_TIMESTAMP),
  (9002, 'before_work_media', 'SO20260408001', '到场检测记录.svg', 'image/svg+xml', 12288, '/seed-media/service-aircon.svg', '/seed-media/service-aircon.svg', CURRENT_TIMESTAMP),
  (9003, 'before_work_media', 'SO20260407009', '门锁安装前.svg', 'image/svg+xml', 12288, '/seed-media/service-lock.svg', '/seed-media/service-lock.svg', CURRENT_TIMESTAMP),
  (9004, 'after_work_media', 'SO20260407009', '门锁安装完成.svg', 'image/svg+xml', 12288, '/seed-media/product-lock.svg', '/seed-media/product-lock.svg', CURRENT_TIMESTAMP);
