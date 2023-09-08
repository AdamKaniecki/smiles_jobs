CREATE TABLE candidate (
    candidate_id SERIAL NOT NULL,
    name VARCHAR(255),
    surname VARCHAR(255),
    email VARCHAR(255),
    phone_number VARCHAR(20),
--    skills VARCHAR(255),
--    expected_salary NUMERIC (7, 2)
    availability_status BOOLEAN,
    address_id INT  ,
    candidate_advertisement_id INT ,
    PRIMARY KEY (candidate_id),
--    UNIQUE (email),
--    UNIQUE (phone_number),
    CONSTRAINT fk_candidate_address
         FOREIGN KEY (address_id)
              REFERENCES address (address_id)
--    CONSTRAINT fk_candidate_candidate_advertisement
--                     FOREIGN KEY (candidate_advertisement_id)
--                          REFERENCES candidate_advertisement (candidate_advertisement_id)
);