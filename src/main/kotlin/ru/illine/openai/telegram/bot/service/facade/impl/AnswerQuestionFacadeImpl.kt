package ru.illine.openai.telegram.bot.service.facade.impl

import org.springframework.stereotype.Service
import ru.illine.openai.telegram.bot.model.TelegramQuestionInfoDto
import ru.illine.openai.telegram.bot.service.facade.AnswerQuestionFacade
import ru.illine.openai.telegram.bot.service.openai.OpenAIAnswerService
import ru.illine.openai.telegram.bot.service.openai.OpenAIService
import ru.illine.openai.telegram.bot.service.telegram.TelegramQuestionService

@Service
class AnswerQuestionFacadeImpl(
    private val telegramQuestionService: TelegramQuestionService,
    private val openAIAnswerService: OpenAIAnswerService,
    private val openAIService: OpenAIService
) : AnswerQuestionFacade {

    override fun buildOpenAIQuestion(sourceQuestion: String, chatId: Long, username: String): String {
        return openAIAnswerService.buildQuestion(sourceQuestion, chatId)
    }

    override fun enrichOpenAIAnswerHistory(newAnswer: String, newQuestion: String, chatId: Long, username: String) {
        openAIAnswerService.enrichAnswerHistory(newAnswer, newQuestion, chatId, username)
    }

    override fun clearOpenAIAnswerHistory(chatId: Long) {
        openAIAnswerService.clearAnswerHistory(chatId)
    }

    override fun askOpenAI(question: String) = openAIService.chatSingleAnswer(question)

    override fun getLastTelegramUserMessage(chatId: Long) = telegramQuestionService.getLastUserMessage(chatId)

    override fun saveLastTelegramUserMessage(
        message: String,
        chatId: Long,
        messageId: Long,
        username: String
    ) {
        telegramQuestionService.saveLastUserMessage(
            chatId,
            TelegramQuestionInfoDto(
                telegramUserUsername = username,
                telegramChatId = chatId,
                telegramMessageId = messageId,
                telegramUserQuestion = message
            )
        )
    }

    override fun clearOldTelegramUserMessages(chatId: Long) {
        telegramQuestionService.clearOldUserMessages(chatId)
    }
}
