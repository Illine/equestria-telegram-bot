package ru.illine.openai.telegram.bot.dao.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.illine.openai.telegram.bot.dao.entity.OpenAIAnswerHistoryEntity

interface OpenAIAnswerHistoryRepository : JpaRepository<OpenAIAnswerHistoryEntity, Long> {

    fun findAllByTelegramChatId(chatId: Long): Set<OpenAIAnswerHistoryEntity>

    fun deleteAllByTelegramChatId(chatId: Long)
}
