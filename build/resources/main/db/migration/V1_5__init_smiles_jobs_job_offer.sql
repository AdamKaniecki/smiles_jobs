CREATE TABLE job_offer (
    job_offer_id SERIAL NOT NULL,
    number VARCHAR(255),
    company_name VARCHAR(255),
    position VARCHAR(255),
    programming_language VARCHAR(255),
    job_responsibilities VARCHAR(255),
    knowledge_technology VARCHAR(255),
    benefits VARCHAR(255),
    salary_range VARCHAR(255),
    date_of_job_offers TIMESTAMP WITH TIME ZONE,
    company_id INT NOT NULL,
    PRIMARY KEY (job_offer_id),
    UNIQUE(number),
    CONSTRAINT fk_job_offer_company
         FOREIGN KEY (company_id)
              REFERENCES company (company_id)
);

