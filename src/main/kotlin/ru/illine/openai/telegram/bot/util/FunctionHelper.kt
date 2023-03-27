package ru.illine.openai.telegram.bot.util

object FunctionHelper {

    inline fun <T> Boolean.check(
        ifTrue: Boolean.() -> T,
        ifFalse: Boolean.() -> T
    ) = when (this) {
        true -> ifTrue()
        false -> ifFalse()
    }

    inline fun catchAny(action: () -> Unit, ifException: (Exception) -> Unit? = {}, logging: (Exception) -> Unit?) {
        try {
            action()
        } catch (e: Exception) {
            logging(e)
            ifException(e)
        }
    }

    inline fun <T> catchAnyWithReturn(action: () -> T, ifException: () -> T, logging: (Exception) -> Unit?): T {
        try {
            return action()
        } catch (e: Exception) {
            logging(e)
            return ifException()
        }
    }
}
