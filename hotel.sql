
-- Create database and use it
CREATE DATABASE hotel_db;
USE hotel_db;

-- Create table to store rooms
CREATE TABLE room (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    type VARCHAR(255) NOT NULL,
    price DOUBLE NOT NULL,
    availability BOOLEAN DEFAULT true
);

-- Insert data into room table
INSERT INTO room (type, price) VALUES ('Single', 100.00);
INSERT INTO room (type, price) VALUES ('Double', 150.00);
INSERT INTO room (type, price) VALUES ('Suite', 200.00);

-- Create table to store bookings
CREATE TABLE booking (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    roomid BIGINT NOT NULL,
    guestname VARCHAR(255) NOT NULL,
    checkin DATE NOT NULL,
    checkout DATE NOT NULL,
    totalprice DOUBLE NOT NULL,
    FOREIGN KEY (roomid) REFERENCES room(id)
);


-- Query to get total expenses per month
SELECT 
    DATE_FORMAT(date, '%Y-%m') AS month,
    SUM(amount) AS total_expenses
FROM 
    expense
GROUP BY 
    DATE_FORMAT(date, '%Y-%m')
ORDER BY 
    month;

-- Query to filter expenses by category during start date and end date
SELECT 
    *
FROM   
    expense
WHERE  
    category = 'Food' AND date BETWEEN '2021-01-01' AND '2021-12-31';