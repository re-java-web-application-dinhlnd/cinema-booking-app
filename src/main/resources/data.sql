-- ============================================
-- CINEWAVE SEED DATA
-- Mật khẩu mặc định: Password123!
-- ============================================

-- 1. USERS
INSERT IGNORE INTO users (username, password, role, status, created_at, updated_at) VALUES
('dinhlunhut', '$2a$10$/u4WbGer6kVHjrLGJvqqlO/7F4kc.0tKg29iMS8LAke2iQ1i6nJ3C', 'ADMIN', 'ACTIVE', NOW(), NOW()),
('staff01', '$2a$10$/u4WbGer6kVHjrLGJvqqlO/7F4kc.0tKg29iMS8LAke2iQ1i6nJ3C', 'STAFF', 'ACTIVE', NOW(), NOW()),
('customer01', '$2a$10$/u4WbGer6kVHjrLGJvqqlO/7F4kc.0tKg29iMS8LAke2iQ1i6nJ3C', 'CUSTOMER', 'ACTIVE', NOW(), NOW()),
('customer02', '$2a$10$/u4WbGer6kVHjrLGJvqqlO/7F4kc.0tKg29iMS8LAke2iQ1i6nJ3C', 'CUSTOMER', 'ACTIVE', NOW(), NOW()),
('customer03', '$2a$10$/u4WbGer6kVHjrLGJvqqlO/7F4kc.0tKg29iMS8LAke2iQ1i6nJ3C', 'CUSTOMER', 'ACTIVE', NOW(), NOW());

INSERT IGNORE INTO user_profiles (user_id, full_name, email, phone_number, created_at, updated_at) VALUES
((SELECT id FROM users WHERE username = 'dinhlunhut'), 'Đinh Lư Nhựt (Admin)', 'admin@cinewave.vn', '0900000001', NOW(), NOW()),
((SELECT id FROM users WHERE username = 'staff01'), 'Nhân Viên Bán Vé 1', 'staff01@cinewave.vn', '0900000002', NOW(), NOW()),
((SELECT id FROM users WHERE username = 'customer01'), 'Khách Hàng Thường', 'customer01@gmail.com', '0900000003', NOW(), NOW()),
((SELECT id FROM users WHERE username = 'customer02'), 'Khách Hàng VIP', 'customer02@gmail.com', '0900000004', NOW(), NOW()),
((SELECT id FROM users WHERE username = 'customer03'), 'Khách Hàng VVIP', 'customer03@gmail.com', '0900000005', NOW(), NOW());

-- 2. GENRES (tmdb_id mapping từ TMDB API)
INSERT IGNORE INTO genres (name, tmdb_id) VALUES
('Hành Động', 28),
('Phiêu Lưu', 12),
('Hoạt Hình', 16),
('Hài', 35),
('Hình Sự', 80),
('Tài Liệu', 99),
('Chính Kịch', 18),
('Gia Đình', 10751),
('Giả Tưởng', 14),
('Lịch Sử', 36),
('Kinh Dị', 27),
('Nhạc', 10402),
('Bí Ẩn', 9648),
('Lãng Mạn', 10749),
('Khoa Học Viễn Tưởng', 878),
('Chiến Tranh', 10752),
('Phim Truyền Hình', 10770),
('Giật Gân', 53),
('Miền Tây', 37);

-- 3. ROOMS (Phòng chiếu - Hardcode)
INSERT IGNORE INTO rooms (name, total_seats, status) VALUES
('Phòng 1', 60, 'ACTIVE'),
('Phòng 2', 80, 'ACTIVE'),
('Phòng 3', 100, 'ACTIVE');

-- 4. SEATS — Phòng 1 (60 ghế: A-F, mỗi hàng 10)
INSERT IGNORE INTO seats (room_id, seat_name, seat_type) VALUES
-- Hàng A-D: STANDARD
((SELECT id FROM rooms WHERE name='Phòng 1'), 'A1', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 1'), 'A2', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 1'), 'A3', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 1'), 'A4', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 1'), 'A5', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 1'), 'A6', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 1'), 'A7', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 1'), 'A8', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 1'), 'A9', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 1'), 'A10', 'STANDARD'),
((SELECT id FROM rooms WHERE name='Phòng 1'), 'B1', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 1'), 'B2', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 1'), 'B3', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 1'), 'B4', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 1'), 'B5', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 1'), 'B6', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 1'), 'B7', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 1'), 'B8', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 1'), 'B9', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 1'), 'B10', 'STANDARD'),
((SELECT id FROM rooms WHERE name='Phòng 1'), 'C1', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 1'), 'C2', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 1'), 'C3', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 1'), 'C4', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 1'), 'C5', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 1'), 'C6', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 1'), 'C7', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 1'), 'C8', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 1'), 'C9', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 1'), 'C10', 'STANDARD'),
((SELECT id FROM rooms WHERE name='Phòng 1'), 'D1', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 1'), 'D2', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 1'), 'D3', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 1'), 'D4', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 1'), 'D5', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 1'), 'D6', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 1'), 'D7', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 1'), 'D8', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 1'), 'D9', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 1'), 'D10', 'STANDARD'),
-- Hàng E-F: VIP
((SELECT id FROM rooms WHERE name='Phòng 1'), 'E1', 'VIP'), ((SELECT id FROM rooms WHERE name='Phòng 1'), 'E2', 'VIP'), ((SELECT id FROM rooms WHERE name='Phòng 1'), 'E3', 'VIP'), ((SELECT id FROM rooms WHERE name='Phòng 1'), 'E4', 'VIP'), ((SELECT id FROM rooms WHERE name='Phòng 1'), 'E5', 'VIP'), ((SELECT id FROM rooms WHERE name='Phòng 1'), 'E6', 'VIP'), ((SELECT id FROM rooms WHERE name='Phòng 1'), 'E7', 'VIP'), ((SELECT id FROM rooms WHERE name='Phòng 1'), 'E8', 'VIP'), ((SELECT id FROM rooms WHERE name='Phòng 1'), 'E9', 'VIP'), ((SELECT id FROM rooms WHERE name='Phòng 1'), 'E10', 'VIP'),
((SELECT id FROM rooms WHERE name='Phòng 1'), 'F1', 'VIP'), ((SELECT id FROM rooms WHERE name='Phòng 1'), 'F2', 'VIP'), ((SELECT id FROM rooms WHERE name='Phòng 1'), 'F3', 'VIP'), ((SELECT id FROM rooms WHERE name='Phòng 1'), 'F4', 'VIP'), ((SELECT id FROM rooms WHERE name='Phòng 1'), 'F5', 'VIP'), ((SELECT id FROM rooms WHERE name='Phòng 1'), 'F6', 'VIP'), ((SELECT id FROM rooms WHERE name='Phòng 1'), 'F7', 'VIP'), ((SELECT id FROM rooms WHERE name='Phòng 1'), 'F8', 'VIP'), ((SELECT id FROM rooms WHERE name='Phòng 1'), 'F9', 'VIP'), ((SELECT id FROM rooms WHERE name='Phòng 1'), 'F10', 'VIP');

