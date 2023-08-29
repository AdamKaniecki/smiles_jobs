ALTER TABLE candidate
ADD COLUMN user_id INT,
ADD FOREIGN KEY (user_id) REFERENCES smiles_jobs_user (user_id);

insert into smiles_jobs_user (user_id, user_name, email, password, active) values (1, 'test_user', 'test_user@zajavka.pl', '$2a$12$qzGxlitieg20o05qCCvMeORoOYooTJWJiv5ZLG.c4bbQoxD4glgq2', true);