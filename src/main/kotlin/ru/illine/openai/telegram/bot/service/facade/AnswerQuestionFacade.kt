package ru.illine.openai.telegram.bot.service.facade

interface AnswerQuestionFacade {

    fun buildOpenAIQuestion(sourceQuestion: String, chatId: Long, username: String): String

    fun enrichOpenAIAnswerHistory(newAnswer: String, chatId: Long, username: String)

    fun clearOpenAIAnswerHistory(chatId: Long)

    fun askOpenAI(question: String): String

    fun getLastTelegramUserMessage(chatId: Long): Pair<String, Long>?

    fun saveLastTelegramUserMessage(
        message: String,
        chatId: Long,
        messageId: Long,
        username: String
    )

    fun clearOldTelegramUserMessages(chatId: Long)
}
