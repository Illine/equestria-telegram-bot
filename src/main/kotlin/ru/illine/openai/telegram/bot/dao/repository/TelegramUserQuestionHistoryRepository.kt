package ru.illine.openai.telegram.bot.dao.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.illine.openai.telegram.bot.dao.entity.TelegramUserQuestionHistoryEntity

interface TelegramUserQuestionHistoryRepository : JpaRepository<TelegramUserQuestionHistoryEntity, Long> {

    fun findAllByTelegramChatId(chatId: Long): Set<TelegramUserQuestionHistoryEntity>
}
