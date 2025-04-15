CREATE DATABASE songsdb;
\c songsdb;

CREATE TABLE songs (
                       id uuid NOT NULL,
                       length float NOT NULL,
                       title text NOT NULL,
                       artist text NOT NULL
);

ALTER TABLE songs
ADD CONSTRAINT songs_pkey PRIMARY KEY (id);

CREATE TABLE users (
                       id uuid NOT NULL,
                       name text NOT NULL,
                       age numeric,
                       song_id uuid,
                       FOREIGN KEY (song_id) REFERENCES songs(id)
);
ALTER TABLE users
ADD CONSTRAINT users_pkey PRIMARY KEY (id);
