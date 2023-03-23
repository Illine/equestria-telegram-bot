package ru.illine.openai.telegram.bot.service.telegram.impl.command

import com.github.kotlintelegrambot.dispatcher.Dispatcher
import org.springframework.stereotype.Service
import ru.illine.openai.telegram.bot.config.property.MessagesProperties
import ru.illine.openai.telegram.bot.handler.UnknownCommandHandler
import ru.illine.openai.telegram.bot.model.type.TelegramBotCommandType
import ru.illine.openai.telegram.bot.service.telegram.TelegramBotCommandService
import ru.illine.openai.telegram.bot.service.telegram.TelegramMessageHandler

@Service
class TelegramBotCommandUnknownImpl(
    private val messagesProperties: MessagesProperties,
    private val defaultTelegramMessageHandler: TelegramMessageHandler
) : TelegramBotCommandService {

    override fun executeCommand(dispatcher: Dispatcher) {
        dispatcher.addHandler(
            UnknownCommandHandler {
                defaultTelegramMessageHandler.sendMessage(bot, update.message!!.chat.id, messagesProperties.unknownCommand)
            }
        )
    }

    override fun getCommand() = TelegramBotCommandType.UNKNOWN
}