-- SEATS — Phòng 2 (80 ghế: A-H, mỗi hàng 10)
INSERT IGNORE INTO seats (room_id, seat_name, seat_type) VALUES
-- Hàng A-E: STANDARD
((SELECT id FROM rooms WHERE name='Phòng 2'), 'A1', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 2'), 'A2', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 2'), 'A3', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 2'), 'A4', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 2'), 'A5', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 2'), 'A6', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 2'), 'A7', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 2'), 'A8', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 2'), 'A9', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 2'), 'A10', 'STANDARD'),
((SELECT id FROM rooms WHERE name='Phòng 2'), 'B1', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 2'), 'B2', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 2'), 'B3', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 2'), 'B4', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 2'), 'B5', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 2'), 'B6', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 2'), 'B7', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 2'), 'B8', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 2'), 'B9', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 2'), 'B10', 'STANDARD'),
((SELECT id FROM rooms WHERE name='Phòng 2'), 'C1', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 2'), 'C2', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 2'), 'C3', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 2'), 'C4', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 2'), 'C5', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 2'), 'C6', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 2'), 'C7', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 2'), 'C8', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 2'), 'C9', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 2'), 'C10', 'STANDARD'),
((SELECT id FROM rooms WHERE name='Phòng 2'), 'D1', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 2'), 'D2', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 2'), 'D3', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 2'), 'D4', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 2'), 'D5', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 2'), 'D6', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 2'), 'D7', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 2'), 'D8', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 2'), 'D9', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 2'), 'D10', 'STANDARD'),
((SELECT id FROM rooms WHERE name='Phòng 2'), 'E1', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 2'), 'E2', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 2'), 'E3', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 2'), 'E4', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 2'), 'E5', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 2'), 'E6', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 2'), 'E7', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 2'), 'E8', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 2'), 'E9', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 2'), 'E10', 'STANDARD'),
-- Hàng F-H: VIP
((SELECT id FROM rooms WHERE name='Phòng 2'), 'F1', 'VIP'), ((SELECT id FROM rooms WHERE name='Phòng 2'), 'F2', 'VIP'), ((SELECT id FROM rooms WHERE name='Phòng 2'), 'F3', 'VIP'), ((SELECT id FROM rooms WHERE name='Phòng 2'), 'F4', 'VIP'), ((SELECT id FROM rooms WHERE name='Phòng 2'), 'F5', 'VIP'), ((SELECT id FROM rooms WHERE name='Phòng 2'), 'F6', 'VIP'), ((SELECT id FROM rooms WHERE name='Phòng 2'), 'F7', 'VIP'), ((SELECT id FROM rooms WHERE name='Phòng 2'), 'F8', 'VIP'), ((SELECT id FROM rooms WHERE name='Phòng 2'), 'F9', 'VIP'), ((SELECT id FROM rooms WHERE name='Phòng 2'), 'F10', 'VIP'),
((SELECT id FROM rooms WHERE name='Phòng 2'), 'G1', 'VIP'), ((SELECT id FROM rooms WHERE name='Phòng 2'), 'G2', 'VIP'), ((SELECT id FROM rooms WHERE name='Phòng 2'), 'G3', 'VIP'), ((SELECT id FROM rooms WHERE name='Phòng 2'), 'G4', 'VIP'), ((SELECT id FROM rooms WHERE name='Phòng 2'), 'G5', 'VIP'), ((SELECT id FROM rooms WHERE name='Phòng 2'), 'G6', 'VIP'), ((SELECT id FROM rooms WHERE name='Phòng 2'), 'G7', 'VIP'), ((SELECT id FROM rooms WHERE name='Phòng 2'), 'G8', 'VIP'), ((SELECT id FROM rooms WHERE name='Phòng 2'), 'G9', 'VIP'), ((SELECT id FROM rooms WHERE name='Phòng 2'), 'G10', 'VIP'),
((SELECT id FROM rooms WHERE name='Phòng 2'), 'H1', 'VIP'), ((SELECT id FROM rooms WHERE name='Phòng 2'), 'H2', 'VIP'), ((SELECT id FROM rooms WHERE name='Phòng 2'), 'H3', 'VIP'), ((SELECT id FROM rooms WHERE name='Phòng 2'), 'H4', 'VIP'), ((SELECT id FROM rooms WHERE name='Phòng 2'), 'H5', 'VIP'), ((SELECT id FROM rooms WHERE name='Phòng 2'), 'H6', 'VIP'), ((SELECT id FROM rooms WHERE name='Phòng 2'), 'H7', 'VIP'), ((SELECT id FROM rooms WHERE name='Phòng 2'), 'H8', 'VIP'), ((SELECT id FROM rooms WHERE name='Phòng 2'), 'H9', 'VIP'), ((SELECT id FROM rooms WHERE name='Phòng 2'), 'H10', 'VIP');

