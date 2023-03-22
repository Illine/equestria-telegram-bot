package ru.illine.openai.telegram.bot.model

import java.time.LocalDateTime

class OpenAIAnswerHistory(
    val message: String,
    val created: LocalDateTime = LocalDateTime.now()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as OpenAIAnswerHistory

        if (created != other.created) return false

        return true
    }

    override fun hashCode(): Int {
        return created.hashCode()
    }
}
