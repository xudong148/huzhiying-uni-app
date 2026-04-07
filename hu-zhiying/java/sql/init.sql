CREATE DATABASE IF NOT EXISTS huzhiying DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE huzhiying;

CREATE TABLE IF NOT EXISTS demo_bootstrap_marker (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  marker VARCHAR(64) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO demo_bootstrap_marker (marker)
SELECT 'seed-ready'
WHERE NOT EXISTS (
  SELECT 1 FROM demo_bootstrap_marker WHERE marker = 'seed-ready'
);
