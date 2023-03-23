package ru.illine.openai.telegram.bot.service.telegram

import com.github.kotlintelegrambot.Bot
import ru.illine.openai.telegram.bot.model.type.TelegramHandlerType

interface TelegramMessageHandler {

    fun sendMessage(bot: Bot, chatId: Long, message: String, sourceMessageId: Long? = null)

    fun getType(): TelegramHandlerType
}
