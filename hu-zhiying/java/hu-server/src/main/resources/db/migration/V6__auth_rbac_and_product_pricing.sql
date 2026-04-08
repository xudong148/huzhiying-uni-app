ALTER TABLE users ADD COLUMN IF NOT EXISTS username VARCHAR(64);
ALTER TABLE users ADD COLUMN IF NOT EXISTS password_hash VARCHAR(255);
ALTER TABLE users ADD COLUMN IF NOT EXISTS enabled BOOLEAN DEFAULT TRUE;

ALTER TABLE products ADD COLUMN IF NOT EXISTS tag_price DECIMAL(12,2);
ALTER TABLE products ADD COLUMN IF NOT EXISTS discount_price DECIMAL(12,2);
ALTER TABLE skus ADD COLUMN IF NOT EXISTS tag_price DECIMAL(12,2);
ALTER TABLE skus ADD COLUMN IF NOT EXISTS discount_price DECIMAL(12,2);

CREATE TABLE IF NOT EXISTS user_roles (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  role_id BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS role_menus (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  role_id BIGINT NOT NULL,
  menu_id BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS role_permissions (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  role_id BIGINT NOT NULL,
  permission_id BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS auth_sessions (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  role_code VARCHAR(32) NOT NULL,
  access_token_hash VARCHAR(128) NOT NULL,
  access_token_preview VARCHAR(32),
  refresh_token_hash VARCHAR(128) NOT NULL,
  client_type VARCHAR(32) NOT NULL,
  access_expires_at TIMESTAMP NOT NULL,
  refresh_expires_at TIMESTAMP NOT NULL,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL,
  last_seen_at TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS auth_sms_codes (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  mobile VARCHAR(32) NOT NULL,
  purpose VARCHAR(32) NOT NULL,
  code VARCHAR(16) NOT NULL,
  expires_at TIMESTAMP NOT NULL,
  consumed_at TIMESTAMP,
  created_at TIMESTAMP NOT NULL
);

CREATE UNIQUE INDEX IF NOT EXISTS ux_users_username ON users (username);
CREATE UNIQUE INDEX IF NOT EXISTS ux_user_roles_user_role ON user_roles (user_id, role_id);
CREATE UNIQUE INDEX IF NOT EXISTS ux_role_menus_role_menu ON role_menus (role_id, menu_id);
CREATE UNIQUE INDEX IF NOT EXISTS ux_role_permissions_role_permission ON role_permissions (role_id, permission_id);
CREATE UNIQUE INDEX IF NOT EXISTS ux_auth_sessions_access_hash ON auth_sessions (access_token_hash);
CREATE UNIQUE INDEX IF NOT EXISTS ux_auth_sessions_refresh_hash ON auth_sessions (refresh_token_hash);

UPDATE users
SET enabled = TRUE
WHERE enabled IS NULL;

UPDATE users
SET username = 'admin',
    password_hash = 'pbkdf2$120000$aHp5LWFkbWluLXNhbHQtMA==$NC8vZqrDOvNeH8VjXlSoxBePxM34zfEUFzrZmfte290=',
    enabled = TRUE
WHERE id = 90001;

UPDATE products
SET tag_price = COALESCE(tag_price, CASE id
  WHEN 1001 THEN 1999.00
  WHEN 1002 THEN 699.00
  ELSE price
END),
    discount_price = COALESCE(discount_price, CASE id
      WHEN 1001 THEN 1699.00
      WHEN 1002 THEN 499.00
      ELSE price
    END),
    price = CASE id
      WHEN 1001 THEN 1699.00
      WHEN 1002 THEN 499.00
      ELSE price
    END
WHERE id IN (1001, 1002);

UPDATE skus
SET tag_price = COALESCE(tag_price, CASE id
  WHEN 1 THEN 1999.00
  WHEN 2 THEN 699.00
  ELSE price
END),
    discount_price = COALESCE(discount_price, CASE id
      WHEN 1 THEN 1699.00
      WHEN 2 THEN 499.00
      ELSE price
    END),
    price = CASE id
      WHEN 1 THEN 1699.00
      WHEN 2 THEN 499.00
      ELSE price
    END
WHERE id IN (1, 2);

DELETE FROM user_roles;
DELETE FROM role_menus;
DELETE FROM role_permissions;

INSERT INTO user_roles (user_id, role_id) VALUES
  (90001, 1);

INSERT INTO role_menus (role_id, menu_id) VALUES
  (1, 1),
  (1, 2),
  (1, 3),
  (1, 4),
  (1, 5),
  (1, 6),
  (1, 7),
  (1, 8),
  (1, 9),
  (1, 10),
  (2, 2),
  (2, 3),
  (2, 4),
  (2, 5),
  (3, 3),
  (3, 6);

INSERT INTO role_permissions (role_id, permission_id) VALUES
  (1, 1),
  (1, 2),
  (1, 3),
  (1, 4),
  (2, 1),
  (2, 2),
  (3, 3);
