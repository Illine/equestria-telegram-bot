package ru.illine.openai.telegram.bot.util

object FunctionHelper {

    inline fun <T> Boolean.check(
        ifTrue: Boolean.() -> T,
        ifFalse: Boolean.() -> T
    ) = when (this) {
        true -> ifTrue()
        false -> ifFalse()
    }

    inline fun catchAny(action: () -> Unit, ifException: (Exception) -> Unit? = {}, errorLogging: (Exception) -> Unit?) {
        try {
            action()
        } catch (e: Exception) {
            errorLogging(e)
            ifException(e)
        }
    }

    inline fun <T> catchAnyWithReturn(action: () -> T, ifException: () -> T, errorLogging: (Exception) -> Unit?): T {
        return try {
            action()
        } catch (e: Exception) {
            errorLogging(e)
            ifException()
        }
    }
}
