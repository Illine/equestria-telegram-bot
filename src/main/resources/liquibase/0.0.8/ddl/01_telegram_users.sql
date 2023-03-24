--liquibase formatted sql

--changeset illine:0.0.8/ddl/user_seq
--rollback drop sequence openai_telegram_bot.telegram_user_seq;
create sequence openai_telegram_bot.telegram_user_seq;

--changeset illine:0.0.8/ddl/telegram_users
--rollback drop table openai_telegram_bot.telegram_users;
create table openai_telegram_bot.telegram_users
(
    id            bigint  default nextval('openai_telegram_bot.telegram_user_seq') not null
        constraint telegram_users_pk primary key,
    username      varchar(32)                                                      not null,
    administrator boolean default false                                            not null,
    deleted       boolean default false                                            not null
);

create unique index telegram_users_username_unique_index
    on openai_telegram_bot.telegram_users (username);

comment on table openai_telegram_bot.telegram_users is 'Table stores telegram users';
comment on column openai_telegram_bot.telegram_users.id is 'Primary key of the table';
comment on column openai_telegram_bot.telegram_users.username is 'Username from Telegram, it is unique';
comment on column openai_telegram_bot.telegram_users.administrator is 'Admin flag';
comment on column openai_telegram_bot.telegram_users.deleted is 'Soft delete field, if true then a user is deleted';