package ru.illine.openai.telegram.bot.service.telegram.bot

import org.slf4j.LoggerFactory
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import ru.illine.openai.telegram.bot.config.property.TelegramBotProperties
import ru.illine.openai.telegram.bot.model.TelegramBotType

abstract class AbstractTelegramBot(
    telegramBotProperties: TelegramBotProperties,
) : TelegramLongPollingBot(telegramBotProperties.token) {

    protected val log = LoggerFactory.getLogger("BOT")

    override fun getBotUsername() = getType().name

    abstract fun getType(): TelegramBotType

}