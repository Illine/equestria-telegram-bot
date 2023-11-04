package ru.illine.telegram.bot.equestria.config.property

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated
import java.util.concurrent.TimeUnit
import javax.validation.constraints.DecimalMax
import javax.validation.constraints.DecimalMin
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

@Validated
@ConfigurationProperties("gpt")
data class GptProperties(

    @NotEmpty
    val token: String,

    @DecimalMin("0.1")
    @DecimalMax("1.0")
    val temperature: Double,

    @Min(30)
    @Max(300)
    val timeoutInSec: Int,

    @Min(1)
    @Max(5)
    val keepAliveInSec: Int,

    @NotNull
    val timeUnit: TimeUnit,

    @Min(2)
    @Max(5)
    val maxIdleConnections: Int,

    @Min(3)
    @Max(10)
    val answerHistoryCount: Int
)
