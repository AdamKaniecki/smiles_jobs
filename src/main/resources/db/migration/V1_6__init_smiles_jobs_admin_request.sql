CREATE TABLE admin_request (
    admin_request_id SERIAL NOT NULL,
    company_id INT NOT NULL ,
    candidate_id INT NOT NULL,
    request_status BOOLEAN,
    administrator_id INT NOT NULL,
    UNIQUE (company_id),
    UNIQUE (candidate_id),
    UNIQUE (administrator_id),
    PRIMARY KEY (admin_request_id),
     CONSTRAINT fk_admin_request_company
            FOREIGN KEY (company_id)
                REFERENCES company (company_id),
     CONSTRAINT fk_admin_request_candidate
            FOREIGN KEY (candidate_id)
                REFERENCES candidate (candidate_id),
     CONSTRAINT fk_admin_request_administrator
                 FOREIGN KEY (administrator_id)
                     REFERENCES administrator (administrator_id)
);