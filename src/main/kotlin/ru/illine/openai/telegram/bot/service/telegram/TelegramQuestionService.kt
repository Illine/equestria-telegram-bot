package ru.illine.openai.telegram.bot.service.telegram

import ru.illine.openai.telegram.bot.model.TelegramQuestionInfoDto

interface TelegramQuestionService {

    fun getLastUserMessage(chatId: Long): Pair<String, Long>?

    fun saveLastUserMessage(chatId: Long, telegramQuestionInfo: TelegramQuestionInfoDto)

    fun clearOldUserMessages(chatId: Long)
}