-- SEATS — Phòng 3 (100 ghế: A-J, mỗi hàng 10)
INSERT IGNORE INTO seats (room_id, seat_name, seat_type) VALUES
-- Hàng A-F: STANDARD
((SELECT id FROM rooms WHERE name='Phòng 3'), 'A1', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 3'), 'A2', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 3'), 'A3', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 3'), 'A4', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 3'), 'A5', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 3'), 'A6', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 3'), 'A7', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 3'), 'A8', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 3'), 'A9', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 3'), 'A10', 'STANDARD'),
((SELECT id FROM rooms WHERE name='Phòng 3'), 'B1', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 3'), 'B2', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 3'), 'B3', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 3'), 'B4', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 3'), 'B5', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 3'), 'B6', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 3'), 'B7', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 3'), 'B8', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 3'), 'B9', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 3'), 'B10', 'STANDARD'),
((SELECT id FROM rooms WHERE name='Phòng 3'), 'C1', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 3'), 'C2', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 3'), 'C3', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 3'), 'C4', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 3'), 'C5', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 3'), 'C6', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 3'), 'C7', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 3'), 'C8', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 3'), 'C9', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 3'), 'C10', 'STANDARD'),
((SELECT id FROM rooms WHERE name='Phòng 3'), 'D1', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 3'), 'D2', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 3'), 'D3', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 3'), 'D4', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 3'), 'D5', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 3'), 'D6', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 3'), 'D7', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 3'), 'D8', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 3'), 'D9', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 3'), 'D10', 'STANDARD'),
((SELECT id FROM rooms WHERE name='Phòng 3'), 'E1', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 3'), 'E2', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 3'), 'E3', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 3'), 'E4', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 3'), 'E5', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 3'), 'E6', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 3'), 'E7', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 3'), 'E8', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 3'), 'E9', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 3'), 'E10', 'STANDARD'),
((SELECT id FROM rooms WHERE name='Phòng 3'), 'F1', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 3'), 'F2', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 3'), 'F3', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 3'), 'F4', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 3'), 'F5', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 3'), 'F6', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 3'), 'F7', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 3'), 'F8', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 3'), 'F9', 'STANDARD'), ((SELECT id FROM rooms WHERE name='Phòng 3'), 'F10', 'STANDARD'),
-- Hàng G-H: VIP
((SELECT id FROM rooms WHERE name='Phòng 3'), 'G1', 'VIP'), ((SELECT id FROM rooms WHERE name='Phòng 3'), 'G2', 'VIP'), ((SELECT id FROM rooms WHERE name='Phòng 3'), 'G3', 'VIP'), ((SELECT id FROM rooms WHERE name='Phòng 3'), 'G4', 'VIP'), ((SELECT id FROM rooms WHERE name='Phòng 3'), 'G5', 'VIP'), ((SELECT id FROM rooms WHERE name='Phòng 3'), 'G6', 'VIP'), ((SELECT id FROM rooms WHERE name='Phòng 3'), 'G7', 'VIP'), ((SELECT id FROM rooms WHERE name='Phòng 3'), 'G8', 'VIP'), ((SELECT id FROM rooms WHERE name='Phòng 3'), 'G9', 'VIP'), ((SELECT id FROM rooms WHERE name='Phòng 3'), 'G10', 'VIP'),
((SELECT id FROM rooms WHERE name='Phòng 3'), 'H1', 'VIP'), ((SELECT id FROM rooms WHERE name='Phòng 3'), 'H2', 'VIP'), ((SELECT id FROM rooms WHERE name='Phòng 3'), 'H3', 'VIP'), ((SELECT id FROM rooms WHERE name='Phòng 3'), 'H4', 'VIP'), ((SELECT id FROM rooms WHERE name='Phòng 3'), 'H5', 'VIP'), ((SELECT id FROM rooms WHERE name='Phòng 3'), 'H6', 'VIP'), ((SELECT id FROM rooms WHERE name='Phòng 3'), 'H7', 'VIP'), ((SELECT id FROM rooms WHERE name='Phòng 3'), 'H8', 'VIP'), ((SELECT id FROM rooms WHERE name='Phòng 3'), 'H9', 'VIP'), ((SELECT id FROM rooms WHERE name='Phòng 3'), 'H10', 'VIP'),
-- Hàng I-J: SWEETBOX
((SELECT id FROM rooms WHERE name='Phòng 3'), 'I1', 'SWEETBOX'), ((SELECT id FROM rooms WHERE name='Phòng 3'), 'I2', 'SWEETBOX'), ((SELECT id FROM rooms WHERE name='Phòng 3'), 'I3', 'SWEETBOX'), ((SELECT id FROM rooms WHERE name='Phòng 3'), 'I4', 'SWEETBOX'), ((SELECT id FROM rooms WHERE name='Phòng 3'), 'I5', 'SWEETBOX'), ((SELECT id FROM rooms WHERE name='Phòng 3'), 'I6', 'SWEETBOX'), ((SELECT id FROM rooms WHERE name='Phòng 3'), 'I7', 'SWEETBOX'), ((SELECT id FROM rooms WHERE name='Phòng 3'), 'I8', 'SWEETBOX'), ((SELECT id FROM rooms WHERE name='Phòng 3'), 'I9', 'SWEETBOX'), ((SELECT id FROM rooms WHERE name='Phòng 3'), 'I10', 'SWEETBOX'),
((SELECT id FROM rooms WHERE name='Phòng 3'), 'J1', 'SWEETBOX'), ((SELECT id FROM rooms WHERE name='Phòng 3'), 'J2', 'SWEETBOX'), ((SELECT id FROM rooms WHERE name='Phòng 3'), 'J3', 'SWEETBOX'), ((SELECT id FROM rooms WHERE name='Phòng 3'), 'J4', 'SWEETBOX'), ((SELECT id FROM rooms WHERE name='Phòng 3'), 'J5', 'SWEETBOX'), ((SELECT id FROM rooms WHERE name='Phòng 3'), 'J6', 'SWEETBOX'), ((SELECT id FROM rooms WHERE name='Phòng 3'), 'J7', 'SWEETBOX'), ((SELECT id FROM rooms WHERE name='Phòng 3'), 'J8', 'SWEETBOX'), ((SELECT id FROM rooms WHERE name='Phòng 3'), 'J9', 'SWEETBOX'), ((SELECT id FROM rooms WHERE name='Phòng 3'), 'J10', 'SWEETBOX');

-- 5. PRODUCTS (Combo bắp nước)
INSERT IGNORE INTO products (name, description, price, type, status) VALUES
('Bắp Rang Bơ (Nhỏ)', 'Bắp rang bơ cỡ nhỏ 32oz', 35000, 'SINGLE', 'ACTIVE'),
('Bắp Rang Bơ (Lớn)', 'Bắp rang bơ cỡ lớn 64oz', 49000, 'SINGLE', 'ACTIVE'),
('Coca-Cola', 'Coca-Cola lạnh 22oz', 25000, 'SINGLE', 'ACTIVE'),
('Pepsi', 'Pepsi lạnh 22oz', 25000, 'SINGLE', 'ACTIVE'),
('Nước Suối', 'Nước khoáng Aquafina 500ml', 15000, 'SINGLE', 'ACTIVE'),
('Combo Solo', '1 Bắp nhỏ + 1 Coca-Cola', 55000, 'COMBO', 'ACTIVE'),
('Combo Đôi', '1 Bắp lớn + 2 Coca-Cola', 89000, 'COMBO', 'ACTIVE'),
('Combo Gia Đình', '2 Bắp lớn + 4 Coca-Cola', 159000, 'COMBO', 'ACTIVE');

