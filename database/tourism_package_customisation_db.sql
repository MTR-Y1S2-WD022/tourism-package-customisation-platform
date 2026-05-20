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
    
CREATE TABLE tour_packages (
    package_id INT PRIMARY KEY AUTO_INCREMENT,
    package_name VARCHAR(100) NOT NULL,
    location_area VARCHAR(100) NOT NULL,
    description TEXT,
    base_price DECIMAL(10,2) NOT NULL,
    image_url VARCHAR(255),
    status VARCHAR(20) DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

CREATE TABLE destinations (
     destination_id INT PRIMARY KEY AUTO_INCREMENT,
     destination_name VARCHAR(100) NOT NULL,
     description TEXT,
     image_url VARCHAR(255),
     google_map_url VARCHAR(500),
     base_cost DECIMAL(10,2) DEFAULT 0.00,
     status VARCHAR(20) DEFAULT 'ACTIVE',
     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

CREATE TABLE  IF NOT EXISTS package_destinations (
     id INT PRIMARY KEY AUTO_INCREMENT,
     package_id INT NOT NULL,
     destination_id INT NOT NULL,

     FOREIGN KEY (package_id) REFERENCES tour_packages(package_id)
        ON DELETE CASCADE,

     FOREIGN KEY (destination_id) REFERENCES destinations(destination_id)
        ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS bookings (
      booking_id INT PRIMARY KEY AUTO_INCREMENT,
      user_id INT NOT NULL,
      package_id INT NOT NULL,
      coupon_id INT,

      start_date DATE NOT NULL,
      end_date DATE NOT NULL,

       hotel_type VARCHAR(50) NOT NULL,
      meal_option VARCHAR(50) NOT NULL,
      guide_option VARCHAR(50) NOT NULL,

      subtotal_amount DECIMAL(10,2) NOT NULL,
      discount_amount DECIMAL(10,2) DEFAULT 0.00,
      total_amount DECIMAL(10,2) NOT NULL,

      booking_status VARCHAR(30) DEFAULT 'PENDING',
      payment_status VARCHAR(30) DEFAULT 'PENDING',

      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

      FOREIGN KEY (user_id) REFERENCES users(user_id)
      ON DELETE CASCADE,

      FOREIGN KEY (package_id) REFERENCES tour_packages(package_id)
      ON DELETE CASCADE,

      FOREIGN KEY (coupon_id) REFERENCES coupons(coupon_id)
      ON DELETE SET NULL
      );
     

CREATE TABLE IF NOT EXISTS booking_destinations (
      id INT PRIMARY KEY AUTO_INCREMENT,
      booking_id INT NOT NULL,
      destination_id INT NOT NULL,

      FOREIGN KEY (booking_id) REFERENCES bookings(booking_id)
      ON DELETE CASCADE,

      FOREIGN KEY (destination_id) REFERENCES destinations(destination_id)
      ON DELETE CASCADE
      );

CREATE TABLE IF NOT EXISTS booking_cancellation_requests (
     request_id INT PRIMARY KEY AUTO_INCREMENT,
     booking_id INT NOT NULL,
     user_id INT NOT NULL,

     reason TEXT NOT NULL,
     request_status VARCHAR(20) DEFAULT 'PENDING',

     admin_id INT,
     admin_response TEXT,

     refund_type VARCHAR(20) DEFAULT 'NONE',
     refund_amount DECIMAL(10,2) DEFAULT 0.00,

     requested_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     processed_at TIMESTAMP NULL,

     FOREIGN KEY (booking_id) REFERENCES bookings(booking_id)
        ON DELETE CASCADE,

     FOREIGN KEY (user_id) REFERENCES users(user_id)
        ON DELETE CASCADE,

     FOREIGN KEY (admin_id) REFERENCES admins(admin_id)
        ON DELETE SET NULL
    );
    
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
    


