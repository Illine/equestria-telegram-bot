package ru.illine.openai.telegram.bot.service.telegram

interface TelegramQuestionService {

    fun getLastUserMessage(chatId: Long): String?

    fun saveLastUserMessage(message: String, chatId: Long)
}