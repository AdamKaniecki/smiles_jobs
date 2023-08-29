CREATE TABLE administrator (
    administrator_id SERIAL NOT NULL,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    PRIMARY KEY (administrator_id)
);