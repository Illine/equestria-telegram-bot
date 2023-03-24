package ru.illine.openai.telegram.bot.service.openai

interface OpenAIService {

    fun chat(question: String): Set<String>

    fun chatSingleAnswer(question: String): String
}
