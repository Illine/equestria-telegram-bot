package ru.illine.openai.telegram.bot.model.type

import ru.illine.openai.telegram.bot.config.property.MessagesProperties

enum class TelegramBotCommandType(val command: String) {

    START("start") {
        override fun description(properties: MessagesProperties) = properties.startCommandDescription
    },
    CLEAR("clear") {
        override fun description(properties: MessagesProperties) = properties.clearCommandDescription
    },
    REPEAT("repeat") {
        override fun description(properties: MessagesProperties) = properties.repeatCommandDescription
    },
    VERSION("version") {
        override fun description(properties: MessagesProperties) = properties.versionCommandDescription
    },
    UNKNOWN("unknown") {
        override fun description(properties: MessagesProperties) = "stub"
    };

    abstract fun description(properties: MessagesProperties): String
}