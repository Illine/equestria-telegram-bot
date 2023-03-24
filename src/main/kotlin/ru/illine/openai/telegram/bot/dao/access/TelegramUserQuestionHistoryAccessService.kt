package ru.illine.openai.telegram.bot.dao.access

import ru.illine.openai.telegram.bot.model.dto.InternalTelegramUserQuestionHistoryDto

interface TelegramUserQuestionHistoryAccessService {

    fun findAllByChatId(chatId: Long): Set<InternalTelegramUserQuestionHistoryDto>

    fun findLastMessageByChatId(chatId: Long): InternalTelegramUserQuestionHistoryDto?

    fun save(dto: InternalTelegramUserQuestionHistoryDto): InternalTelegramUserQuestionHistoryDto

    fun deleteAll(ids: Collection<Long>)
}
