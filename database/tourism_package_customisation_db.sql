CREATE DATABASE IF NOT EXISTS tourism_package_customisation_db;
    USE tourism_package_customisation_db;


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