-- 6. MOVIES (Tất cả poster/backdrop đã được verify HTTP 200)
INSERT IGNORE INTO movies (tmdb_id, title, description, duration_minutes, release_date, poster_url, backdrop_url, trailer_url, vote_average, status, created_at, updated_at) VALUES
(299534, 'Avengers: Endgame', 'Avengers tập hợp lại để đảo ngược hành động của Thanos và khôi phục trật tự vũ trụ.', 181, '2019-04-24', 'https://image.tmdb.org/t/p/w500/or06FN3Dka5tukK1e9sl16pB3iy.jpg', 'https://image.tmdb.org/t/p/w1280/7RyHsO4yDXtBv1zUU3mTpHeQ0d5.jpg', 'https://www.youtube.com/watch?v=TcMBFSGVi1c', 8.3, 'ACTIVE', NOW(), NOW()),
(27205, 'Inception', 'Một tên trộm lành nghề chuyên đánh cắp bí mật qua giấc mơ được giao nhiệm vụ gieo rắc ý tưởng.', 148, '2010-07-15', 'https://image.tmdb.org/t/p/w500/oYuLEt3zVCKq57qu2F8dT7NIa6f.jpg', 'https://image.tmdb.org/t/p/w1280/8ZTVqvKDQ8emSGUEMjsS4yHAwrp.jpg', 'https://www.youtube.com/watch?v=YoHD9XEInc0', 8.4, 'ACTIVE', NOW(), NOW()),
(634649, 'Spider-Man: No Way Home', 'Peter Parker đối mặt với kẻ thù từ đa vũ trụ khi danh tính bị lộ.', 148, '2021-12-15', 'https://image.tmdb.org/t/p/w500/1g0dhYtq4irTY1GPXvft6k4YLjm.jpg', 'https://image.tmdb.org/t/p/w1280/14QbnygCuTO0vl7CAFmPf1fgZfV.jpg', 'https://www.youtube.com/watch?v=JfVOs4VSpmA', 8.0, 'ACTIVE', NOW(), NOW()),
(155, 'The Dark Knight', 'Batman đối đầu Joker trong cuộc chiến bảo vệ Gotham City.', 152, '2008-07-16', 'https://image.tmdb.org/t/p/w500/qJ2tW6WMUDux911r6m7haRef0WH.jpg', 'https://image.tmdb.org/t/p/w1280/nMKdUUepR0i5zn0y1T4CsSB5ez.jpg', 'https://www.youtube.com/watch?v=EXeTwQWrcwY', 8.5, 'ACTIVE', NOW(), NOW()),
(693134, 'Dune: Part Two', 'Paul Atreides hợp nhất với người Fremen trả thù và đối mặt số phận vũ trụ.', 166, '2024-02-27', 'https://image.tmdb.org/t/p/w500/czembW0Rk1Ke7lCJGahbOhdCuhV.jpg', 'https://image.tmdb.org/t/p/w1280/xOMo8BRK7PfcJv9JCnx7s5hj0PX.jpg', 'https://www.youtube.com/watch?v=Way9Dexny3w', 8.2, 'ACTIVE', NOW(), NOW()),
(872585, 'Oppenheimer', 'Câu chuyện về nhà vật lý phát triển bom nguyên tử và hậu quả thay đổi thế giới.', 180, '2023-07-19', 'https://image.tmdb.org/t/p/w500/8Gxv8gSFCU0XGDykEGv7zR1n2ua.jpg', 'https://image.tmdb.org/t/p/w1280/fm6KqXpk3M2HVveHwCrBSSBaO0V.jpg', 'https://www.youtube.com/watch?v=uYPbbksJxIg', 8.1, 'ACTIVE', NOW(), NOW()),
(1022789, 'Inside Out 2', 'Riley đối mặt cảm xúc mới tuổi dậy thì: Lo Âu, Ghen Tị, Chán Nản.', 100, '2024-06-11', 'https://image.tmdb.org/t/p/w500/vpnVM9B6NMmQpWeZvzLvDESb2QY.jpg', 'https://image.tmdb.org/t/p/w1280/xRd1eJIDe7JHO5u4gtEYwGn5wtf.jpg', 'https://www.youtube.com/watch?v=LEjhY15eCx0', 7.6, 'ACTIVE', NOW(), NOW()),
(533535, 'Deadpool & Wolverine', 'Deadpool kéo Wolverine vào sứ mệnh đa vũ trụ đầy hỗn loạn.', 128, '2024-07-24', 'https://image.tmdb.org/t/p/w500/8cdWjvZQUExUUTzyp4t6EDMubfO.jpg', 'https://image.tmdb.org/t/p/w1280/yDHYTfA3R0jFYba16jBB1ef8oIt.jpg', 'https://www.youtube.com/watch?v=73_1biulkYk', 7.7, 'ACTIVE', NOW(), NOW()),
(603692, 'John Wick: Chapter 4', 'John Wick tìm cách đánh bại The High Table để giành tự do.', 169, '2023-03-22', 'https://image.tmdb.org/t/p/w500/vZloFAK7NmvMGKE7VkF5UHaz0I.jpg', 'https://image.tmdb.org/t/p/w1280/kn5GDpEGCJSuLSwrpIODMVzz3MB.jpg', 'https://www.youtube.com/watch?v=qEVUtrk8_B4', 7.7, 'ACTIVE', NOW(), NOW()),
(950387, 'A Minecraft Movie', 'Bốn người bị kéo vào thế giới Minecraft và phải học cách sinh tồn.', 101, '2025-04-02', 'https://image.tmdb.org/t/p/w500/yFHHfHcUgGAxziP1C3lLt0q2T4s.jpg', 'https://image.tmdb.org/t/p/w1280/2Nti3gYAX513wvhp8IiLL3MxGNA.jpg', 'https://www.youtube.com/watch?v=wJO_vIDylog', 6.5, 'ACTIVE', NOW(), NOW()),
(558449, 'Gladiator II', 'Lucius bước vào đấu trường Colosseum chiến đấu giành lại vinh quang La Mã.', 148, '2024-11-13', 'https://image.tmdb.org/t/p/w500/2cxhvwyEwRlysAmRH4iodkvo0z5.jpg', 'https://image.tmdb.org/t/p/w1280/euYIwmwkmz95mnXvufEmbL6ovhZ.jpg', 'https://www.youtube.com/watch?v=4rgYUipGJNo', 6.8, 'ACTIVE', NOW(), NOW()),
(475557, 'Joker', 'Arthur Fleck rơi vào điên loạn và trở thành biểu tượng tội phạm Gotham City.', 122, '2019-10-02', 'https://image.tmdb.org/t/p/w500/udDclJoHjfjb8Ekgsd4FDteOkCU.jpg', 'https://image.tmdb.org/t/p/w1280/f5F4cRhQdUbyVbB5lTNCwUzD6BP.jpg', 'https://www.youtube.com/watch?v=zAGVQLHvwOY', 8.2, 'ACTIVE', NOW(), NOW()),
(76600, 'Avatar: The Way of Water', 'Gia đình Sully phiêu lưu trên đại dương Pandora đối mặt mối đe dọa mới.', 192, '2022-12-14', 'https://image.tmdb.org/t/p/w500/kKgQzkUCnQmeTPkyIwHly2t6ZFI.jpg', 'https://image.tmdb.org/t/p/w1280/s16H6tpK2utvwDtzZ8Qy4qm5Emw.jpg', 'https://www.youtube.com/watch?v=d9MyW72ELq0', 7.6, 'ACTIVE', NOW(), NOW()),
(238, 'The Godfather', 'Người đứng đầu gia đình tội phạm chuyển giao quyền lực cho con trai út.', 175, '1972-03-14', 'https://image.tmdb.org/t/p/w500/3bhkrj58Vtu7enYsRolD1fZdja1.jpg', 'https://image.tmdb.org/t/p/w1280/tmU7GeKVybMWFButWEGl2M4GeiP.jpg', 'https://www.youtube.com/watch?v=sY1S34973zA', 8.7, 'ACTIVE', NOW(), NOW()),
(680, 'Pulp Fiction', 'Bốn câu chuyện bạo lực và cứu chuộc đan xen trong thế giới ngầm Los Angeles.', 154, '1994-09-10', 'https://image.tmdb.org/t/p/w500/d5iIlFn5s0ImszYzBPb8JPIfbXD.jpg', 'https://image.tmdb.org/t/p/w1280/suaEOtk1N1sgg2MTM7oZd2cfVp3.jpg', 'https://www.youtube.com/watch?v=s7EdQ4FqbhY', 8.5, 'ACTIVE', NOW(), NOW()),
(603, 'The Matrix', 'Hacker Neo khám phá thực tại là mô phỏng và gia nhập cuộc nổi dậy.', 136, '1999-03-30', 'https://image.tmdb.org/t/p/w500/f89U3ADr1oiB1s9GkdPOEpXUk5H.jpg', 'https://image.tmdb.org/t/p/w1280/fNG7i7RqMErkcqhohV2a6cV1Ehy.jpg', 'https://www.youtube.com/watch?v=vKQi3bBA1y8', 8.2, 'ACTIVE', NOW(), NOW()),
(122, 'The Lord of the Rings: The Return of the King', 'Frodo và Sam tiến vào Mordor trong khi Aragorn dẫn quân chống Sauron.', 201, '2003-12-01', 'https://image.tmdb.org/t/p/w500/rCzpDGLbOoPwLjy3OAm5NUPOTrC.jpg', 'https://image.tmdb.org/t/p/w1280/lXhgCODAbBXL5buk9yEmTpOoOgR.jpg', 'https://www.youtube.com/watch?v=r5X-hFf6Bwo', 8.5, 'ACTIVE', NOW(), NOW()),
(11, 'Star Wars', 'Luke Skywalker gia nhập lực lượng Jedi để cứu thiên hà khỏi Đế chế.', 121, '1977-05-25', 'https://image.tmdb.org/t/p/w500/6FfCtAuVAW8XJjZ7eWeLibRLWTw.jpg', 'https://image.tmdb.org/t/p/w1280/zqkmTXzjkAgXmEWLRsY4UpTWCeo.jpg', 'https://www.youtube.com/watch?v=vZ734NWnAHA', 8.2, 'ACTIVE', NOW(), NOW()),
(496243, 'Parasite', 'Gia đình Ki-taek len lỏi vào nhà Park giàu có với kết cục kinh hoàng.', 132, '2019-05-30', 'https://image.tmdb.org/t/p/w500/7IiTTgloJzvGI1TAYymCfbfl3vT.jpg', 'https://image.tmdb.org/t/p/w1280/TU9NIjwzjoKPwQHoHshkFcQUCG.jpg', 'https://www.youtube.com/watch?v=5xH0HfJHsaY', 8.5, 'INACTIVE', NOW(), NOW());

