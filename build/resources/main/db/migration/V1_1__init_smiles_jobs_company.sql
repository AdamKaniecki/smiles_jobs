CREATE TABLE company (
    company_id SERIAL NOT NULL,
    company_name VARCHAR(255) NOT NULL,
    company_description TEXT,
    contact_person VARCHAR(255),
    recruitment_criteria TEXT,
    candidate_status TEXT,
    request_employment BOOLEAN,
    PRIMARY KEY (company_id)
);