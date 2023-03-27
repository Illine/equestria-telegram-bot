package ru.illine.openai.telegram.bot.service.openai.impl

import com.theokanning.openai.completion.chat.ChatCompletionRequest
import com.theokanning.openai.completion.chat.ChatMessage
import com.theokanning.openai.completion.chat.ChatMessageRole
import com.theokanning.openai.service.OpenAiService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import ru.illine.openai.telegram.bot.config.property.OpenAIProperties
import ru.illine.openai.telegram.bot.service.openai.OpenAIService
import ru.illine.openai.telegram.bot.util.StringHelper
import java.net.SocketTimeoutException

@Service
class OpenAIServiceImpl(
    private val openAIProperties: OpenAIProperties,
    private val openAi: OpenAiService
) : OpenAIService {

    private val log = LoggerFactory.getLogger("SERVICE")

    override fun chat(question: String): Set<String> {
        val completionRequest = buildOpenAIQuestion(question)
        try {
            return openAi
                .createChatCompletion(completionRequest)
                .choices
                .map { it.message }
                .map { it.content }
                .toSet()
        } catch (e: Exception) {
            log.error("Open AI returned an error!", e)
            throw when (e.cause) {
                is SocketTimeoutException -> e.cause!!
                else -> e
            }
        }
    }

    override fun chatSingleAnswer(question: String): String {
        return chat(question).joinToString(separator = StringHelper.DEFAULT_SEPARATOR)
    }

    private fun buildOpenAIQuestion(question: String): ChatCompletionRequest {
        val completionRequest = ChatCompletionRequest.builder()
            .model(openAIProperties.model)
            .temperature(openAIProperties.temperature)
            .messages(listOf(ChatMessage(ChatMessageRole.USER.value(), question)))
            .build()
        return completionRequest
    }
}
