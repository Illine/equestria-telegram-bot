package ru.illine.openai.telegram.bot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import ru.illine.openai.telegram.bot.config.property.AsyncProperties
import ru.illine.openai.telegram.bot.config.property.LogbookProperties
import ru.illine.openai.telegram.bot.config.property.GptProperties
import ru.illine.openai.telegram.bot.config.property.TelegramBotProperties

@EnableConfigurationProperties(
    *[
        TelegramBotProperties::class,
        GptProperties::class,
        LogbookProperties::class,
        AsyncProperties::class
    ]
)
@SpringBootApplication
class EquestriaTelegramBotApplication

fun main(args: Array<String>) {
    runApplication<EquestriaTelegramBotApplication>(*args)
}
