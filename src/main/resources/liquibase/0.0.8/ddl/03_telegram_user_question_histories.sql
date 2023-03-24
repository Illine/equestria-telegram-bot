--liquibase formatted sql

--changeset illine:0.0.8/ddl/telegram_user_question_history_seq
--rollback drop sequence openai_telegram_bot.telegram_user_question_history_seq;
create sequence openai_telegram_bot.telegram_user_question_history_seq;

--changeset illine:0.0.8/ddl/telegram_user_question_histories
--rollback drop table openai_telegram_bot.telegram_user_question_histories;
create table openai_telegram_bot.telegram_user_question_histories
(
    id                     bigint                      default nextval('openai_telegram_bot.telegram_user_question_history_seq') not null
        constraint telegram_history_pk primary key,
    telegram_user_id       bigint
        constraint telegram_users_id_fk
            references openai_telegram_bot.telegram_users (id)                                                                   not null,
    telegram_chat_id       bigint                                                                                                not null,
    telegram_message_id    bigint                                                                                                not null,
    telegram_user_question text                                                                                                  not null,
    created                timestamp(0) with time zone default now()                                                             not null
);

comment on table openai_telegram_bot.telegram_user_question_histories is 'Table stores a message history of Telegram chat';
comment on column openai_telegram_bot.telegram_user_question_histories.id is 'Primary key of the table';
comment on column openai_telegram_bot.telegram_user_question_histories.telegram_user_id is 'Reference on linked a user by id';
comment on column openai_telegram_bot.telegram_user_question_histories.telegram_chat_id is 'Unique id of a Telegram chat';
comment on column openai_telegram_bot.telegram_user_question_histories.telegram_message_id is 'Unique id of a message in a Telegram chat';
comment on column openai_telegram_bot.telegram_user_question_histories.telegram_user_question is 'Text of a user message';
comment on column openai_telegram_bot.telegram_user_question_histories.created is 'Date and time of created';