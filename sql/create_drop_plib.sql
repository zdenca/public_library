drop sequence book_seq;
drop sequence author_seq;
drop table books_to_authors;
drop table authors;
drop table books;


create table books
(id number not null,
title varchar(256) not null,
isbn varchar(13) not null,
issue_year number(4),
constraint pk_book_id primary key (id));

create table authors
(id number not null,
first_name varchar(100),
last_name varchar(256),
constraint pk_author_id primary key (id));

create table books_to_authors
(book_id number not null,
author_id number not null,
constraint pk_book_author_id primary key (book_id, author_id),
constraint fk_book_id foreign key (book_id)
references books (id),
constraint fk_author_id foreign key(author_id)
references authors(id)
);

create sequence book_seq start with 1
increment by 1 nocache nocycle;

create sequence author_seq start with 1
increment by 1 nocache nocycle;