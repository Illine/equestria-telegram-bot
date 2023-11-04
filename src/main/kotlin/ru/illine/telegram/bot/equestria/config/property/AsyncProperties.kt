package ru.illine.telegram.bot.equestria.config.property

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.Max
import javax.validation.constraints.Min

@Validated
@ConfigurationProperties("async")
data class AsyncProperties(

    @Min(2)
    @Max(15)
    val defaultPoolSize: Int

)
