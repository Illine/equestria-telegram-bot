package ru.illine.openai.telegram.bot.service.telegram.impl

import com.github.kotlintelegrambot.extensions.filters.Filter
import org.springframework.stereotype.Service
import ru.illine.openai.telegram.bot.config.property.TelegramBotProperties
import ru.illine.openai.telegram.bot.filter.UserFilter
import ru.illine.openai.telegram.bot.service.telegram.TelegramBotFilterService

@Service
class TelegramBotAccessServiceImpl(
    private val telegramBotProperties: TelegramBotProperties,
) : TelegramBotFilterService {

    override fun userFilter() = UserFilter(telegramBotProperties)

    override fun messageUserFilter() = userFilter() and Filter.Text

    override fun commandUserFilter() = userFilter() and Filter.Command

    override fun defaultFilter() = Filter.Text.not() and Filter.Command.not()
}