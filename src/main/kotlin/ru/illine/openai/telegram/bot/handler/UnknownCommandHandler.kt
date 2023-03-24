package ru.illine.openai.telegram.bot.handler

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.dispatcher.handlers.Handler
import com.github.kotlintelegrambot.entities.Message
import com.github.kotlintelegrambot.entities.Update
import ru.illine.openai.telegram.bot.model.type.TelegramBotCommandType

data class UnknownCommandHandlerEnvironment internal constructor(
    val bot: Bot,
    val update: Update,
    val message: Message
)

internal class UnknownCommandHandler(
    private val handleCommand: UnknownCommandHandlerEnvironment.() -> Unit
) : Handler {

    override fun checkUpdate(update: Update): Boolean {
        return hasText(update) && isCommand(update) && (foundCommand(update) or isUnknownCommand(update))
    }

    private fun hasText(update: Update) = update.message?.text != null

    private fun isCommand(update: Update) = update.message!!.text!!.startsWith("/")

    private fun foundCommand(update: Update): Boolean {
        return enumValues<TelegramBotCommandType>().none { it.match(update.message!!.text!!.drop(1)) }
    }

    private fun isUnknownCommand(update: Update) = TelegramBotCommandType.UNKNOWN.match(update.message!!.text!!.drop(1))

    override fun handleUpdate(bot: Bot, update: Update) {
        checkNotNull(update.message)
        handleCommand(UnknownCommandHandlerEnvironment(bot, update, update.message!!))
    }
}
