-- Mật khẩu: Password123!

INSERT IGNORE INTO users (username, password, role, status, created_at, updated_at) VALUES
('admin', '$2a$10$/u4WbGer6kVHjrLGJvqqlO/7F4kc.0tKg29iMS8LAke2iQ1i6nJ3C', 'ADMIN', 'ACTIVE', NOW(), NOW()),
('staff', '$2a$10$/u4WbGer6kVHjrLGJvqqlO/7F4kc.0tKg29iMS8LAke2iQ1i6nJ3C', 'STAFF', 'ACTIVE', NOW(), NOW()),
('customer', '$2a$10$/u4WbGer6kVHjrLGJvqqlO/7F4kc.0tKg29iMS8LAke2iQ1i6nJ3C', 'CUSTOMER', 'ACTIVE', NOW(), NOW());

INSERT IGNORE INTO user_profiles (user_id, full_name, email, phone_number, created_at, updated_at) VALUES
((SELECT id FROM users WHERE username = 'admin'), 'Quản Trị Viên', 'admin@cinewave.vn', '0900000001', NOW(), NOW()),
((SELECT id FROM users WHERE username = 'staff'), 'Nhân Viên Bán Vé', 'staff@cinewave.vn', '0900000002', NOW(), NOW()),
((SELECT id FROM users WHERE username = 'customer'), 'Khách Hàng VIP', 'customer@gmail.com', '0900000003', NOW(), NOW());
