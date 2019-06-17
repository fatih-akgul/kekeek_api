create table content
(
    id bigint not null
        constraint content_pkey
            primary key,
    created_at timestamp not null,
    updated_at timestamp not null,
    website integer not null,
    comment_count integer,
    content_location varchar(255),
    content_type integer,
    description text,
    img_full varchar(255),
    identifier varchar(30),
    img_description varchar(255),
    language varchar(2),
    like_count integer,
    page_count integer,
    snippet text,
    status integer,
    img_thumbnail varchar(255),
    title varchar(255),
    parent_content_id bigint
        constraint fkbv2h3w7t132dycoeimpuycyim
            references content
            on delete cascade
);

alter table content owner to kekeek;
