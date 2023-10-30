CREATE TABLE advertisement_table(
    advertisement_id SERIAL NOT NULL,
    name VARCHAR(32) NOT NULL,
    user_id INT ,
    PRIMARY KEY(advertisement_id),
    CONSTRAINT fk_advertisement_table_user_table
        FOREIGN KEY (user_id)
            REFERENCES user_table(user_id)

);






