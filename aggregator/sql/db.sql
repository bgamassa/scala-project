-- DROP TABLE IF EXISTS public.report CASCADE;
CREATE TABLE public.report (
	id serial NOT NULL,
	"from" text NOT NULL,
	date timestamp NOT NULL,
	gps_fix smallint,
	latitude float,
	longitude float,
	altitude float,
	temperature smallint,
	battery smallint,
	extra text,
	CONSTRAINT report_pk PRIMARY KEY (id)

);
