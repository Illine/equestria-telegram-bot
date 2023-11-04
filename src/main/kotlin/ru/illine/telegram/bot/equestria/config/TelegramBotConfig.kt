package ru.illine.telegram.bot.equestria.config

import org.apache.http.conn.ssl.NoopHostnameVerifier
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClientBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession
import org.zalando.logbook.httpclient.LogbookHttpRequestInterceptor
import org.zalando.logbook.httpclient.LogbookHttpResponseInterceptor
import ru.illine.telegram.bot.equestria.service.telegram.EquestriaTelegramBot
import ru.illine.telegram.bot.equestria.util.TelegramBotHelper
import java.util.concurrent.TimeUnit


@Configuration
class TelegramBotConfig {

    @Bean
    fun telegramHttpClient(
        telegramLogbookRequestInterceptor: LogbookHttpRequestInterceptor,
        telegramLogbookResponseInterceptor: LogbookHttpResponseInterceptor
    ): CloseableHttpClient {
        return HttpClientBuilder.create()
            .setSSLHostnameVerifier(NoopHostnameVerifier())
            .setConnectionTimeToLive(70, TimeUnit.SECONDS)
            .setMaxConnTotal(100)
            .addInterceptorFirst(telegramLogbookRequestInterceptor)
            .addInterceptorFirst(telegramLogbookResponseInterceptor)
            .build()
    }

    @Bean
    fun telegramBotApi(
        bots: Collection<TelegramLongPollingBot>,
        telegramHttpClient: CloseableHttpClient
    ): TelegramBotsApi {
        val api = TelegramBotsApi(DefaultBotSession::class.java)
        bots.onEach { TelegramBotHelper.setBotSessionHttpClient(api.registerBot(it), telegramHttpClient) }
        return api
    }
}