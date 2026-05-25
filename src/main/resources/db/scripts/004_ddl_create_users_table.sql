CREATE TABLE j_user (
    id SERIAL PRIMARY KEY,
    name VARCHAR(2000),
    role_id INT NOT NULL REFERENCES j_role(id)
);