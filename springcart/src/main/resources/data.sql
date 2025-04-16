-- ユーザー（3件）
INSERT IGNORE INTO user (id, name, email, password, role_id) VALUES
(1, '山田 太郎', 'taro@example.com', 'password', 1),
(2, '佐藤 花子', 'hanako@example.com', 'password',1),
(3, '田中 一郎', 'ichiro@example.com', 'password', 2);

-- カート（3件）
INSERT IGNORE INTO cart (id, user_id, product_id, quantity) VALUES
(1, 1, 1, 2), -- 山田太郎がTシャツ2枚
(2, 2, 3, 1), -- 佐藤花子がマグカップ1個
(3, 3, 4, 3); -- 田中一郎がイヤホン3個

-- 注文（2件）
INSERT IGNORE INTO orders (id, user_id, total_price, ordered_at) VALUES
(1, 1, 3960, '2024-04-10 10:00:00'), -- 山田太郎
(2, 2, 750, '2024-04-11 15:30:00');  -- 佐藤花子

-- 注文明細（4件）
INSERT IGNORE INTO order_detail (id, order_id, product_id, quantity, price) VALUES
(1, 1, 1, 2, 1980), -- Tシャツ2枚
(2, 2, 3, 1, 750);   -- マグカップ1個