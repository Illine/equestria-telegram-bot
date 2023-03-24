package ru.illine.openai.telegram.bot.config.property

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.Max
import javax.validation.constraints.Min

@Validated
@ConfigurationProperties("async")
data class AsyncProperties(

    @Min(2)
    @Max(10)
    val openAIPoolSize: Int

)
