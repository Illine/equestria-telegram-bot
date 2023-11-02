package ru.illine.openai.telegram.bot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import ru.illine.openai.telegram.bot.config.property.LogbookProperties
import ru.illine.openai.telegram.bot.config.property.OpenAIProperties
import ru.illine.openai.telegram.bot.config.property.TelegramBotProperties

@EnableConfigurationProperties(
    *[
        TelegramBotProperties::class,
        OpenAIProperties::class,
        LogbookProperties::class,
    ]
)
@SpringBootApplication
class OpenaiTelegramBotApplication

fun main(args: Array<String>) {
    runApplication<OpenaiTelegramBotApplication>(*args)
}
