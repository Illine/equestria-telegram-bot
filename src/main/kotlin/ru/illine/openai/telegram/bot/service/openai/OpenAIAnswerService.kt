package ru.illine.openai.telegram.bot.service.openai

interface OpenAIAnswerService {

    fun buildQuestion(sourceQuestion: String, chatId: Long): String

    fun enrichAnswerHistory(newAnswer: String, chatId: Long)

    fun clearAnswerHistory(chatId: Long)
}
