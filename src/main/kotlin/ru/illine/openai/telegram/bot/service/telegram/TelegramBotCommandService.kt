package ru.illine.openai.telegram.bot.service.telegram

import com.github.kotlintelegrambot.dispatcher.Dispatcher
import ru.illine.openai.telegram.bot.model.type.TelegramBotCommandType

interface TelegramBotCommandService {

    fun executeCommand(dispatcher: Dispatcher)

    fun getCommand(): TelegramBotCommandType
}
