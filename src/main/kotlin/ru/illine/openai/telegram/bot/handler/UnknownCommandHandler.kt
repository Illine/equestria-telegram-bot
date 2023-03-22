package ru.illine.openai.telegram.bot.handler

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.dispatcher.handlers.Handler
import com.github.kotlintelegrambot.entities.Message
import com.github.kotlintelegrambot.entities.Update
import ru.illine.openai.telegram.bot.model.type.TelegramBotCommandType

data class UnknownCommandHandlerEnvironment internal constructor(
    val bot: Bot,
    val update: Update,
    val message: Message,
)

internal class UnknownCommandHandler(
    private val handleCommand: UnknownCommandHandlerEnvironment.() -> Unit,
) : Handler {

    override fun checkUpdate(update: Update): Boolean {
        return (
                update.message?.text != null && update.message!!.text!!.startsWith("/") &&
                        enumValues<TelegramBotCommandType>().none { it.command == update.message!!.text!!.drop(1) }
                )
    }

    override fun handleUpdate(bot: Bot, update: Update) {
        checkNotNull(update.message)
        handleCommand(UnknownCommandHandlerEnvironment(bot, update, update.message!!))
    }

}
