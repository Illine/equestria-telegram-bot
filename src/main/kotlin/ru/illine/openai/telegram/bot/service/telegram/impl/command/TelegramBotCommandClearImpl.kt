package ru.illine.openai.telegram.bot.service.telegram.impl.command

import com.github.kotlintelegrambot.dispatcher.Dispatcher
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.entities.ChatId
import org.springframework.stereotype.Service
import ru.illine.openai.telegram.bot.config.property.MessagesProperties
import ru.illine.openai.telegram.bot.model.type.TelegramBotCommandType
import ru.illine.openai.telegram.bot.service.openai.OpenAIAnswerService
import ru.illine.openai.telegram.bot.service.telegram.TelegramBotCommandService

@Service
class TelegramBotCommandClearImpl(
    private val messagesProperties: MessagesProperties,
    private val openAIAnswerService: OpenAIAnswerService,
) : TelegramBotCommandService {

    override fun executeCommand(dispatcher: Dispatcher) {
        dispatcher.command(getCommand().command) {
            openAIAnswerService.clearAnswerHistory(update.message!!.chat.id)
            bot.sendMessage(
                chatId = ChatId.fromId(update.message!!.chat.id),
                text = messagesProperties.clearCommand
            )
        }
    }

    override fun getCommand() = TelegramBotCommandType.CLEAR
}