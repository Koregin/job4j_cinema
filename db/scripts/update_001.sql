CREATE TABLE if not exists users (
  id SERIAL PRIMARY KEY,
  username VARCHAR NOT NULL,
  email VARCHAR NOT NULL UNIQUE,
  phone VARCHAR NOT NULL UNIQUE
);

CREATE TABLE if not exists sessions (
  id SERIAL PRIMARY KEY,
  name text
);

CREATE TABLE if not exists ticket (
    id SERIAL PRIMARY KEY,
    session_id INT NOT NULL REFERENCES sessions(id),
    line INT NOT NULL,
    cell INT NOT NULL,
    user_id INT NOT NULL REFERENCES users(id)
);

ALTER TABLE ticket ADD CONSTRAINT ticket_unique UNIQUE (session_id, line, cell);

INSERT INTO users(username, email, phone) VALUES ('Ivanov', 'ivanov@gmail.com', '89034568875');
INSERT INTO users(username, email, phone) VALUES ('Petrov', 'petrov@gmail.com', '89209342289');
INSERT INTO users(username, email, phone) VALUES ('Sidorov', 'sidorov@gmail.com', '89157783455');
INSERT INTO sessions(name) VALUES ('Трансформеры');
INSERT INTO sessions(name) VALUES ('Кошмар на улице Вязов');
INSERT INTO sessions(name) VALUES ('Звездные войны');