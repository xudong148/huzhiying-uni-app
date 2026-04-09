CREATE TABLE IF NOT EXISTS payment_records (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  biz_no VARCHAR(64) NOT NULL,
  order_id VARCHAR(64) NOT NULL,
  order_type VARCHAR(32) NOT NULL,
  status VARCHAR(32) NOT NULL,
  channel VARCHAR(32) NOT NULL,
  amount DECIMAL(12,2),
  user_id BIGINT,
  master_id BIGINT,
  operator_id BIGINT,
  trace_id VARCHAR(64),
  external_transaction_no VARCHAR(128),
  payment_stage VARCHAR(32),
  remark_text VARCHAR(255),
  created_at TIMESTAMP,
  updated_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS refund_requests (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  biz_no VARCHAR(64) NOT NULL,
  order_id VARCHAR(64) NOT NULL,
  order_type VARCHAR(32) NOT NULL,
  payment_record_id BIGINT,
  status VARCHAR(32) NOT NULL,
  channel VARCHAR(32) NOT NULL,
  amount DECIMAL(12,2),
  user_id BIGINT,
  master_id BIGINT,
  operator_id BIGINT,
  trace_id VARCHAR(64),
  reason_text VARCHAR(255),
  created_at TIMESTAMP,
  updated_at TIMESTAMP,
  approved_at TIMESTAMP,
  completed_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS settlement_bills (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  biz_no VARCHAR(64) NOT NULL,
  order_id VARCHAR(64),
  order_type VARCHAR(32) NOT NULL,
  status VARCHAR(32) NOT NULL,
  channel VARCHAR(32) NOT NULL,
  amount DECIMAL(12,2),
  user_id BIGINT,
  master_id BIGINT,
  operator_id BIGINT,
  trace_id VARCHAR(64),
  wallet_account_id BIGINT,
  remark_text VARCHAR(255),
  created_at TIMESTAMP,
  updated_at TIMESTAMP,
  approved_at TIMESTAMP,
  settled_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS wallet_ledgers (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  biz_no VARCHAR(64) NOT NULL,
  wallet_account_id BIGINT NOT NULL,
  settlement_bill_id BIGINT,
  refund_request_id BIGINT,
  order_id VARCHAR(64),
  order_type VARCHAR(32) NOT NULL,
  status VARCHAR(32) NOT NULL,
  direction_code VARCHAR(16) NOT NULL,
  channel VARCHAR(32) NOT NULL,
  amount DECIMAL(12,2),
  user_id BIGINT,
  master_id BIGINT,
  operator_id BIGINT,
  trace_id VARCHAR(64),
  title VARCHAR(255),
  created_at TIMESTAMP,
  updated_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS audit_logs (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  biz_no VARCHAR(64) NOT NULL,
  biz_type VARCHAR(64) NOT NULL,
  biz_id VARCHAR(64) NOT NULL,
  action_code VARCHAR(64) NOT NULL,
  status_code VARCHAR(32),
  operator_role VARCHAR(32),
  operator_id BIGINT,
  user_id BIGINT,
  master_id BIGINT,
  trace_id VARCHAR(64),
  detail_text TEXT,
  created_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS notification_tasks (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  biz_no VARCHAR(64) NOT NULL,
  biz_type VARCHAR(64) NOT NULL,
  biz_id VARCHAR(64) NOT NULL,
  target_role VARCHAR(32),
  target_user_id BIGINT,
  channel VARCHAR(32) NOT NULL,
  template_code VARCHAR(64),
  status VARCHAR(32) NOT NULL,
  payload_text TEXT,
  trace_id VARCHAR(64),
  next_retry_at TIMESTAMP,
  sent_at TIMESTAMP,
  created_at TIMESTAMP,
  updated_at TIMESTAMP
);

CREATE UNIQUE INDEX idx_payment_records_biz_no ON payment_records (biz_no);
CREATE INDEX idx_payment_records_order ON payment_records (order_id, status, updated_at);
CREATE INDEX idx_payment_records_external_no ON payment_records (external_transaction_no);

CREATE UNIQUE INDEX idx_refund_requests_biz_no ON refund_requests (biz_no);
CREATE INDEX idx_refund_requests_order ON refund_requests (order_id, status, created_at);

CREATE UNIQUE INDEX idx_settlement_bills_biz_no ON settlement_bills (biz_no);
CREATE INDEX idx_settlement_bills_order ON settlement_bills (order_id, status);
CREATE INDEX idx_settlement_bills_master ON settlement_bills (master_id, status, created_at);

CREATE UNIQUE INDEX idx_wallet_ledgers_biz_no ON wallet_ledgers (biz_no);
CREATE INDEX idx_wallet_ledgers_wallet ON wallet_ledgers (wallet_account_id, created_at);
CREATE INDEX idx_wallet_ledgers_settlement ON wallet_ledgers (settlement_bill_id);
CREATE INDEX idx_wallet_ledgers_refund ON wallet_ledgers (refund_request_id);

CREATE UNIQUE INDEX idx_audit_logs_biz_no ON audit_logs (biz_no);
CREATE INDEX idx_audit_logs_biz ON audit_logs (biz_type, biz_id, created_at);

CREATE UNIQUE INDEX idx_notification_tasks_biz_no ON notification_tasks (biz_no);
CREATE INDEX idx_notification_tasks_status ON notification_tasks (status, next_retry_at);

INSERT INTO settlement_bills (
  biz_no,
  order_id,
  order_type,
  status,
  channel,
  amount,
  user_id,
  master_id,
  operator_id,
  trace_id,
  wallet_account_id,
  remark_text,
  created_at,
  updated_at,
  approved_at,
  settled_at
)
SELECT
  CONCAT('STL-SEED-', wt.id),
  CASE
    WHEN LOCATE(' ', wt.title) > 0 THEN SUBSTRING(wt.title, 1, LOCATE(' ', wt.title) - 1)
    ELSE wt.title
  END,
  'SERVICE',
  CASE
    WHEN wt.status_text IS NULL OR wt.status_text = '' OR wt.status_text = '待结算' THEN 'PENDING_REVIEW'
    WHEN wt.status_text = '已结算' THEN 'APPROVED'
    ELSE 'APPROVED'
  END,
  'PLATFORM',
  wt.amount,
  NULL,
  wa.master_user_id,
  NULL,
  CONCAT('seed-settlement-', wt.id),
  wt.wallet_account_id,
  wt.title,
  CURRENT_TIMESTAMP,
  CURRENT_TIMESTAMP,
  CASE WHEN wt.status_text = '已结算' THEN CURRENT_TIMESTAMP ELSE NULL END,
  CASE WHEN wt.status_text = '已结算' THEN CURRENT_TIMESTAMP ELSE NULL END
FROM wallet_transactions wt
JOIN wallet_accounts wa ON wa.id = wt.wallet_account_id
WHERE wt.amount >= 0 AND wt.title LIKE 'SO%';

INSERT INTO wallet_ledgers (
  biz_no,
  wallet_account_id,
  settlement_bill_id,
  order_id,
  order_type,
  status,
  direction_code,
  channel,
  amount,
  user_id,
  master_id,
  operator_id,
  trace_id,
  title,
  created_at,
  updated_at
)
SELECT
  CONCAT('LEDGER-SEED-', sb.id),
  sb.wallet_account_id,
  sb.id,
  sb.order_id,
  sb.order_type,
  CASE
    WHEN sb.status = 'APPROVED' THEN 'POSTED'
    WHEN sb.status = 'REVERSED' THEN 'REVERSED'
    ELSE 'PENDING_REVIEW'
  END,
  'IN',
  sb.channel,
  sb.amount,
  sb.user_id,
  sb.master_id,
  sb.operator_id,
  sb.trace_id,
  sb.remark_text,
  sb.created_at,
  sb.updated_at
FROM settlement_bills sb;

INSERT INTO refund_requests (
  biz_no,
  order_id,
  order_type,
  payment_record_id,
  status,
  channel,
  amount,
  user_id,
  master_id,
  operator_id,
  trace_id,
  reason_text,
  created_at,
  updated_at,
  approved_at,
  completed_at
)
SELECT
  CONCAT('RFD-SEED-', so.id),
  so.id,
  'SERVICE',
  NULL,
  'PENDING_REVIEW',
  'WECHAT',
  so.amount,
  so.user_id,
  so.master_user_id,
  NULL,
  CONCAT('seed-refund-', so.id),
  'seed migrated refund request',
  CURRENT_TIMESTAMP,
  CURRENT_TIMESTAMP,
  NULL,
  NULL
FROM service_orders so
WHERE so.payment_status = 'REFUNDING' OR so.status = 'REFUNDING';

INSERT INTO refund_requests (
  biz_no,
  order_id,
  order_type,
  payment_record_id,
  status,
  channel,
  amount,
  user_id,
  master_id,
  operator_id,
  trace_id,
  reason_text,
  created_at,
  updated_at,
  approved_at,
  completed_at
)
SELECT
  CONCAT('RFD-SEED-', po.id),
  po.id,
  'PRODUCT',
  NULL,
  'PENDING_REVIEW',
  'WECHAT',
  po.amount,
  po.user_id,
  NULL,
  NULL,
  CONCAT('seed-refund-', po.id),
  'seed migrated refund request',
  CURRENT_TIMESTAMP,
  CURRENT_TIMESTAMP,
  NULL,
  NULL
FROM product_orders po
WHERE po.payment_status = 'REFUNDING' OR po.status = 'REFUNDING';
