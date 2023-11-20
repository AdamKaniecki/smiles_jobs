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
--    salary_min NUMERIC(7,2) ,
--    salary_max NUMERIC(7,2) NOT NULL,
    date_time_job_offer TIMESTAMP WITH TIME ZONE NOT NULL,
    user_id INT not null,
    PRIMARY KEY(job_offer_id),
    CONSTRAINT fk_job_offer_table_user_table
        FOREIGN KEY (user_id)
            REFERENCES user_table(user_id)

);
--CREATE TABLE notification_table (
--    notification_id serial not null,
--    notification_date_time TIMESTAMP WITH TIME ZONE NOT NULL,
--    message VARCHAR(255) NOT NULL,
--    interaction VARCHAR(32) NOT NULL,
--    PRIMARY KEY(notification_id)
--
--);
--
--CREATE TABLE user_notification_table (
--    user_id INT,
--    notification_id INT,
--    PRIMARY KEY (user_id, notification_id),
--    FOREIGN KEY (user_id) REFERENCES user_table(user_id),
--    FOREIGN KEY (notification_id) REFERENCES notification_table(notification_id)
--);

CREATE TABLE address_table(
address_id SERIAL NOT NULL,
country  VARCHAR(32) NOT NULL,
city  VARCHAR(32) NOT NULL,
street_and_number  VARCHAR(32) NOT NULL,
PRIMARY KEY(address_id)
);


CREATE TABLE business_card_table(
business_card_id SERIAL NOT NULL,
office VARCHAR(128) NOT NULL,
scope_operations VARCHAR(128) not null,
recruitment_email VARCHAR(32) NOT NULL,
phone_number VARCHAR(32) NOT NULL,
company_description TEXT NOT NULL,
technologies_and_tools TEXT NOT NULL,
certificates_and_awards TEXT NOT NULL,
address_id INT NOT NULL,
user_id INT NOT NULL,
PRIMARY KEY(business_card_id),
 CONSTRAINT fk_business_card_table_address_table
    FOREIGN KEY (address_id)
        REFERENCES address_table(address_id),
 CONSTRAINT fk_business_card_table_user_table
    FOREIGN KEY (user_id)
         REFERENCES user_table(user_id)

);

    CREATE TABLE CV(
    cv_id SERIAL NOT NULL,
    cv_name VARCHAR(32) NOT NULL,
    cv_surname VARCHAR(32) NOT NULL,
    date_of_birth VARCHAR(32) NOT NULl,
    sex VARCHAR(32) NOT NULL,
    marital_status VARCHAR(32) NOT NULL,
    phone_number VARCHAR(32) NOT NULL,
    contact_email VARCHAR(32) NOT NULL,
    cv_work_experience VARCHAR(32) NOT NULL,
    education VARCHAR(32) NOT NULL,
    skills VARCHAR(32) NOT NULL,
    language VARCHAR(32) NOT NULL,
    language_level VARCHAR(32) NOT NULL,
    hobby TEXT NOT NULL,
    address_id INT ,
    user_id INT NOT NULL,
--    advertisement_id INT NOT NULL,
    PRIMARY KEY(cv_id),
    CONSTRAINT fk_CV_address_table
        FOREIGN KEY (address_id)
            REFERENCES address_table(address_id),
    CONSTRAINT fk_CV_user_table
        FOREIGN KEY (user_id)
             REFERENCES user_table(user_id)
--    CONSTRAINT fk_CV_advertisement_table
--            FOREIGN KEY (advertisement_id)
--                 REFERENCES advertisement_table(advertisement_id)



    );



