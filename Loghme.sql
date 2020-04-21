CREATE TABLE IF NOT EXISTS restaurant (
    id VARCHAR(24),
    name VARCHAR(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci,
    location_x INTEGER,
    location_y INTEGER,
    logo VARCHAR(200),
    PRIMARY KEY(id));

CREATE TABLE IF NOT EXISTS food (
    id INTEGER NOT NULL AUTO_INCREMENT,
    name VARCHAR(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci,
    description VARCHAR(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci,
    popularity INTEGER,
    price INTEGER,
    image VARCHAR(200),
    restaurantId VARCHAR(24),
    available INTEGER,
    PRIMARY KEY(id),
    FOREIGN KEY(restaurantId) REFERENCES restaurant(id));

CREATE TABLE IF NOT EXISTS user (
    firstname VARCHAR(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci,
    lastname VARCHAR(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci,
    phone VARCHAR(11),
    email VARCHAR(30),
    credit INTEGER,
    PRIMARY KEY(email));

CREATE TABLE IF NOT EXISTS orders (
    id INTEGER NOT NULL AUTO_INCREMENT,
    userId VARCHAR(30),
    restaurantId VARCHAR(24),
    state VARCHAR(20),
    remMin INTEGER,
    remSec INTEGER,
    PRIMARY KEY(id),
    FOREIGN KEY(userId) REFERENCES user(email),
    FOREIGN KEY(restaurantId) REFERENCES restaurant(id));