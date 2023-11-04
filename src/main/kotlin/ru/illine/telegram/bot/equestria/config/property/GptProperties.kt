package ru.illine.telegram.bot.equestria.config.property

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.*

@Validated
@ConfigurationProperties("gpt")
data class GptProperties(

    @NotEmpty
    val token: String,

    @DecimalMin("0.1")
    @DecimalMax("1.0")
    val temperature: Double,

    @Min(3)
    @Max(10)
    val answerHistoryCount: Int,

    @NotNull
    val http: Http

) {
    data class Http(
        @Min(30)
        @Max(300)
        val timeoutInSec: Long,

        @Min(1)
        @Max(5)
        val keepAliveInSec: Long,

        @Min(2)
        @Max(5)
        val maxIdleConnections: Int
    )
}
