package ru.illine.openai.telegram.bot.service.telegram.impl.command

import com.github.kotlintelegrambot.dispatcher.Dispatcher
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.entities.ChatId
import org.springframework.stereotype.Service
import ru.illine.openai.telegram.bot.config.property.VersionProperties
import ru.illine.openai.telegram.bot.model.type.TelegramBotCommandType
import ru.illine.openai.telegram.bot.service.telegram.TelegramBotCommandService
import java.util.*

@Service
class TelegramBotCommandVersionImpl(
    private val properties: VersionProperties,
) : TelegramBotCommandService {

    private val versionProperties = Properties()

    init {
        versionProperties.load(this.javaClass.getResourceAsStream("/${properties.fileName}"))
    }

    override fun executeCommand(dispatcher: Dispatcher) {
        dispatcher.command(getCommand().command) {
            bot.sendMessage(
                chatId = ChatId.fromId(update.message!!.chat.id),
                text = getVersionMessage()
            )
        }
    }

    override fun getCommand() = TelegramBotCommandType.VERSION

    private fun getVersionMessage(): String {
        val version = versionProperties.get(properties.propertyName) as String
        return "${properties.description}: $version"
    }
}