-- 7. MOVIE_GENRES
INSERT IGNORE INTO movie_genres (movie_id, genre_id) VALUES
((SELECT id FROM movies WHERE tmdb_id=299534),(SELECT id FROM genres WHERE tmdb_id=28)),
((SELECT id FROM movies WHERE tmdb_id=299534),(SELECT id FROM genres WHERE tmdb_id=12)),
((SELECT id FROM movies WHERE tmdb_id=299534),(SELECT id FROM genres WHERE tmdb_id=878)),
((SELECT id FROM movies WHERE tmdb_id=27205),(SELECT id FROM genres WHERE tmdb_id=28)),
((SELECT id FROM movies WHERE tmdb_id=27205),(SELECT id FROM genres WHERE tmdb_id=878)),
((SELECT id FROM movies WHERE tmdb_id=634649),(SELECT id FROM genres WHERE tmdb_id=28)),
((SELECT id FROM movies WHERE tmdb_id=634649),(SELECT id FROM genres WHERE tmdb_id=12)),
((SELECT id FROM movies WHERE tmdb_id=155),(SELECT id FROM genres WHERE tmdb_id=18)),
((SELECT id FROM movies WHERE tmdb_id=155),(SELECT id FROM genres WHERE tmdb_id=28)),
((SELECT id FROM movies WHERE tmdb_id=155),(SELECT id FROM genres WHERE tmdb_id=80)),
((SELECT id FROM movies WHERE tmdb_id=693134),(SELECT id FROM genres WHERE tmdb_id=878)),
((SELECT id FROM movies WHERE tmdb_id=693134),(SELECT id FROM genres WHERE tmdb_id=12)),
((SELECT id FROM movies WHERE tmdb_id=872585),(SELECT id FROM genres WHERE tmdb_id=18)),
((SELECT id FROM movies WHERE tmdb_id=872585),(SELECT id FROM genres WHERE tmdb_id=36)),
((SELECT id FROM movies WHERE tmdb_id=1022789),(SELECT id FROM genres WHERE tmdb_id=16)),
((SELECT id FROM movies WHERE tmdb_id=1022789),(SELECT id FROM genres WHERE tmdb_id=10751)),
((SELECT id FROM movies WHERE tmdb_id=1022789),(SELECT id FROM genres WHERE tmdb_id=35)),
((SELECT id FROM movies WHERE tmdb_id=533535),(SELECT id FROM genres WHERE tmdb_id=28)),
((SELECT id FROM movies WHERE tmdb_id=533535),(SELECT id FROM genres WHERE tmdb_id=35)),
((SELECT id FROM movies WHERE tmdb_id=533535),(SELECT id FROM genres WHERE tmdb_id=878)),
((SELECT id FROM movies WHERE tmdb_id=603692),(SELECT id FROM genres WHERE tmdb_id=28)),
((SELECT id FROM movies WHERE tmdb_id=603692),(SELECT id FROM genres WHERE tmdb_id=53)),
((SELECT id FROM movies WHERE tmdb_id=950387),(SELECT id FROM genres WHERE tmdb_id=10751)),
((SELECT id FROM movies WHERE tmdb_id=950387),(SELECT id FROM genres WHERE tmdb_id=12)),
((SELECT id FROM movies WHERE tmdb_id=950387),(SELECT id FROM genres WHERE tmdb_id=35)),
((SELECT id FROM movies WHERE tmdb_id=558449),(SELECT id FROM genres WHERE tmdb_id=28)),
((SELECT id FROM movies WHERE tmdb_id=558449),(SELECT id FROM genres WHERE tmdb_id=18)),
((SELECT id FROM movies WHERE tmdb_id=475557),(SELECT id FROM genres WHERE tmdb_id=80)),
((SELECT id FROM movies WHERE tmdb_id=475557),(SELECT id FROM genres WHERE tmdb_id=53)),
((SELECT id FROM movies WHERE tmdb_id=475557),(SELECT id FROM genres WHERE tmdb_id=18)),
((SELECT id FROM movies WHERE tmdb_id=76600),(SELECT id FROM genres WHERE tmdb_id=878)),
((SELECT id FROM movies WHERE tmdb_id=76600),(SELECT id FROM genres WHERE tmdb_id=28)),
((SELECT id FROM movies WHERE tmdb_id=76600),(SELECT id FROM genres WHERE tmdb_id=12)),
((SELECT id FROM movies WHERE tmdb_id=238),(SELECT id FROM genres WHERE tmdb_id=18)),
((SELECT id FROM movies WHERE tmdb_id=238),(SELECT id FROM genres WHERE tmdb_id=80)),
((SELECT id FROM movies WHERE tmdb_id=680),(SELECT id FROM genres WHERE tmdb_id=53)),
((SELECT id FROM movies WHERE tmdb_id=680),(SELECT id FROM genres WHERE tmdb_id=80)),
((SELECT id FROM movies WHERE tmdb_id=603),(SELECT id FROM genres WHERE tmdb_id=28)),
((SELECT id FROM movies WHERE tmdb_id=603),(SELECT id FROM genres WHERE tmdb_id=878)),
((SELECT id FROM movies WHERE tmdb_id=122),(SELECT id FROM genres WHERE tmdb_id=12)),
((SELECT id FROM movies WHERE tmdb_id=122),(SELECT id FROM genres WHERE tmdb_id=14)),
((SELECT id FROM movies WHERE tmdb_id=122),(SELECT id FROM genres WHERE tmdb_id=28)),
((SELECT id FROM movies WHERE tmdb_id=11),(SELECT id FROM genres WHERE tmdb_id=12)),
((SELECT id FROM movies WHERE tmdb_id=11),(SELECT id FROM genres WHERE tmdb_id=28)),
((SELECT id FROM movies WHERE tmdb_id=11),(SELECT id FROM genres WHERE tmdb_id=878)),
((SELECT id FROM movies WHERE tmdb_id=496243),(SELECT id FROM genres WHERE tmdb_id=53)),
((SELECT id FROM movies WHERE tmdb_id=496243),(SELECT id FROM genres WHERE tmdb_id=35)),
((SELECT id FROM movies WHERE tmdb_id=496243),(SELECT id FROM genres WHERE tmdb_id=18));
-- 8. SHOWTIMES (Suất chiếu mẫu — ngày 20-22/05/2026)
INSERT IGNORE INTO showtimes (movie_id, room_id, start_time, end_time, ticket_price, status, created_at) VALUES
-- Phòng 1: Avengers, Inception, Spider-Man (20/05)
((SELECT id FROM movies WHERE tmdb_id = 299534), (SELECT id FROM rooms WHERE name='Phòng 1'), '2026-05-20 09:00:00', '2026-05-20 12:16:00', 75000, 'ACTIVE', NOW()),
((SELECT id FROM movies WHERE tmdb_id = 27205), (SELECT id FROM rooms WHERE name='Phòng 1'), '2026-05-20 13:00:00', '2026-05-20 15:43:00', 65000, 'ACTIVE', NOW()),
((SELECT id FROM movies WHERE tmdb_id = 634649), (SELECT id FROM rooms WHERE name='Phòng 1'), '2026-05-20 16:00:00', '2026-05-20 18:43:00', 70000, 'ACTIVE', NOW()),
((SELECT id FROM movies WHERE tmdb_id = 299534), (SELECT id FROM rooms WHERE name='Phòng 1'), '2026-05-20 19:30:00', '2026-05-20 22:46:00', 85000, 'ACTIVE', NOW()),

