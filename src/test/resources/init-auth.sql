create table USERS (
	USERNAME 	varchar(255) primary key,
	PASS  		varchar(255) not null,
	ROLE        varchar(16) not null
);

insert into users values ('user', 'pass', 'USER');