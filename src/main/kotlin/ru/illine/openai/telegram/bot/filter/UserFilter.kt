package ru.illine.openai.telegram.bot.filter

import com.github.kotlintelegrambot.entities.Message
import com.github.kotlintelegrambot.extensions.filters.Filter
import ru.illine.openai.telegram.bot.model.dto.InnerTelegramUserDto

class UserFilter(
    private val allowedUsersFunction: () -> Set<InnerTelegramUserDto>
) : Filter {

    override fun Message.predicate(): Boolean {
        return allowedUsersFunction()
            .map { it.username }
            .contains(chat.username)
    }
}
