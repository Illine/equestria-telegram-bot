package ru.illine.openai.telegram.bot.util

object TelegramUsernameHelper {

    private val defaultTelegramUsernameRegex = "^(\\d|\\w|\\_){5,32}\$".toRegex()

    fun filterUsernames(usernames: Collection<String>): Set<String> {
        return usernames
            .filter { defaultTelegramUsernameRegex.matches(it) }
            .toSet()
    }

    fun matchUsername(username: String) = defaultTelegramUsernameRegex.matches(username)
}
