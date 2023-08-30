CREATE TABLE company (
    company_id SERIAL NOT NULL,
    company_name VARCHAR(255) NOT NULL,
    company_description TEXT,
    email VARCHAR(255),
    recruitment_criteria TEXT,
    request_employment BOOLEAN,
    address_id INT  NOT NULL,
    PRIMARY KEY (company_id),
    CONSTRAINT fk_company_address
        FOREIGN KEY (address_id)
           REFERENCES address (address_id)
);