ALTER TABLE master_profiles ADD COLUMN IF NOT EXISTS enabled BOOLEAN DEFAULT TRUE;
ALTER TABLE wallet_transactions ADD COLUMN IF NOT EXISTS status_text VARCHAR(64);
ALTER TABLE arbitration_cases ADD COLUMN IF NOT EXISTS result_text VARCHAR(255);

UPDATE master_profiles
SET enabled = TRUE
WHERE enabled IS NULL;

UPDATE wallet_transactions
SET status_text = CASE
    WHEN amount >= 0 THEN '待结算'
    ELSE '已冻结'
END
WHERE status_text IS NULL;

UPDATE arbitration_cases
SET result_text = ''
WHERE result_text IS NULL;
