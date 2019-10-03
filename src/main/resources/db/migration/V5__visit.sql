create table visit (
    id int8 not null,
    created_at timestamp not null,
    updated_at timestamp not null,
    counter int4,
    identifier varchar(63),
    primary key (id)
);
