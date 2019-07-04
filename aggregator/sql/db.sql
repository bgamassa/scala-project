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
	happiness_level smallint,
	anger_level smallint,
	stress_level smallint,
	extra text,
	CONSTRAINT report_pk PRIMARY KEY (id)

);
