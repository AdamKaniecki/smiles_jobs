CREATE TABLE user_table
(
    user_id   SERIAL        NOT NULL,
    user_name VARCHAR(32)   NOT NULL,
    email     VARCHAR(32)   NOT NULL,
    password  VARCHAR(128)  NOT NULL,
    active    BOOLEAN       NOT NULL,
    PRIMARY KEY (user_id)

);


create TABLE user_role_table (
    user_id INT NOT NULL,
    roles VARCHAR(255),
    PRIMARY KEY (user_id, roles),
  CONSTRAINT fk_user_role_table_user_table
        FOREIGN KEY (user_id)
            REFERENCES user_table (user_id)

);


