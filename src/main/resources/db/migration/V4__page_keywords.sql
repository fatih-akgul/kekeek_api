create table page_keywords
(
    page_id bigint not null
        constraint fkdhi38675tnw2oa0ht4yfee5q8
            references site_page,
    keywords varchar(255)
);

alter table page_keywords owner to kekeek;
