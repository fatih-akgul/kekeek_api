create table content_page
(
    id bigint not null
        constraint content_page_pkey
            primary key,
    created_at timestamp not null,
    updated_at timestamp not null,
    website integer not null,
    content_text text,
    identifier varchar(30),
    language varchar(2),
    page_number integer,
    title varchar(255),
    content_id bigint not null
        constraint fks38qsrl3fr9wyrlaknfa5pap5
            references content
            on delete cascade
);

alter table content_page owner to kekeek;
