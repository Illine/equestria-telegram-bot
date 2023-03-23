package ru.illine.openai.telegram.bot.service.telegram.impl.command

import com.github.kotlintelegrambot.dispatcher.Dispatcher
import com.github.kotlintelegrambot.dispatcher.command
import org.springframework.stereotype.Service
import ru.illine.openai.telegram.bot.config.property.VersionProperties
import ru.illine.openai.telegram.bot.model.type.TelegramBotCommandType
import ru.illine.openai.telegram.bot.service.telegram.TelegramBotCommandService
import ru.illine.openai.telegram.bot.service.telegram.TelegramMessageHandler
import java.util.*

@Service
class TelegramBotCommandVersionImpl(
    private val properties: VersionProperties,
    private val defaultTelegramMessageHandler: TelegramMessageHandler
) : TelegramBotCommandService {

    private val versionProperties = Properties()

    init {
        versionProperties.load(this.javaClass.getResourceAsStream("/${properties.fileName}"))
    }

    override fun executeCommand(dispatcher: Dispatcher) {
        dispatcher.command(getCommand().command) {
            defaultTelegramMessageHandler.sendMessage(bot, update.message!!.chat.id, getVersionMessage())
        }
    }

    override fun getCommand() = TelegramBotCommandType.VERSION

    private fun getVersionMessage(): String {
        val version = versionProperties.get(properties.propertyName) as String
        return "${properties.description}: $version"
    }
}
