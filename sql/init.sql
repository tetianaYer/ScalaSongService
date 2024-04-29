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
