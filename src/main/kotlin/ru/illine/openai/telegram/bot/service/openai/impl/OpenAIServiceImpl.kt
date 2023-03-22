package ru.illine.openai.telegram.bot.service.openai.impl

import com.theokanning.openai.completion.chat.ChatCompletionRequest
import com.theokanning.openai.completion.chat.ChatMessage
import com.theokanning.openai.completion.chat.ChatMessageRole
import com.theokanning.openai.service.OpenAiService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import ru.illine.openai.telegram.bot.config.property.MessagesProperties
import ru.illine.openai.telegram.bot.config.property.OpenAIProperties
import ru.illine.openai.telegram.bot.service.openai.OpenAIService

@Service
class OpenAIServiceImpl(
    private val openAIProperties: OpenAIProperties,
    private val messagesProperties: MessagesProperties,
    private val openAi: OpenAiService,
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
                .map { it.substringAfter("\n") } // OpenAI отправляет в первых двух строках тех. инфу?
                .toSet()
        } catch (e: Exception) {
            log.error("Open AI returned an error!", e)
            return setOf(messagesProperties.openaiError)
        }
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