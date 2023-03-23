package ru.illine.openai.telegram.bot.filter

import com.github.kotlintelegrambot.entities.Message
import com.github.kotlintelegrambot.extensions.filters.Filter
import ru.illine.openai.telegram.bot.config.property.TelegramBotProperties

class UserFilter(
    private val telegramBotProperties: TelegramBotProperties
) : Filter {

    override fun Message.predicate(): Boolean = telegramBotProperties.allowedUsers.contains(chat.username)
}
