DROP TABLE IF EXISTS public.events_compilations;
DROP TABLE IF EXISTS public.compilations;
DROP TABLE IF EXISTS public.requests;
DROP TABLE IF EXISTS public.events;
DROP TABLE IF EXISTS public.users;
DROP TABLE IF EXISTS public.categories;

CREATE TABLE public.users (
	id bigint NOT NULL GENERATED BY DEFAULT AS IDENTITY,
	name varchar NOT NULL,
	email varchar NOT NULL,
	CONSTRAINT users_pk PRIMARY KEY (id),
	CONSTRAINT users_un UNIQUE (email)
);

CREATE TABLE public.categories (
	id bigint NOT NULL GENERATED BY DEFAULT AS IDENTITY,
	name varchar NOT NULL,
	CONSTRAINT categories_pk PRIMARY KEY (id),
	CONSTRAINT categories_un UNIQUE (name)
);

CREATE TABLE public.events (
	id bigint NOT NULL GENERATED BY DEFAULT AS IDENTITY,
	annotation varchar NOT NULL,
	description varchar NOT NULL,
	title varchar NOT NULL,
	created_on timestamp without time zone NOT NULL,
	published_on timestamp without time zone NULL,
	event_date timestamp without time zone NOT NULL,
	category_id bigint NOT NULL,
	initiator_id bigint NOT NULL,
	lat NUMERIC(8,6) NOT NULL,
	lon NUMERIC(9,6) NOT NULL,
	paid boolean NOT NULL,
	participant_limit int DEFAULT 0,
	request_moderation boolean DEFAULT TRUE,
	state varchar NOT NULL,
	CONSTRAINT events_pk PRIMARY KEY (id),
	CONSTRAINT events_users_fk FOREIGN KEY (initiator_id) REFERENCES public.users(id) ON DELETE CASCADE,
	CONSTRAINT events_categories_fk FOREIGN KEY (category_id) REFERENCES public.categories(id)
);

CREATE TABLE public.requests (
	id bigint NOT NULL GENERATED BY DEFAULT AS IDENTITY,
    event_id bigint NOT NULL,
    requester_id bigint NOT NULL,
    created timestamp without time zone NOT NULL,
    status varchar NOT NULL,
	CONSTRAINT requests_pk PRIMARY KEY (id),
	CONSTRAINT requests_users_fk FOREIGN KEY (requester_id) REFERENCES public.users(id) ON DELETE CASCADE,
	CONSTRAINT requests_events_fk FOREIGN KEY (event_id) REFERENCES public.events(id) ON DELETE CASCADE,
    CONSTRAINT requests_requester_event_un UNIQUE (requester_id, event_id)
);

CREATE TABLE public.compilations (
	id bigint NOT NULL GENERATED BY DEFAULT AS IDENTITY,
    title varchar NOT NULL,
    pinned boolean NOT NULL,
	CONSTRAINT compilations_pk PRIMARY KEY (id),
    CONSTRAINT compilations_title_un UNIQUE (title)
);

CREATE TABLE public.events_compilations (
    event_id bigint NOT NULL,
    compilation_id bigint NOT NULL,
	CONSTRAINT events_compilations_pk PRIMARY KEY (event_id, compilation_id),
	CONSTRAINT events_fk FOREIGN KEY (event_id) REFERENCES public.events(id) ON DELETE CASCADE,
	CONSTRAINT compilation_fk FOREIGN KEY (compilation_id) REFERENCES public.compilations(id) ON DELETE CASCADE
);