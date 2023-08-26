CREATE TABLE candidate (
    candidate_id SERIAL NOT NULL,
    full_name VARCHAR(255),
    email VARCHAR(255) NOT NULL,
    phone_number VARCHAR(20) NOT NULL,
    profile_picture_url VARCHAR(255),
    address_id INT REFERENCES address(address_id),
    cv TEXT,
    desired_position TEXT,
    preferred_technologies TEXT,
    availability_status BOOLEAN,
    employed BOOLEAN,
    PRIMARY KEY (candidate_id),
    UNIQUE (email),
    UNIQUE (phone_number),
    CONSTRAINT fk_candidate_address
         FOREIGN KEY (address_id)
              REFERENCES address (address_id)
);