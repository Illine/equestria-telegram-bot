package ru.illine.openai.telegram.bot.service.telegram

interface TelegramQuestionService {

    fun getLastUserMessage(chatId: Long): Pair<String, Long>?

    fun saveLastUserMessage(chatId: Long, messageToMessageId: Pair<String, Long>)
}
