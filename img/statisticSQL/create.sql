/*
 * create table for collecting testset statistics
 */

-- tablature/historic (mensural, chant)/modern
create table notationtype (
  nr        varchar(3)    primary key,
  name      varchar(250)
);

-- music typesetting program
create table notationprogram (
  nr        varchar(3)    primary key,
  name      varchar(250),
  author    varchar(250),
  url       varchar(250)
);

-- the actual samples
create table musicsample (
  nr        varchar(3) primary key,
  filename  varchar(250),
  typenr    varchar(3) references notationtype(nr),
  staffs    int,
  programnr varchar(3) references notationprogram(nr)
);
