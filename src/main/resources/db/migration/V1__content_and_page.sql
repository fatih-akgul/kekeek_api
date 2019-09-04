create sequence hibernate_sequence start 1 increment 1;

create table content (
                         id int8 not null,
                         created_at timestamp not null,
                         updated_at timestamp not null,
                         content_text text,
                         description text,
                         identifier varchar(30),
                         image varchar(255),
                         image_description varchar(255),
                         language varchar(2),
                         page_number int4,
                         snippet text,
                         title varchar(255),
                         page_id int8 not null,
                         primary key (id)
);

create table page (
                      id int8 not null,
                      created_at timestamp not null,
                      updated_at timestamp not null,
                      comment_count int4,
                      content_type varchar(255),
                      description text,
                      identifier varchar(30),
                      image varchar(255),
                      image_description varchar(255),
                      language varchar(2),
                      like_count int4,
                      page_count int4,
                      sequence int4,
                      snippet text,
                      status varchar(255),
                      title varchar(255),
                      primary key (id)
);

create table page_keywords (
                               page_id int8 not null,
                               keywords varchar(255)
);

create table page_hierarchy (
                                child_id int8 not null,
                                parent_id int8 not null,
                                sequence int4,
                                primary key (child_id, parent_id)
);

alter table content
    add constraint UKjo05cur2yp8cqavpkyp6uaejr unique (page_id, identifier);

alter table page
    add constraint UK_q8ws0op4yqlviv1ydissfs8un unique (identifier);

alter table content
    add constraint FK1lv6h7cjxydrlm7fjy8ncp75k
        foreign key (page_id)
            references page
            on delete cascade;

alter table page_keywords
    add constraint FK3m3fyc9nv5ecwa74feaubeb3q
        foreign key (page_id)
            references page;

alter table page_hierarchy
    add constraint FKkjc14sfbte2rsi1w3ix37i8ps
        foreign key (child_id)
            references page;

alter table page_hierarchy
    add constraint FKcc1hjpe1uynbk94tjuyj0b4af
        foreign key (parent_id)
            references page;
