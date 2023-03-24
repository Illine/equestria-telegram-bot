package ru.illine.openai.telegram.bot.filter

import com.github.kotlintelegrambot.entities.Message
import com.github.kotlintelegrambot.extensions.filters.Filter
import ru.illine.openai.telegram.bot.model.dto.InnerTelegramUserDto

class AdminFilter(
    private val allowedUsersFunction: () -> Set<InnerTelegramUserDto>
) : Filter {

    override fun Message.predicate(): Boolean {
        return allowedUsersFunction()
            .filter { it.administrator }
            .map { it.username }
            .contains(chat.username)
    }
}
