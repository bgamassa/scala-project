CREATE KEYSPACE brain_implant WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 };

use brain_implant;

drop table if exists report;
create table report (
    "from" text,
    date text,
    gps_fix int,
    latitude float,
    longitude float,
    altitude float,
    temperature float,
    battery float,
    happiness_level int,
    anger_level int,
    stress_level int,
    extra text,
    PRIMARY KEY ((altitude, "from"), temperature))
    WITH CLUSTERING ORDER BY (temperature ASC);