-- Phòng 2: Dune 2, Oppenheimer, Dark Knight (20/05)
((SELECT id FROM movies WHERE tmdb_id = 693134), (SELECT id FROM rooms WHERE name='Phòng 2'), '2026-05-20 09:30:00', '2026-05-20 12:31:00', 80000, 'ACTIVE', NOW()),
((SELECT id FROM movies WHERE tmdb_id = 872585), (SELECT id FROM rooms WHERE name='Phòng 2'), '2026-05-20 13:00:00', '2026-05-20 16:15:00', 75000, 'ACTIVE', NOW()),
((SELECT id FROM movies WHERE tmdb_id = 155), (SELECT id FROM rooms WHERE name='Phòng 2'), '2026-05-20 17:00:00', '2026-05-20 19:47:00', 70000, 'ACTIVE', NOW()),
((SELECT id FROM movies WHERE tmdb_id = 693134), (SELECT id FROM rooms WHERE name='Phòng 2'), '2026-05-20 20:30:00', '2026-05-20 23:31:00', 90000, 'ACTIVE', NOW()),

-- Phòng 3: Inside Out 2, Deadpool, Avatar 2 (20/05)
((SELECT id FROM movies WHERE tmdb_id = 1022789), (SELECT id FROM rooms WHERE name='Phòng 3'), '2026-05-20 09:00:00', '2026-05-20 10:55:00', 55000, 'ACTIVE', NOW()),
((SELECT id FROM movies WHERE tmdb_id = 533535), (SELECT id FROM rooms WHERE name='Phòng 3'), '2026-05-20 11:30:00', '2026-05-20 13:53:00', 70000, 'ACTIVE', NOW()),
((SELECT id FROM movies WHERE tmdb_id = 76600), (SELECT id FROM rooms WHERE name='Phòng 3'), '2026-05-20 14:30:00', '2026-05-20 17:57:00', 80000, 'ACTIVE', NOW()),
((SELECT id FROM movies WHERE tmdb_id = 1022789), (SELECT id FROM rooms WHERE name='Phòng 3'), '2026-05-20 18:30:00', '2026-05-20 20:25:00', 60000, 'ACTIVE', NOW()),

-- 21/05 — Phòng 1, 2, 3
((SELECT id FROM movies WHERE tmdb_id = 27205), (SELECT id FROM rooms WHERE name='Phòng 1'), '2026-05-21 10:00:00', '2026-05-21 12:43:00', 65000, 'ACTIVE', NOW()),
((SELECT id FROM movies WHERE tmdb_id = 634649), (SELECT id FROM rooms WHERE name='Phòng 1'), '2026-05-21 14:00:00', '2026-05-21 16:43:00', 70000, 'ACTIVE', NOW()),
((SELECT id FROM movies WHERE tmdb_id = 533535), (SELECT id FROM rooms WHERE name='Phòng 1'), '2026-05-21 19:00:00', '2026-05-21 21:23:00', 75000, 'ACTIVE', NOW()),

((SELECT id FROM movies WHERE tmdb_id = 872585), (SELECT id FROM rooms WHERE name='Phòng 2'), '2026-05-21 09:00:00', '2026-05-21 12:15:00', 75000, 'ACTIVE', NOW()),
((SELECT id FROM movies WHERE tmdb_id = 299534), (SELECT id FROM rooms WHERE name='Phòng 2'), '2026-05-21 13:00:00', '2026-05-21 16:16:00', 80000, 'ACTIVE', NOW()),
((SELECT id FROM movies WHERE tmdb_id = 155), (SELECT id FROM rooms WHERE name='Phòng 2'), '2026-05-21 19:30:00', '2026-05-21 22:17:00', 85000, 'ACTIVE', NOW()),

((SELECT id FROM movies WHERE tmdb_id = 76600), (SELECT id FROM rooms WHERE name='Phòng 3'), '2026-05-21 10:00:00', '2026-05-21 13:27:00', 80000, 'ACTIVE', NOW()),
((SELECT id FROM movies WHERE tmdb_id = 1022789), (SELECT id FROM rooms WHERE name='Phòng 3'), '2026-05-21 14:00:00', '2026-05-21 15:55:00', 55000, 'ACTIVE', NOW()),
((SELECT id FROM movies WHERE tmdb_id = 693134), (SELECT id FROM rooms WHERE name='Phòng 3'), '2026-05-21 16:30:00', '2026-05-21 19:31:00', 85000, 'ACTIVE', NOW()),
((SELECT id FROM movies WHERE tmdb_id = 533535), (SELECT id FROM rooms WHERE name='Phòng 3'), '2026-05-21 20:00:00', '2026-05-21 22:23:00', 75000, 'ACTIVE', NOW()),

