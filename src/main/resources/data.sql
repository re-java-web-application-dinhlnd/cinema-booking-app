-- ============================================
-- CINEWAVE SEED DATA
-- Mật khẩu mặc định: Password123!
-- ============================================

-- 1. USERS
INSERT IGNORE INTO users (username, password, role, status, created_at, updated_at) VALUES
('admin', '$2a$10$/u4WbGer6kVHjrLGJvqqlO/7F4kc.0tKg29iMS8LAke2iQ1i6nJ3C', 'ADMIN', 'ACTIVE', NOW(), NOW()),
('staff', '$2a$10$/u4WbGer6kVHjrLGJvqqlO/7F4kc.0tKg29iMS8LAke2iQ1i6nJ3C', 'STAFF', 'ACTIVE', NOW(), NOW()),
('customer', '$2a$10$/u4WbGer6kVHjrLGJvqqlO/7F4kc.0tKg29iMS8LAke2iQ1i6nJ3C', 'CUSTOMER', 'ACTIVE', NOW(), NOW());

INSERT IGNORE INTO user_profiles (user_id, full_name, email, phone_number, created_at, updated_at) VALUES
((SELECT id FROM users WHERE username = 'admin'), 'Quản Trị Viên', 'admin@cinewave.vn', '0900000001', NOW(), NOW()),
((SELECT id FROM users WHERE username = 'staff'), 'Nhân Viên Bán Vé', 'staff@cinewave.vn', '0900000002', NOW(), NOW()),
((SELECT id FROM users WHERE username = 'customer'), 'Khách Hàng VIP', 'customer@gmail.com', '0900000003', NOW(), NOW());

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
