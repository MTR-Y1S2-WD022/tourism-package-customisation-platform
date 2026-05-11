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
            'SUPER_ADMIN',
            'ACTIVE',
            TRUE
        );

CREATE TABLE IF NOT EXISTS users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    phone_number VARCHAR(20) NOT NULL,
    address VARCHAR(255),
    profile_image VARCHAR(255),
    status VARCHAR(20) DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );
   
CREATE TABLE IF NOT EXISTS reviews (
     review_id INT PRIMARY KEY AUTO_INCREMENT,
     booking_id INT NOT NULL,
     user_id INT NOT NULL,
     package_id INT NOT NULL,
     rating INT NOT NULL,
     comment TEXT,
     status VARCHAR(20) DEFAULT 'VISIBLE',
                                  
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (booking_id) REFERENCES bookings(booking_id)
    ON DELETE CASCADE,

    FOREIGN KEY (user_id) REFERENCES users(user_id)
    ON DELETE CASCADE,

    FOREIGN KEY (package_id) REFERENCES tour_packages(package_id)
    ON DELETE CASCADE,

    CONSTRAINT chk_rating CHECK (rating >= 1 AND rating <= 5)
    );