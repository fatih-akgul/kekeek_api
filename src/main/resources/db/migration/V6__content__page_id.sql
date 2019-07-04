
ALTER TABLE content ADD COLUMN page_id int8;

alter table content
    add constraint FKngj2ma1r0oie2xrq602oapj2i
        foreign key (page_id)
            references site_page
            on delete cascade;
