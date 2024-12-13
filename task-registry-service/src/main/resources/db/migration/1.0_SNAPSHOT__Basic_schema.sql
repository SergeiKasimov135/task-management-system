create schema if not exists registry;

create table registry.t_user(
    id bigserial primary key,
    c_email varchar(255) check ( c_email ~ '^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$' ),
    c_username varchar(255) check ( length(trim(c_username)) > 0 ),
    c_password varchar(255) check ( length(trim(c_password)) >= 8 ),
    c_role varchar(28)
);

create table registry.t_task(
    id bigserial primary key,
    c_title varchar(70) check ( length(trim(c_title)) >= 1 and length(trim(c_title)) <= 70 ),
    c_description text,
    c_status varchar(28),
    c_priority varchar(28),
    c_comments text,
    с_author_id bigint references registry.t_user(id),
    с_assignee_id bigint references registry.t_user(id)
);
