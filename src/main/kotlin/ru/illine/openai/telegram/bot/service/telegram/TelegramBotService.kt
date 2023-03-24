package ru.illine.openai.telegram.bot.service.telegram

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.dispatcher.Dispatcher

interface TelegramBotService {

    fun createMenu(bot: Bot)

    fun askOpenAI(dispatcher: Dispatcher)

    fun performCommand(dispatcher: Dispatcher)

    fun getDefault(dispatcher: Dispatcher)
}
