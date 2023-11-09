package ru.illine.telegram.bot.equestria.service.gpt

import ru.illine.telegram.bot.equestria.model.GptModelType

interface GptService {

    fun chat(question: String): Set<String>

    fun chatSingleAnswer(question: String): String

    fun getModel(): GptModelType

}
