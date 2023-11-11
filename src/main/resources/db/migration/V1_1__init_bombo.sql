CREATE TABLE advertisement_table(
    advertisement_id SERIAL NOT NULL,
    name VARCHAR(32) NOT NULL,
    surname VARCHAR(32) NOT NULL,
    work_experience VARCHAR(168) NOT NULL,
    knowledge_of_technologies VARCHAR(168) NOT NULL,
      date_time TIMESTAMP WITH TIME ZONE NOT NULL,
    user_id INT not null,
    PRIMARY KEY(advertisement_id),
    CONSTRAINT fk_advertisement_table_user_table
        FOREIGN KEY (user_id)
            REFERENCES user_table(user_id)
);

CREATE TABLE job_offer_table(
    job_offer_id SERIAL NOT NULL,
    company_name VARCHAR(32) NOT NULL,
    position VARCHAR(32) NOT NULL,
    responsibilities VARCHAR(168) NOT NULL,
    required_technologies VARCHAR(168) NOT NULL,
    benefits VARCHAR(168) NOT NULL,
--    salary_min NUMERIC(7,2) NOT NULL,
--    salary_max NUMERIC(7,2) NOT NULL,
    date_time_job_offer TIMESTAMP WITH TIME ZONE NOT NULL,
    user_id INT not null,
    PRIMARY KEY(job_offer_id),
    CONSTRAINT fk_job_offer_table_user_table
        FOREIGN KEY (user_id)
            REFERENCES user_table(user_id)

);




