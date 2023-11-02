package ru.illine.openai.telegram.bot.config.property

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.NotEmpty

@Validated
@ConfigurationProperties("telegram.bot")
data class TelegramBotProperties(

    @NotEmpty
    val token: String
)
