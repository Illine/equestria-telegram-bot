package ru.illine.openai.telegram.bot.service.telegram.impl.command

import com.github.kotlintelegrambot.dispatcher.Dispatcher
import com.github.kotlintelegrambot.entities.ChatId
import org.springframework.stereotype.Service
import ru.illine.openai.telegram.bot.config.property.MessagesProperties
import ru.illine.openai.telegram.bot.handler.UnknownCommandHandler
import ru.illine.openai.telegram.bot.model.type.TelegramBotCommandType
import ru.illine.openai.telegram.bot.service.telegram.TelegramBotCommandService

@Service
class TelegramBotCommandUnknownImpl(
    private val messagesProperties: MessagesProperties,
) : TelegramBotCommandService {

    override fun executeCommand(dispatcher: Dispatcher) {
        dispatcher.addHandler(UnknownCommandHandler {
            bot.sendMessage(
                chatId = ChatId.fromId(update.message!!.chat.id),
                text = messagesProperties.unknownCommand
            )
        })
    }

    override fun getCommand() = TelegramBotCommandType.UNKNOWN
}