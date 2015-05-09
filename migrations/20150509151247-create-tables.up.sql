CREATE TABLE users (
       id serial PRIMARY KEY
);
CREATE TABLE languages (
       id serial PRIMARY KEY,
       name text NOT NULL
);
CREATE TABLE translations (
       id serial PRIMARY KEY,
       user_id integer REFERENCES users (id),
       orig_id integer REFERENCES languages (id) NOT NULL,
       translation_id integer REFERENCES languages (id) NOT NULL,
       word text NOT NULL DEFAULT '',
       trans text NOT NULL DEFAULT ''
);
