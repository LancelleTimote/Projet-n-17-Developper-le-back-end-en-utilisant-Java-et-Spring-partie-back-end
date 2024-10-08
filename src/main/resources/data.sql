CREATE TABLE USERS (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE RENTALS (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    surface DECIMAL(10, 2) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    picture VARCHAR(255),
    description TEXT NOT NULL,
    owner_id INTEGER NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_owner FOREIGN KEY (owner_id) REFERENCES USERS (id)
);

CREATE TABLE MESSAGES (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    rental_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    message TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_rental FOREIGN KEY (rental_id) REFERENCES RENTALS (id),
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES USERS (id)
);

CREATE UNIQUE INDEX USERS_index ON USERS (email);