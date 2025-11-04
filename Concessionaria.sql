CREATE DATABASE if NOT EXISTS dealership;

CREATE TABLE if NOT EXISTS cars(
	car_id INT AUTO_INCREMENT PRIMARY KEY,
	make CHAR(13),
	car_name CHAR(40),
	year_model INT(4),
	plate CHAR(7)
);

CREATE TABLE IF NOT EXISTS market_user(
	user_id INT AUTO_INCREMENT PRIMARY KEY,
	user_name CHAR(40),
	active_consortium ENUM ('S', 'N'), 
	email CHAR(40),
	age INT,
	user_password CHAR(20)
);

CREATE TABLE IF NOT EXISTS posts (
    post_id INT AUTO_INCREMENT PRIMARY KEY,
    content VARCHAR(255),
    post_date DATE,
    user_id INT,
    car_id INT,
    FOREIGN KEY (user_id) REFERENCES market_user(user_id),
    FOREIGN KEY (car_id) REFERENCES cars(car_id)
);
USE dealership;
SELECT * FROM cars;
SELECT * FROM market_user;
SELECT * FROM posts;