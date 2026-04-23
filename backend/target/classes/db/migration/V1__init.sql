CREATE TABLE task (
                      id SERIAL PRIMARY KEY,
                      name VARCHAR(255),
                      completed BOOLEAN,
                      assigned_to VARCHAR(255)
);