package ru.illine.telegram.bot.equestria

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import ru.illine.telegram.bot.equestria.config.property.AsyncProperties
import ru.illine.telegram.bot.equestria.config.property.LogbookProperties
import ru.illine.telegram.bot.equestria.config.property.GptProperties
import ru.illine.telegram.bot.equestria.config.property.TelegramBotProperties

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
