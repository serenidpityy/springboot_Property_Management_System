-- Drop tables if they exist to ensure a clean slate for recreation
DROP TABLE IF EXISTS ComplaintSuggestion;
DROP TABLE IF EXISTS CommunityAnnouncement;
DROP TABLE IF EXISTS Charge;
DROP TABLE IF EXISTS ParkingSpace;
DROP TABLE IF EXISTS Property;
DROP TABLE IF EXISTS App_User; -- Renamed from User

-- Create App_User Table
CREATE TABLE App_User (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    user_type ENUM('ADMIN', 'STAFF', 'OWNER') NOT NULL,
    display_name VARCHAR(100),
    account_balance DECIMAL(10, 2) DEFAULT 0.00
);

-- Create Property Table
CREATE TABLE Property (
    id INT AUTO_INCREMENT PRIMARY KEY,
    house_number VARCHAR(50) NOT NULL UNIQUE,
    owner_id INT NOT NULL,
    building VARCHAR(50) NOT NULL,
    unit VARCHAR(50) NOT NULL,
    area DECIMAL(10, 2),
    house_type VARCHAR(50),
    additional_description TEXT,
    occupancy_info VARCHAR(255),
    FOREIGN KEY (owner_id) REFERENCES App_User(id) ON DELETE CASCADE
);

-- Create ParkingSpace Table
CREATE TABLE ParkingSpace (
    id INT AUTO_INCREMENT PRIMARY KEY,
    parking_id VARCHAR(50) NOT NULL UNIQUE,
    owner_id INT NOT NULL,
    FOREIGN KEY (owner_id) REFERENCES App_User(id) ON DELETE CASCADE
);

-- Create Charge Table
CREATE TABLE Charge (
    id INT AUTO_INCREMENT PRIMARY KEY,
    charge_item VARCHAR(100) NOT NULL,
    owner_id INT NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    description TEXT,
    payment_status ENUM('UNPAID', 'PAID') DEFAULT 'UNPAID',
    payment_time DATETIME,
    FOREIGN KEY (owner_id) REFERENCES App_User(id) ON DELETE CASCADE
);

-- Create CommunityAnnouncement Table
CREATE TABLE CommunityAnnouncement (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    publish_time DATETIME NOT NULL
);

-- Create ComplaintSuggestion Table
CREATE TABLE ComplaintSuggestion (
    id INT AUTO_INCREMENT PRIMARY KEY,
    owner_id INT NOT NULL,
    content TEXT NOT NULL,
    submitter_phone VARCHAR(20),
    urgency_level ENUM('LOW', 'MEDIUM', 'HIGH', 'URGENT') DEFAULT 'LOW',
    processing_status ENUM('PENDING', 'PROCESSED') DEFAULT 'PENDING',
    submission_time DATETIME NOT NULL,
    processor_id INT,
    processing_time DATETIME,
    processor_response TEXT,
    FOREIGN KEY (owner_id) REFERENCES App_User(id) ON DELETE CASCADE,
    FOREIGN KEY (processor_id) REFERENCES App_User(id) ON DELETE SET NULL
);
