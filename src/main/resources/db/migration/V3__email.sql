
create table email (
   id int8 not null,
   created_at timestamp not null,
   updated_at timestamp not null,
   from_email varchar(255),
   from_name varchar(255),
   message text,
   sender_email varchar(255),
   sender_ip varchar(40),
   sender_name varchar(255),
   subject varchar(1023),
   success boolean,
   to_email varchar(255),
   primary key (id)
);
