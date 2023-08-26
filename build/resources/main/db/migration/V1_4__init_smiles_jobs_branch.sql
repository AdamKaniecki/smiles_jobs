CREATE TABLE branch (
    branch_id SERIAL NOT NULL,
    branch_name VARCHAR(255),
    company_id INT  NOT NULL,
    address_id INT  NOT NULL,
    UNIQUE (company_id),
    UNIQUE (branch_name),
    PRIMARY KEY (branch_id),
    CONSTRAINT fk_branch_company
        FOREIGN KEY (company_id)
            REFERENCES company (company_id),
     CONSTRAINT fk_branch_address
         FOREIGN KEY (address_id)
             REFERENCES address (address_id)


);