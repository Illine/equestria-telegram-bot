--liquibase formatted sql

--changeset illine:0.0.9/ddl/openai_answer_histories
--rollback alter table openai_telegram_bot.openai_answer_histories drop column openai_question;
alter table openai_telegram_bot.openai_answer_histories
    add openai_question text default '' not null;

comment on column openai_telegram_bot.openai_answer_histories.openai_question is 'A question which was asked OpenAI';