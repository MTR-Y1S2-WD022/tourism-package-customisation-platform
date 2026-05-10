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

    CREATE TABLE IF NOT EXISTS coupons (
        coupon_id INT PRIMARY KEY AUTO_INCREMENT,
        coupon_code VARCHAR(50) NOT NULL UNIQUE,
        discount_type VARCHAR(20) NOT NULL,
        discount_value DECIMAL(10,2) NOT NULL,
        issue_date DATE NOT NULL,
        expiry_date DATE NOT NULL,
        status VARCHAR(20) DEFAULT 'ACTIVE',
        created_by_admin_id INT,
        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

        FOREIGN KEY (created_by_admin_id) REFERENCES admins(admin_id)
    );

    SELECT * FROM coupons;