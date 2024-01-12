CREATE TABLE user_table
(
    user_id   SERIAL        NOT NULL,
    user_name VARCHAR(32)   NOT NULL,
    email     VARCHAR(32)   NOT NULL,
    password  VARCHAR(128)  NOT NULL,
    active    BOOLEAN       NOT NULL,
    unique(email),
    PRIMARY KEY (user_id)

);

CREATE TABLE role_table
(
    role_id SERIAL      NOT NULL,
    role    VARCHAR(20) NOT NULL,
    PRIMARY KEY (role_id)
);


create TABLE user_role_table (
    user_id INT NOT NULL,
    role_id INT NOT NULL,
    PRIMARY KEY (user_id, role_id),
  CONSTRAINT fk_user_role_table_user_table
        FOREIGN KEY (user_id)
            REFERENCES user_table (user_id),
   CONSTRAINT fk_user_role_table_role_table
          FOREIGN KEY (role_id)
              REFERENCES role_table(role_id)

);