-- 22/05 - 28/05 (Lặp lại lịch với phim khác nhau cho phong phú)
((SELECT id FROM movies WHERE tmdb_id = 1022789), (SELECT id FROM rooms WHERE name='Phòng 1'), '2026-05-22 09:00:00', '2026-05-22 10:55:00', 60000, 'ACTIVE', NOW()),
((SELECT id FROM movies WHERE tmdb_id = 533535), (SELECT id FROM rooms WHERE name='Phòng 1'), '2026-05-22 12:00:00', '2026-05-22 14:23:00', 70000, 'ACTIVE', NOW()),
((SELECT id FROM movies WHERE tmdb_id = 693134), (SELECT id FROM rooms WHERE name='Phòng 1'), '2026-05-22 15:30:00', '2026-05-22 18:31:00', 85000, 'ACTIVE', NOW()),
((SELECT id FROM movies WHERE tmdb_id = 299534), (SELECT id FROM rooms WHERE name='Phòng 1'), '2026-05-22 19:30:00', '2026-05-22 22:46:00', 90000, 'ACTIVE', NOW()),

((SELECT id FROM movies WHERE tmdb_id = 872585), (SELECT id FROM rooms WHERE name='Phòng 2'), '2026-05-22 08:30:00', '2026-05-22 11:45:00', 75000, 'ACTIVE', NOW()),
((SELECT id FROM movies WHERE tmdb_id = 155), (SELECT id FROM rooms WHERE name='Phòng 2'), '2026-05-22 13:00:00', '2026-05-22 15:47:00', 80000, 'ACTIVE', NOW()),
((SELECT id FROM movies WHERE tmdb_id = 27205), (SELECT id FROM rooms WHERE name='Phòng 2'), '2026-05-22 17:00:00', '2026-05-22 19:43:00', 75000, 'ACTIVE', NOW()),
((SELECT id FROM movies WHERE tmdb_id = 76600), (SELECT id FROM rooms WHERE name='Phòng 2'), '2026-05-22 20:30:00', '2026-05-22 23:57:00', 90000, 'ACTIVE', NOW()),

((SELECT id FROM movies WHERE tmdb_id = 634649), (SELECT id FROM rooms WHERE name='Phòng 3'), '2026-05-22 09:30:00', '2026-05-22 12:13:00', 75000, 'ACTIVE', NOW()),
((SELECT id FROM movies WHERE tmdb_id = 1022789), (SELECT id FROM rooms WHERE name='Phòng 3'), '2026-05-22 13:30:00', '2026-05-22 15:25:00', 60000, 'ACTIVE', NOW()),
((SELECT id FROM movies WHERE tmdb_id = 533535), (SELECT id FROM rooms WHERE name='Phòng 3'), '2026-05-22 16:30:00', '2026-05-22 18:53:00', 80000, 'ACTIVE', NOW()),
((SELECT id FROM movies WHERE tmdb_id = 693134), (SELECT id FROM rooms WHERE name='Phòng 3'), '2026-05-22 20:00:00', '2026-05-22 23:01:00', 85000, 'ACTIVE', NOW()),

-- 23/05 (Cuối tuần - Giá vé cao hơn)
((SELECT id FROM movies WHERE tmdb_id = 533535), (SELECT id FROM rooms WHERE name='Phòng 1'), '2026-05-23 09:00:00', '2026-05-23 11:23:00', 80000, 'ACTIVE', NOW()),
((SELECT id FROM movies WHERE tmdb_id = 1022789), (SELECT id FROM rooms WHERE name='Phòng 1'), '2026-05-23 12:30:00', '2026-05-23 14:25:00', 70000, 'ACTIVE', NOW()),
((SELECT id FROM movies WHERE tmdb_id = 299534), (SELECT id FROM rooms WHERE name='Phòng 1'), '2026-05-23 15:30:00', '2026-05-23 18:46:00', 95000, 'ACTIVE', NOW()),
((SELECT id FROM movies WHERE tmdb_id = 693134), (SELECT id FROM rooms WHERE name='Phòng 1'), '2026-05-23 20:00:00', '2026-05-23 23:01:00', 95000, 'ACTIVE', NOW()),

((SELECT id FROM movies WHERE tmdb_id = 76600), (SELECT id FROM rooms WHERE name='Phòng 2'), '2026-05-23 08:30:00', '2026-05-23 11:57:00', 95000, 'ACTIVE', NOW()),
((SELECT id FROM movies WHERE tmdb_id = 872585), (SELECT id FROM rooms WHERE name='Phòng 2'), '2026-05-23 13:00:00', '2026-05-23 16:15:00', 85000, 'ACTIVE', NOW()),
((SELECT id FROM movies WHERE tmdb_id = 155), (SELECT id FROM rooms WHERE name='Phòng 2'), '2026-05-23 17:30:00', '2026-05-23 20:17:00', 85000, 'ACTIVE', NOW()),

((SELECT id FROM movies WHERE tmdb_id = 634649), (SELECT id FROM rooms WHERE name='Phòng 3'), '2026-05-23 10:00:00', '2026-05-23 12:43:00', 80000, 'ACTIVE', NOW()),
((SELECT id FROM movies WHERE tmdb_id = 27205), (SELECT id FROM rooms WHERE name='Phòng 3'), '2026-05-23 14:00:00', '2026-05-23 16:43:00', 80000, 'ACTIVE', NOW()),
((SELECT id FROM movies WHERE tmdb_id = 533535), (SELECT id FROM rooms WHERE name='Phòng 3'), '2026-05-23 18:00:00', '2026-05-23 20:23:00', 85000, 'ACTIVE', NOW()),
((SELECT id FROM movies WHERE tmdb_id = 1022789), (SELECT id FROM rooms WHERE name='Phòng 3'), '2026-05-23 21:30:00', '2026-05-23 23:25:00', 70000, 'ACTIVE', NOW()),

-- 24/05 (Chủ Nhật)
((SELECT id FROM movies WHERE tmdb_id = 299534), (SELECT id FROM rooms WHERE name='Phòng 1'), '2026-05-24 09:30:00', '2026-05-24 12:46:00', 95000, 'ACTIVE', NOW()),
((SELECT id FROM movies WHERE tmdb_id = 693134), (SELECT id FROM rooms WHERE name='Phòng 1'), '2026-05-24 14:00:00', '2026-05-24 17:01:00', 95000, 'ACTIVE', NOW()),
((SELECT id FROM movies WHERE tmdb_id = 533535), (SELECT id FROM rooms WHERE name='Phòng 1'), '2026-05-24 18:30:00', '2026-05-24 20:53:00', 85000, 'ACTIVE', NOW()),

