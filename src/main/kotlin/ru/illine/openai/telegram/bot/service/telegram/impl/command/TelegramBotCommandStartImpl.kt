package ru.illine.openai.telegram.bot.service.telegram.impl.command

import com.github.kotlintelegrambot.dispatcher.Dispatcher
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.ParseMode
import org.springframework.stereotype.Service
import ru.illine.openai.telegram.bot.config.property.MessagesProperties
import ru.illine.openai.telegram.bot.model.type.TelegramBotCommandType
import ru.illine.openai.telegram.bot.service.telegram.TelegramBotCommandService

@Service
class TelegramBotCommandStartImpl(
    private val messagesProperties: MessagesProperties,
) : TelegramBotCommandService {

    override fun executeCommand(dispatcher: Dispatcher) {
        dispatcher.command(getCommand().command) {
            bot.sendMessage(
                chatId = ChatId.fromId(update.message!!.chat.id),
                text = messagesProperties.botStart,
                parseMode = ParseMode.MARKDOWN
            )
        }
    }

    override fun getCommand() = TelegramBotCommandType.START
}