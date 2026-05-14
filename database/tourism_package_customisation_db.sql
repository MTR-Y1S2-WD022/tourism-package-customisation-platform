CREATE DATABASE IF NOT EXISTS tourism_package_customisation_db;
USE tourism_package_customisation_db;
   
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