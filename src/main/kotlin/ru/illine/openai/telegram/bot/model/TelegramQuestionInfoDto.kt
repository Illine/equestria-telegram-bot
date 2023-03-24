package ru.illine.openai.telegram.bot.model

data class TelegramQuestionInfoDto(

    var telegramUserUsername: String,

    val telegramChatId: Long,

    val telegramMessageId: Long,

    val telegramUserQuestion: String

)
