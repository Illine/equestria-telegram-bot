package ru.illine.openai.telegram.bot.model.type

import ru.illine.openai.telegram.bot.config.property.MessagesProperties

enum class TelegramBotCommandType(
    val command: String,
    val visible: Boolean
) {

    START("start", false) {
        override fun description(properties: MessagesProperties) = properties.startCommandDescription
    },
    REPEAT("repeat", true) {
        override fun description(properties: MessagesProperties) = properties.repeatCommandDescription
    },

//    GET_USERS("getUsers", false) {
//        override fun description(properties: MessagesProperties) = properties.getUsersCommandDescription
//    },
    ADD_USER("adduser", true) {
        override fun description(properties: MessagesProperties) = properties.addUserCommandDescription
    },

//    REMOVE_USER("removeUser", false) {
//        override fun description(properties: MessagesProperties) = properties.removeUserCommandDescription
//    },
    CLEAR("clear", true) {
        override fun description(properties: MessagesProperties) = properties.clearCommandDescription
    },
    VERSION("version", true) {
        override fun description(properties: MessagesProperties) = properties.versionCommandDescription
    },
    UNKNOWN("unknown", false) {
        override fun description(properties: MessagesProperties) = properties.unknownCommandDescription
    };

    abstract fun description(properties: MessagesProperties): String

    fun match(sourceCommand: String): Boolean {
        val commandWithArgs = getCommandWithArgs(sourceCommand)
        val command = commandWithArgs[0]
        val args = formatArgs(commandWithArgs.drop(1))
        val defaultRegex = "^(${command}$args).*$".toRegex()
        return defaultRegex.matches(this.command)
    }

    private fun getCommandWithArgs(sourceCommand: String): List<String> = sourceCommand.split("\\s+".toRegex())

    private fun formatArgs(args: Collection<String>) = args.filter { it == this.command }.joinToString(separator = " ")
}
