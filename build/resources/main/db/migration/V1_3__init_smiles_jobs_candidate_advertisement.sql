CREATE TABLE candidate_advertisement (
    candidate_advertisement_id SERIAL NOT NULL,
    number VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    surname VARCHAR(255) NOT NULL,
    work_experience VARCHAR(255) NOT NULL,
    programming_language VARCHAR(255) NOT NULL,
    date_of_advertisement TIMESTAMP WITH TIME ZONE,
    PRIMARY KEY (candidate_advertisement_id),
    UNIQUE(number)

);