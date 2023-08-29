CREATE TABLE address (
    address_id SERIAL NOT NULL,
    country VARCHAR(100),
    city VARCHAR(255),
    postal_code VARCHAR(10),
    street_and_number VARCHAR(255),
    PRIMARY KEY (address_id)
);