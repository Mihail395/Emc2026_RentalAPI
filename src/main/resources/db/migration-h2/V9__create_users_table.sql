CREATE TABLE users (
                       id       BIGINT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(100) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       role     VARCHAR(50)  NOT NULL
);

INSERT INTO users (username, password, role) VALUES
                                                 ('admin', '$2a$10$GhQOMsek2utwSsVIjWyOM.p5jXM7qJORpJYRtMXeC0Pfeg.HdWtfS', 'ROLE_ADMIN'),
                                                 ('user',  '$2a$10$GhQOMsek2utwSsVIjWyOM.p5jXM7qJORpJYRtMXeC0Pfeg.HdWtfS', 'ROLE_USER');