package ru.illine.openai.telegram.bot.service.telegram.impl.command

import com.github.kotlintelegrambot.dispatcher.Dispatcher
import com.github.kotlintelegrambot.dispatcher.command
import org.springframework.stereotype.Service
import ru.illine.openai.telegram.bot.config.property.MessagesProperties
import ru.illine.openai.telegram.bot.config.property.VersionProperties
import ru.illine.openai.telegram.bot.model.type.TelegramBotCommandType
import ru.illine.openai.telegram.bot.service.telegram.AbstractTelegramBotCommandService
import ru.illine.openai.telegram.bot.service.telegram.TelegramBotFilterService
import ru.illine.openai.telegram.bot.service.telegram.TelegramMessageHandler
import ru.illine.openai.telegram.bot.util.FunctionHelper.check
import java.util.*

@Service
class TelegramBotCommandVersionImpl(
    private val properties: VersionProperties,
    private val defaultTelegramMessageHandler: TelegramMessageHandler,
    private val messagesProperties: MessagesProperties,
    telegramBotFilterService: TelegramBotFilterService
) : AbstractTelegramBotCommandService(telegramBotFilterService) {

    private val versionProperties = Properties()

    init {
        versionProperties.load(this.javaClass.getResourceAsStream("/${properties.fileName}"))
    }

    override fun executeCommand(dispatcher: Dispatcher) {
        dispatcher.command(getCommand().command) {
            val access = hasAccess(this.message)
            val chatId = message.chat.id
            access.check(
                ifTrue = {
                    defaultTelegramMessageHandler.sendMessage(bot, chatId, getVersionMessage())
                },
                ifFalse = {
                    defaultTelegramMessageHandler.sendMessage(bot, chatId, messagesProperties.accessError)
                }
            )
        }
    }

    override fun getCommand() = TelegramBotCommandType.VERSION

    private fun getVersionMessage(): String {
        val version = versionProperties.get(properties.propertyName) as String
        return "${properties.description}: $version"
    }
}
