package ru.illine.openai.telegram.bot.service.telegram.impl

import org.springframework.stereotype.Service
import ru.illine.openai.telegram.bot.dao.access.TelegramUserAccessService
import ru.illine.openai.telegram.bot.dao.access.TelegramUserQuestionHistoryAccessService
import ru.illine.openai.telegram.bot.model.TelegramQuestionInfoDto
import ru.illine.openai.telegram.bot.model.dto.InternalTelegramUserQuestionHistoryDto
import ru.illine.openai.telegram.bot.service.telegram.TelegramQuestionService

@Service
class TelegramQuestionServiceImpl(
    private val telegramUserQuestionHistoryAccessService: TelegramUserQuestionHistoryAccessService,
    private val userAccessService: TelegramUserAccessService
) : TelegramQuestionService {

    override fun getLastUserMessage(chatId: Long): Pair<String, Long>? {
        val lastQuestion = telegramUserQuestionHistoryAccessService.findLastMessageByChatId(chatId)
        return lastQuestion?.let { it.telegramUserQuestion to it.telegramMessageId }
    }

    override fun saveLastUserMessage(chatId: Long, telegramQuestionInfo: TelegramQuestionInfoDto) {
        val telegramUser = userAccessService.findByUsername(telegramQuestionInfo.telegramUserUsername)
        val telegramQuestion = InternalTelegramUserQuestionHistoryDto(
            telegramUser = telegramUser,
            telegramChatId = telegramQuestionInfo.telegramChatId,
            telegramMessageId = telegramQuestionInfo.telegramMessageId,
            telegramUserQuestion = telegramQuestionInfo.telegramUserQuestion
        )
        telegramUserQuestionHistoryAccessService.save(telegramQuestion)
    }

    override fun clearOldUserMessages(chatId: Long) {
        val firstElem = 1
        val oldMessages = telegramUserQuestionHistoryAccessService.findAllByChatId(chatId)
            .sortedByDescending { it.created }
            .drop(firstElem)
            .map { it.id!! }
            .toList()
        telegramUserQuestionHistoryAccessService.deleteAll(oldMessages)
    }
}
