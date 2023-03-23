package ru.illine.openai.telegram.bot.service.facade

interface AnswerQuestionFacade {

    fun buildOpenAIQuestion(sourceQuestion: String, chatId: Long): String

    fun enrichOpenAIAnswerHistory(newAnswer: String, chatId: Long)

    fun clearOpenAIAnswerHistory(chatId: Long)

    fun getLastTelegramUserMessage(chatId: Long): String?

    fun saveLastTelegramUserMessage(message: String, chatId: Long)
}
