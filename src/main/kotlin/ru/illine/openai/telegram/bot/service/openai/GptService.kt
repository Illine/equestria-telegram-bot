package ru.illine.openai.telegram.bot.service.openai

interface GptService {

    fun chat(question: String): Set<String>

    fun chatSingleAnswer(question: String): String

    fun getModel(): String
}
