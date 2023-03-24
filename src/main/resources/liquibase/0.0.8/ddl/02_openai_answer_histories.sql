--liquibase formatted sql

--changeset illine:0.0.8/ddl/openai_answer_history_seq
--rollback drop sequence openai_telegram_bot.openai_answer_history_seq;
create sequence openai_telegram_bot.openai_answer_history_seq;

--changeset illine:0.0.8/ddl/openai_answer_histories
--rollback drop table openai_telegram_bot.openai_answer_histories;
create table openai_telegram_bot.openai_answer_histories
(
    id               bigint                      default nextval('openai_telegram_bot.openai_answer_history_seq') not null
        constraint openai_answer_history_pk primary key,
    telegram_user_id bigint
        constraint telegram_users_id_fk
            references openai_telegram_bot.telegram_users (id)                                                    not null,
    telegram_chat_id bigint                                                                                       not null,
    openai_answer    text                                                                                         not null,
    created          timestamp(0) with time zone default now()                                                    not null
);

comment on table openai_telegram_bot.openai_answer_histories is 'Table stores OpenAIs answers';
comment on column openai_telegram_bot.openai_answer_histories.id is 'Primary key of the table';
comment on column openai_telegram_bot.openai_answer_histories.telegram_user_id is 'Reference on linked a user by id';
comment on column openai_telegram_bot.openai_answer_histories.telegram_chat_id is 'Unique id of a Telegram chat';
comment on column openai_telegram_bot.openai_answer_histories.openai_answer is 'OpenAIs answer';
comment on column openai_telegram_bot.openai_answer_histories.created is 'Date and time of created';