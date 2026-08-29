CREATE TABLE users (
    id INT PRIMARY KEY auto_increment,
    name VARCHAR(80) NOT NULL,
    email VARCHAR(100) NOT NULL,
    password VARCHAR(255) NOT NULL
);