CREATE TABLE users (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       name VARCHAR(100) NOT NULL,
                       userRole VARCHAR(20) NOT NULL,
                       created_at DATETIME NOT NULL
);

CREATE TABLE concert (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         title VARCHAR(255) NOT NULL,
                         description TEXT,
                         location VARCHAR(255),
                         start_time DATETIME NOT NULL,
                         end_time DATETIME NOT NULL,
                         reservation_open_at DATETIME NOT NULL,
                         reservation_close_at DATETIME NOT NULL,
                         created_at DATETIME NOT NULL
);

CREATE TABLE seat (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      grade VARCHAR(50) NOT NULL,
                      `row_name` VARCHAR(50) NOT NULL,
                      seat_number INT NOT NULL,
                      status VARCHAR(20) NOT NULL,
                      concert_id BIGINT,
                      CONSTRAINT fk_seat_concert FOREIGN KEY (concert_id) REFERENCES concert(id) ON DELETE CASCADE
);

CREATE TABLE reservation (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             user_id BIGINT,
                             seat_id BIGINT,
                             concert_id BIGINT,
                             status VARCHAR(20) NOT NULL,
                             reserved_at DATETIME NOT NULL,
                             expired_at DATETIME,
                             CONSTRAINT fk_reservation_user FOREIGN KEY (user_id) REFERENCES users(id),
                             CONSTRAINT fk_reservation_seat FOREIGN KEY (seat_id) REFERENCES seat(id),
                             CONSTRAINT fk_reservation_concert FOREIGN KEY (concert_id) REFERENCES concert(id)
);

CREATE TABLE payment (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         reservation_id BIGINT UNIQUE,
                         transaction_id VARCHAR(255) NOT NULL,
                         amount INT NOT NULL,
                         status VARCHAR(30) NOT NULL,
                         paid_at DATETIME,
                         CONSTRAINT fk_payment_reservation FOREIGN KEY (reservation_id) REFERENCES reservation(id)
);

CREATE TABLE waiting_queue (
                               id BIGINT AUTO_INCREMENT PRIMARY KEY,
                               session_id VARCHAR(255),
                               user_id BIGINT,
                               queue_number INT NOT NULL,
                               entered BOOLEAN NOT NULL,
                               created_at DATETIME NOT NULL
);

CREATE TABLE location (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          name VARCHAR(255) NOT NULL,
                          address VARCHAR(500) NOT NULL,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE seat_grade_config (
                                   id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                   grade VARCHAR(50) NOT NULL,
                                   start_row CHAR(1) NOT NULL,
                                   end_row CHAR(1) NOT NULL,
                                   seat_per_row INT NOT NULL,
                                   price INT NOT NULL,
                                   location_id BIGINT NOT NULL,
                                   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                   updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

ALTER TABLE seat_grade_config
    ADD CONSTRAINT fk_seat_grade_location
        FOREIGN KEY (location_id)
            REFERENCES location(id)
            ON DELETE CASCADE;
