CREATE DATABASE IF NOT EXISTS tourism_package_customisation_db;
    USE tourism_package_customisation_db;

CREATE TABLE IF NOT EXISTS admins (
    admin_id INT PRIMARY KEY AUTO_INCREMENT,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(20) DEFAULT 'ADMIN',
    status VARCHAR(20) DEFAULT 'ACTIVE',
    is_default BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

    INSERT INTO admins (
        full_name,
        email,
        password,
        role,
        status,
        is_default
    )
        VALUES (
            'Default Admin',
            'admin@gmail.com',
            'admin123',
            'ADMIN',
            'ACTIVE',
            TRUE
        );

    SELECT * FROM admins;