package ru.illine.openai.telegram.bot.config

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.illine.openai.telegram.bot.config.property.TelegramBotProperties
import ru.illine.openai.telegram.bot.service.telegram.TelegramBotService
import java.util.concurrent.ConcurrentHashMap

@Configuration
class TelegramBotConfig {

    @Bean
    fun chatToTelegramMessage() = ConcurrentHashMap<Long, String>()

    @Bean(destroyMethod = "stopPolling")
    fun bot(properties: TelegramBotProperties, telegramBotService: TelegramBotService): Bot {
        val bot = bot {
            token = properties.token
            logLevel = properties.logLevel.logLevel
            timeout = properties.timeoutInSec

            dispatch {
                telegramBotService.message(this)
                telegramBotService.command(this)
                telegramBotService.default(this)
            }
        }

        telegramBotService.menu(bot)
        bot.startPolling()
        return bot
    }
}
