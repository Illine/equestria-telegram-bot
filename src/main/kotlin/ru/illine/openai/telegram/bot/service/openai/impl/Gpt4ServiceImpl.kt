package ru.illine.openai.telegram.bot.service.openai.impl

import com.theokanning.openai.completion.chat.ChatCompletionRequest
import com.theokanning.openai.completion.chat.ChatMessage
import com.theokanning.openai.completion.chat.ChatMessageRole
import com.theokanning.openai.service.OpenAiService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import ru.illine.openai.telegram.bot.config.property.GptProperties
import ru.illine.openai.telegram.bot.service.openai.GptService
import ru.illine.openai.telegram.bot.util.StringHelper
import java.net.SocketTimeoutException

@Service
class Gpt4ServiceImpl(
    private val gptProperties: GptProperties,
    private val openAi: OpenAiService
) : GptService {

    private val log = LoggerFactory.getLogger("SERVICE")

    override fun chat(question: String): Set<String> {
        log.info("Calling the OpenAI API...")
        val completionRequest = buildOpenAIQuestion(question)
        try {
            return openAi
                .createChatCompletion(completionRequest)
                .choices
                .map { it.message }
                .map { it.content }
                .toSet()
        } catch (e: Exception) {
            throw when (e.cause) {
                is SocketTimeoutException -> e.cause!!
                else -> e
            }
        }
    }

    override fun chatSingleAnswer(question: String): String {
        return chat(question).joinToString(separator = StringHelper.DEFAULT_SEPARATOR)
    }

    override fun getModel() = "gpt-4-0613"

    private fun buildOpenAIQuestion(question: String): ChatCompletionRequest {
        val completionRequest = ChatCompletionRequest.builder()
            .model(getModel())
            .temperature(gptProperties.temperature)
            //ToDo Разобраться что за роль
            .messages(listOf(ChatMessage(ChatMessageRole.USER.value(), question)))
            .build()
        return completionRequest
    }
}
