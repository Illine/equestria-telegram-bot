package ru.illine.openai.telegram.bot.service.telegram

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.dispatcher.Dispatcher

interface TelegramBotService {

    fun menu(bot: Bot)

    fun message(dispatcher: Dispatcher)

    fun command(dispatcher: Dispatcher)

    fun default(dispatcher: Dispatcher)

    fun error(dispatcher: Dispatcher)
}
