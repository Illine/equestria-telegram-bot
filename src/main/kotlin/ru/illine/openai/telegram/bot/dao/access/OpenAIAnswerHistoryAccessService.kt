package ru.illine.openai.telegram.bot.dao.access

import ru.illine.openai.telegram.bot.model.dto.InnerOpenAIAnswerHistoryDto

interface OpenAIAnswerHistoryAccessService {

    fun findByChatId(chatId: Long): Set<InnerOpenAIAnswerHistoryDto>

    fun save(dto: InnerOpenAIAnswerHistoryDto): InnerOpenAIAnswerHistoryDto

    fun deleteById(id: Long)

    fun deleteAllByChatId(chatId: Long)
}
