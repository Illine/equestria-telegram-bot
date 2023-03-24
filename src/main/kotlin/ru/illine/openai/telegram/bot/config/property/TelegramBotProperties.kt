package ru.illine.openai.telegram.bot.config.property

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated
import ru.illine.openai.telegram.bot.model.type.TelegramBotLogLevelType
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

@Validated
@ConfigurationProperties("telegram.bot")
data class TelegramBotProperties(

    @NotEmpty
    val token: String,

    @NotNull
    val logLevel: TelegramBotLogLevelType,

    @Min(10)
    @Max(300)
    val timeoutInSec: Int
)
