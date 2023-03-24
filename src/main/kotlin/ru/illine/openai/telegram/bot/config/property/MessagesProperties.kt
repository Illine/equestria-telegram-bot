package ru.illine.openai.telegram.bot.config.property

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.NotEmpty

@Validated
@ConfigurationProperties("messages")
data class MessagesProperties(

    @NotEmpty
    val botStart: String,

    @NotEmpty
    val openaiError: String,

    @NotEmpty
    val telegramError: String,

    @NotEmpty
    val accessError: String,

    @NotEmpty
    val repeatCommandError: String,

    @NotEmpty
    val wrongMessageTypeError: String,

    @NotEmpty
    val unknownError: String,

    @NotEmpty
    val addUserCommandEmptyError: String,

    @NotEmpty
    val addUserCommandSaveError: String,

    @NotEmpty
    val addUserCommandFilterError: String,

    @NotEmpty
    val addUserCommandMuchMoreError: String,

    @NotEmpty
    val unknownCommand: String,

    @NotEmpty
    val clearCommand: String,

    @NotEmpty
    val addUserCommand: String,

    @NotEmpty
    val startCommandDescription: String,

    @NotEmpty
    val clearCommandDescription: String,

    @NotEmpty
    val versionCommandDescription: String,

    @NotEmpty
    val repeatCommandDescription: String,

    @NotEmpty
    val getUsersCommandDescription: String,

    @NotEmpty
    val addUserCommandDescription: String,

    @NotEmpty
    val removeUserCommandDescription: String,

    @NotEmpty
    val unknownCommandDescription: String

)
