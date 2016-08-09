CREATE TABLE users (
    id serial primary key,
    username varchar(32) NOT NULL,
    password varchar(32) NOT NULL,
    auth_token varchar(255),
    veri_token varchar(255),
    created_at timestamp without time zone NOT NULL,
    email_confirmed boolean NOT NULL DEFAULT false,
    salt varchar(32) NOT NULL,
    timezone_offset integer NOT NULL DEFAULT 0
);

CREATE TABLE images (
    id serial primary key,
    location varchar(50) NOT NULL,
    type varchar(20),
    owner_id integer NOT NULL,
    created_at timestamp without time zone NOT NULL,
	enabled boolean NOT NULL DEFAULT true,
    title varchar(50)
);

CREATE TABLE relationships (
    id serial primary key,
    sender_id integer NOT NULL,
    receiver_id integer NOT NULL,
    accepted boolean NOT NULL DEFAULT false,
    created_at timestamp without time zone NOT NULL
);