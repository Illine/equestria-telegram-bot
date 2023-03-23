package ru.illine.openai.telegram.bot.config.property

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.NotEmpty

@Validated
@ConfigurationProperties("version")
data class VersionProperties(

    @NotEmpty
    val fileName: String,

    @NotEmpty
    val propertyName: String,

    @NotEmpty
    val description: String
)
