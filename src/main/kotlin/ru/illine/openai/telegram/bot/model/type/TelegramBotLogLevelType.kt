package ru.illine.openai.telegram.bot.model.type

import com.github.kotlintelegrambot.logging.LogLevel

enum class TelegramBotLogLevelType(
    val logLevel: LogLevel
) {

    ALL(LogLevel.All()),
    NONE(LogLevel.None),
    NETWORK_BASIC(LogLevel.Network.Basic),
    NETWORK_HEADERS(LogLevel.Network.Headers),
    ERROR(LogLevel.Error);
}
