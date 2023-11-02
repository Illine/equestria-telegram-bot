package ru.illine.openai.telegram.bot.model.dto

import ru.illine.openai.telegram.bot.model.entity.TelegramUserQuestionHistoryEntity
import java.time.OffsetDateTime

data class InternalTelegramUserQuestionHistoryDto(
    val id: Long? = null,

    var telegramUser: InnerTelegramUserDto,

    val telegramChatId: Long,

    val telegramMessageId: Long,

    val telegramUserQuestion: String,

    val created: OffsetDateTime? = null
) {

    fun toEntity() = TelegramUserQuestionHistoryEntity(
        id = id,
        telegramUser = telegramUser.toEntity(),
        telegramChatId = telegramChatId,
        telegramMessageId = telegramMessageId,
        telegramUserQuestion = telegramUserQuestion
    )
}
