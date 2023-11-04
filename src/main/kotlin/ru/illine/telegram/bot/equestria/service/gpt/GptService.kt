package ru.illine.telegram.bot.equestria.service.gpt

interface GptService {

    fun chat(question: String): Set<String>

    fun chatSingleAnswer(question: String): String

    fun getModel(): String
}