package ru.illine.telegram.bot.equestria.util

class StringHelper {

    companion object {
        val DEFAULT_SEPARATOR = "\n\n"
        val SINGLE_NEW_LINE_SEPARATOR = "\n"
        val SPECIAL_TELEGRAM_CHARACTERS =
            listOf("_", "*", "[", "]", "(", ")", "~", "`", ">", "#", "+", "-", "=", "|", "{", "}", ".", "!")

        fun replaceSpecialTelegramCharacters(message: String): String {
            return message.replace(
                Regex("[" + SPECIAL_TELEGRAM_CHARACTERS.joinToString("") { "\\" + it } + "]"),
                { result -> "\\" + result.value }
            )
        }
    }
}
