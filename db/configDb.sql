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
    type varchar(10),
    type_mapping_id integer,
    owner_id integer NOT NULL,
    created_at timestamp without time zone NOT NULL,
	enabled boolean NOT NULL DEFAULT true,
    title varchar(50)
);

CREATE TABLE relationships (
    id serial primary key,
    sender_id integer NOT NULL,
    receiver_id integer NOT NULL,
    confirmed boolean NOT NULL DEFAULT false,
    type varchar(10),
    created_at timestamp without time zone NOT NULL
);

CREATE TABLE user_details (
    id serial primary key,
    user_id integer NOT NULL,
    name varchar(20),
    nickname varchar(10),
    age integer,
    gender varchar(1),
    location varchar(10),
    whats_up varchar(200)
);

CREATE TABLE Feeds (
    id serial primary key,
    body varchar(400),
    owner_id integer NOT NULL,
    enabled boolean NOT NULL DEFAULT true,
    created_at timestamp without time zone NOT NULL
);

CREATE TABLE comments (
	id serial primary key,
	body varchar(400),
	mentioned_user_id integer,
	type varchar(10),
	type_mapping_id integer,
	owner_id integer NOT NULL,
	enabled boolean NOT NULL DEFAULT true,
	created_at timestamp without time zone NOT NULL
);
