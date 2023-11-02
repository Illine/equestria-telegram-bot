package ru.illine.openai.telegram.bot.model.dto

import ru.illine.openai.telegram.bot.model.entity.OpenAIAnswerHistoryEntity
import java.time.OffsetDateTime

data class InnerOpenAIAnswerHistoryDto(
    val id: Long? = null,

    var telegramUser: InnerTelegramUserDto,

    val telegramChatId: Long,

    val openAIAnswer: String,

    val openAIQuestion: String,

    val created: OffsetDateTime? = null
) {

    fun toEntity() = OpenAIAnswerHistoryEntity(
        id = id,
        telegramUser = telegramUser.toEntity(),
        telegramChatId = telegramChatId,
        openAIAnswer = openAIAnswer,
        openAIQuestion = openAIQuestion
    )
}