((SELECT id FROM movies WHERE tmdb_id = 1022789), (SELECT id FROM rooms WHERE name='Phòng 2'), '2026-05-24 09:00:00', '2026-05-24 10:55:00', 70000, 'ACTIVE', NOW()),
((SELECT id FROM movies WHERE tmdb_id = 76600), (SELECT id FROM rooms WHERE name='Phòng 2'), '2026-05-24 12:00:00', '2026-05-24 15:27:00', 95000, 'ACTIVE', NOW()),
((SELECT id FROM movies WHERE tmdb_id = 872585), (SELECT id FROM rooms WHERE name='Phòng 2'), '2026-05-24 16:30:00', '2026-05-24 19:45:00', 85000, 'ACTIVE', NOW()),

((SELECT id FROM movies WHERE tmdb_id = 155), (SELECT id FROM rooms WHERE name='Phòng 3'), '2026-05-24 10:30:00', '2026-05-24 13:17:00', 85000, 'ACTIVE', NOW()),
((SELECT id FROM movies WHERE tmdb_id = 634649), (SELECT id FROM rooms WHERE name='Phòng 3'), '2026-05-24 14:30:00', '2026-05-24 17:13:00', 80000, 'ACTIVE', NOW()),
((SELECT id FROM movies WHERE tmdb_id = 27205), (SELECT id FROM rooms WHERE name='Phòng 3'), '2026-05-24 18:30:00', '2026-05-24 21:13:00', 80000, 'ACTIVE', NOW()),

-- 25/05 (Thứ 2)
((SELECT id FROM movies WHERE tmdb_id = 1022789), (SELECT id FROM rooms WHERE name='Phòng 1'), '2026-05-25 18:00:00', '2026-05-25 19:55:00', 60000, 'ACTIVE', NOW()),
((SELECT id FROM movies WHERE tmdb_id = 533535), (SELECT id FROM rooms WHERE name='Phòng 1'), '2026-05-25 21:00:00', '2026-05-25 23:23:00', 70000, 'ACTIVE', NOW()),

-- 26/05 (Thứ 3)
((SELECT id FROM movies WHERE tmdb_id = 693134), (SELECT id FROM rooms WHERE name='Phòng 2'), '2026-05-26 17:30:00', '2026-05-26 20:31:00', 85000, 'ACTIVE', NOW()),
((SELECT id FROM movies WHERE tmdb_id = 299534), (SELECT id FROM rooms WHERE name='Phòng 2'), '2026-05-26 21:00:00', '2026-05-26 23:55:00', 90000, 'ACTIVE', NOW()),

-- 27/05 (Thứ 4)
((SELECT id FROM movies WHERE tmdb_id = 872585), (SELECT id FROM rooms WHERE name='Phòng 3'), '2026-05-27 18:00:00', '2026-05-27 21:15:00', 75000, 'ACTIVE', NOW()),
((SELECT id FROM movies WHERE tmdb_id = 155), (SELECT id FROM rooms WHERE name='Phòng 3'), '2026-05-27 21:45:00', '2026-05-28 00:32:00', 80000, 'ACTIVE', NOW()),

-- 28/05 (Thứ 5)
((SELECT id FROM movies WHERE tmdb_id = 76600), (SELECT id FROM rooms WHERE name='Phòng 1'), '2026-05-28 17:00:00', '2026-05-28 20:27:00', 80000, 'ACTIVE', NOW()),
((SELECT id FROM movies WHERE tmdb_id = 634649), (SELECT id FROM rooms WHERE name='Phòng 1'), '2026-05-28 21:00:00', '2026-05-28 23:43:00', 70000, 'ACTIVE', NOW()),

-- 29/05 (Thứ 6)
((SELECT id FROM movies WHERE tmdb_id = 1022789), (SELECT id FROM rooms WHERE name='Phòng 2'), '2026-05-29 18:30:00', '2026-05-29 20:25:00', 60000, 'ACTIVE', NOW()),
((SELECT id FROM movies WHERE tmdb_id = 533535), (SELECT id FROM rooms WHERE name='Phòng 2'), '2026-05-29 21:00:00', '2026-05-29 23:23:00', 75000, 'ACTIVE', NOW());

-- ============================================
-- BOOKINGS & TICKETS SEED (Demo Data)
-- ============================================
INSERT IGNORE INTO bookings (booking_code, user_id, booking_date, total_amount, status) VALUES
('BKG210520261', (SELECT id FROM users WHERE username='customer01'), '2026-05-21 08:30:00', 130000, 'CONFIRMED'),
('BKG210520262', (SELECT id FROM users WHERE username='customer02'), '2026-05-21 09:15:00', 215000, 'CONFIRMED'),
('BKG220520261', (SELECT id FROM users WHERE username='customer03'), '2026-05-22 10:00:00', 380000, 'CONFIRMED'),
('BKG230520261', (SELECT id FROM users WHERE username='customer01'), '2026-05-23 07:00:00', 80000, 'CANCELLED'),
('BKG200520261', (SELECT id FROM users WHERE username='customer02'), '2026-05-20 18:00:00', 95000, 'CHECKED_IN');

-- TICKETS for BKG210520261 (Customer 01, Showtime: Inception 21/05 10:00, 2 vé Standard = 130k)
INSERT IGNORE INTO tickets (booking_id, showtime_id, seat_id, price) VALUES
((SELECT id FROM bookings WHERE booking_code='BKG210520261'), 
 (SELECT id FROM showtimes WHERE start_time='2026-05-21 10:00:00' LIMIT 1),
 (SELECT id FROM seats WHERE seat_name='D4' AND room_id=(SELECT id FROM rooms WHERE name='Phòng 1')), 65000),
((SELECT id FROM bookings WHERE booking_code='BKG210520261'), 
 (SELECT id FROM showtimes WHERE start_time='2026-05-21 10:00:00' LIMIT 1),
 (SELECT id FROM seats WHERE seat_name='D5' AND room_id=(SELECT id FROM rooms WHERE name='Phòng 1')), 65000);

-- TICKETS for BKG210520262 (Customer 02, Showtime: Spider-Man 21/05 14:00, 2 vé VIP = 182k + Combo = 215k)
INSERT IGNORE INTO tickets (booking_id, showtime_id, seat_id, price) VALUES
((SELECT id FROM bookings WHERE booking_code='BKG210520262'), 
 (SELECT id FROM showtimes WHERE start_time='2026-05-21 14:00:00' LIMIT 1),
 (SELECT id FROM seats WHERE seat_name='E5' AND room_id=(SELECT id FROM rooms WHERE name='Phòng 1')), 91000),
((SELECT id FROM bookings WHERE booking_code='BKG210520262'), 
 (SELECT id FROM showtimes WHERE start_time='2026-05-21 14:00:00' LIMIT 1),
 (SELECT id FROM seats WHERE seat_name='E6' AND room_id=(SELECT id FROM rooms WHERE name='Phòng 1')), 91000);

-- BOOKING_PRODUCTS for BKG210520262
INSERT IGNORE INTO booking_products (booking_id, product_id, quantity, price) VALUES
((SELECT id FROM bookings WHERE booking_code='BKG210520262'), (SELECT id FROM products WHERE name='Bắp Rang Bơ (Nhỏ)'), 1, 35000);
