create table site_page
(
    id bigint not null
        constraint site_page_pkey
            primary key,
    created_at timestamp not null,
    updated_at timestamp not null,
    website integer not null,
    identifier varchar(30),
    language varchar(2),
    title varchar(255)
);

alter table site_page owner to kekeek;
