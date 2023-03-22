package ru.illine.openai.telegram.bot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import ru.illine.openai.telegram.bot.config.property.*

@EnableConfigurationProperties(
    *[
        TelegramBotProperties::class,
        OpenAIProperties::class,
        MessagesProperties::class,
        LogbookProperties::class,
        VersionProperties::class
    ]
)
@SpringBootApplication
class OpenaiTelegramBotApplication

fun main(args: Array<String>) {
    runApplication<OpenaiTelegramBotApplication>(*args)
}
