DROP TABLE IF EXISTS public.statistics;

CREATE TABLE public.statistics (
	id bigint NOT NULL GENERATED BY DEFAULT AS IDENTITY,
	app varchar NOT NULL,
	uri varchar NOT NULL,
	ip varchar NOT NULL,
	timestamp timestamp without time zone NOT NULL,
	CONSTRAINT statistics_pk PRIMARY KEY (id)
);
