package ru.illine.openai.telegram.bot.service.telegram

import com.github.kotlintelegrambot.entities.Message

abstract class AbstractTelegramBotCommandService(
    protected val telegramBotFilterService: TelegramBotFilterService
) : TelegramBotCommandService {

    protected open fun hasAccess(message: Message) = telegramBotFilterService.userFilter().checkFor(message)
}
