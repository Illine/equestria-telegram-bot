package ru.illine.openai.telegram.bot.service.facade.impl

import org.springframework.stereotype.Service
import ru.illine.openai.telegram.bot.service.facade.AnswerQuestionFacade
import ru.illine.openai.telegram.bot.service.openai.OpenAIAnswerService
import ru.illine.openai.telegram.bot.service.telegram.TelegramQuestionService

@Service
class AnswerQuestionFacadeImpl(
    private val telegramQuestionService: TelegramQuestionService,
    private val openAIAnswerService: OpenAIAnswerService
) : AnswerQuestionFacade {

    override fun buildOpenAIQuestion(sourceQuestion: String, chatId: Long) = openAIAnswerService.buildQuestion(sourceQuestion, chatId)

    override fun enrichOpenAIAnswerHistory(newAnswer: String, chatId: Long) {
        openAIAnswerService.enrichAnswerHistory(newAnswer, chatId)
    }

    override fun clearOpenAIAnswerHistory(chatId: Long) {
        openAIAnswerService.clearAnswerHistory(chatId)
    }

    override fun getLastTelegramUserMessage(chatId: Long) = telegramQuestionService.getLastUserMessage(chatId)

    override fun saveLastTelegramUserMessage(message: String, chatId: Long, messageId: Long) {
        telegramQuestionService.saveLastUserMessage(chatId, message to messageId)
    }
}
