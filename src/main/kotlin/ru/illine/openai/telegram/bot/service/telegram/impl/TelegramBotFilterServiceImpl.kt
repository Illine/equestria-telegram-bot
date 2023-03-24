package ru.illine.openai.telegram.bot.service.telegram.impl

import com.github.kotlintelegrambot.extensions.filters.Filter
import org.springframework.stereotype.Service
import ru.illine.openai.telegram.bot.dao.access.TelegramUserAccessService
import ru.illine.openai.telegram.bot.filter.AdminFilter
import ru.illine.openai.telegram.bot.filter.UserFilter
import ru.illine.openai.telegram.bot.service.telegram.TelegramBotFilterService

@Service
class TelegramBotFilterServiceImpl(
    private val userAccessService: TelegramUserAccessService
) : TelegramBotFilterService {

    override fun userFilter() = UserFilter(userAccessService::findAll)

    override fun messageUserFilter() = userFilter() and Filter.Text

    override fun adminFilter() = AdminFilter(userAccessService::findAll)

    override fun defaultFilter() = Filter.Text.not() and Filter.Command.not()
}
