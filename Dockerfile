FROM openjdk:17-alpine

ARG APP_HOME=/opt/openai-telegram-bot
ARG APP_JAR=openai-telegram-bot.jar

ENV TZ=Europe/Moscow \
    HOME=$APP_HOME \
    JAR=$APP_JAR

WORKDIR $HOME
COPY build/libs/openai-telegram-bot.jar $HOME/$JAR
ENTRYPOINT java $JAVA_OPTS -jar $JAR