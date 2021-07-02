DROP TABLE IF EXISTS users;

CREATE TABLE users (
                              id INT AUTO_INCREMENT  PRIMARY KEY,
                              first_name VARCHAR(250) NOT NULL,
                              last_name VARCHAR(250) NOT NULL,
                              email VARCHAR(250) UNIQUE NOT NULL
);

INSERT INTO users (first_name, last_name, email) VALUES
('Aliko', 'Dangote', 'aaa@aaa.com'),
('Bill', 'Gates', 'bbb@bbb.com'),
('Folrunsho', 'Alakija', 'ccc@ccc.com');