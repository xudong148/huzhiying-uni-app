CREATE TABLE IF NOT EXISTS message_session_reads (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  session_id VARCHAR(64) NOT NULL,
  reader_code VARCHAR(32) NOT NULL,
  last_read_message_id BIGINT,
  read_at TIMESTAMP,
  CONSTRAINT uk_message_session_reader UNIQUE (session_id, reader_code)
);

UPDATE message_sessions
SET participant_user_id = (
  SELECT so.user_id
  FROM service_orders so
  WHERE so.id = message_sessions.order_id
)
WHERE EXISTS (
  SELECT 1
  FROM service_orders so
  WHERE so.id = message_sessions.order_id
    AND (message_sessions.participant_user_id IS NULL OR message_sessions.participant_user_id <> so.user_id)
);
