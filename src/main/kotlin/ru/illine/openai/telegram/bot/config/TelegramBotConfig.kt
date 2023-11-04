package ru.illine.openai.telegram.bot.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession


@Configuration
class TelegramBotConfig {

    @Bean
    fun telegramBotApi(bots: Collection<TelegramLongPollingBot>): TelegramBotsApi {
        val api = TelegramBotsApi(DefaultBotSession::class.java)
        bots.onEach { api.registerBot(it) }
        return api
    }

}