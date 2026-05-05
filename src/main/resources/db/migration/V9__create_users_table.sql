CREATE TABLE users (
                       id       BIGSERIAL    PRIMARY KEY,
                       username VARCHAR(100) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       role     VARCHAR(50)  NOT NULL
);

-- Insert default admin and user
-- Passwords are BCrypt hashed — both passwords are "password123"
INSERT INTO users (username, password, role) VALUES
                                                 ('admin', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'ROLE_ADMIN'),
                                                 ('user',  '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'ROLE_USER');