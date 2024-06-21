CREATE TABLE USERS (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255),
    name VARCHAR(255),
    password VARCHAR(255),
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE RENTALS (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255),
    surface NUMERIC,
    price NUMERIC,
    picture VARCHAR(255),
    description VARCHAR(2000),
    owner_id INTEGER NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    CONSTRAINT fk_owner FOREIGN KEY (owner_id) REFERENCES USERS (id)
);

CREATE TABLE MESSAGES (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    rental_id INTEGER,
    user_id INTEGER,
    message VARCHAR(2000),
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    CONSTRAINT fk_rental FOREIGN KEY (rental_id) REFERENCES RENTALS (id),
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES USERS (id)
);

CREATE UNIQUE INDEX USERS_index ON USERS (email);
