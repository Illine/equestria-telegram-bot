package ru.illine.openai.telegram.bot.service.telegram.impl.command

import com.github.kotlintelegrambot.dispatcher.Dispatcher
import com.github.kotlintelegrambot.dispatcher.command
import org.springframework.stereotype.Service
import ru.illine.openai.telegram.bot.config.property.MessagesProperties
import ru.illine.openai.telegram.bot.model.type.TelegramBotCommandType
import ru.illine.openai.telegram.bot.service.telegram.AbstractTelegramBotCommandService
import ru.illine.openai.telegram.bot.service.telegram.TelegramBotFilterService
import ru.illine.openai.telegram.bot.service.telegram.TelegramMessageHandler
import ru.illine.openai.telegram.bot.util.FunctionHelper.check

@Service
class TelegramBotCommandStartImpl(
    private val messagesProperties: MessagesProperties,
    private val defaultTelegramMessageHandler: TelegramMessageHandler,
    telegramBotFilterService: TelegramBotFilterService
) : AbstractTelegramBotCommandService(telegramBotFilterService) {

    override fun executeCommand(dispatcher: Dispatcher) {
        dispatcher.command(getCommand().command) {
            val access = hasAccess(this.message)
            val chatId = message.chat.id
            access.check(
                ifTrue = {
                    defaultTelegramMessageHandler.sendMessage(
                        bot = bot,
                        chatId = chatId,
                        message = messagesProperties.botStart
                    )
                },
                ifFalse = {
                    defaultTelegramMessageHandler.sendMessage(
                        bot = bot,
                        chatId = chatId,
                        message = messagesProperties.accessError
                    )
                }
            )
        }
    }

    override fun getCommand() = TelegramBotCommandType.START
}
