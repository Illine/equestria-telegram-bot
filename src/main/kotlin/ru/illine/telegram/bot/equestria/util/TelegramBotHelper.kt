package ru.illine.telegram.bot.equestria.util

import org.apache.http.client.HttpClient
import org.springframework.util.ReflectionUtils
import org.telegram.telegrambots.meta.generics.BotSession

class TelegramBotHelper {

    companion object {
        private val READER_THREAD_FIELD_NAME = "readerThread"
        private val HTTP_CLIENT_FIELD_NAME = "httpclient"

        fun setBotSessionHttpClient(
            session: BotSession,
            httpClient: HttpClient
        ) {
            val readerThreadField = session.javaClass.getDeclaredField(READER_THREAD_FIELD_NAME)
            readerThreadField.trySetAccessible()
            val readerThread = ReflectionUtils.getField(readerThreadField, session)

            val httpclientField = readerThread!!.javaClass.getDeclaredField(HTTP_CLIENT_FIELD_NAME)
            httpclientField.trySetAccessible()
            ReflectionUtils.setField(httpclientField, readerThread, httpClient)
        }
    }
}
