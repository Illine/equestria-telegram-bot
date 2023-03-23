package ru.illine.openai.telegram.bot.service.openai.impl

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import ru.illine.openai.telegram.bot.config.property.OpenAIProperties
import ru.illine.openai.telegram.bot.model.OpenAIAnswerHistory
import ru.illine.openai.telegram.bot.service.openai.OpenAIAnswerService
import java.util.concurrent.ConcurrentHashMap

@Service
class InMemoryOpenAIAnswerServiceImpl(
    private val chatToOpenAIAnswers: ConcurrentHashMap<Long, MutableSet<OpenAIAnswerHistory>>,
    private val openAIProperties: OpenAIProperties
) : OpenAIAnswerService {

    private val log = LoggerFactory.getLogger("SERVICE")

    override fun buildQuestion(sourceQuestion: String, chatId: Long): String {
        log.debug("Building an openai questions for a chat: [$chatId]")
        log.debug("The question:\n${sourceQuestion}\n")
        val answerHistories = getAnswerHistory(chatId)
        return createOpenAIQuestion(answerHistories, sourceQuestion)
    }

    override fun enrichAnswerHistory(newAnswer: String, chatId: Long) {
        log.debug("Enriching an answer history for a chat: [$chatId]")
        val answerHistories = getAnswerHistory(chatId)
        if (answerHistories.size < openAIProperties.answerHistoryCount) {
            answerHistories.add(OpenAIAnswerHistory(newAnswer))
        } else {
            log.debug("An answer limit ([${openAIProperties.answerHistoryCount}]) was achieved.")
            log.debug("The oldest answer will be removed!")
            val oldestUserHistory = answerHistories.minBy { it.created }
            answerHistories.remove(oldestUserHistory)
            answerHistories.add(OpenAIAnswerHistory(newAnswer))
        }
    }

    override fun clearAnswerHistory(chatId: Long) {
        chatToOpenAIAnswers.get(chatId)?.apply { this.clear() }
    }

    private fun initHistoryByChat(chatId: Long) {
        if (!chatToOpenAIAnswers.containsKey(chatId)) {
            log.info("Init a map for a chatId: [{}]", chatId)
            chatToOpenAIAnswers.put(chatId, mutableSetOf())
        }
    }

    private fun getAnswerHistory(chatId: Long): MutableSet<OpenAIAnswerHistory> {
        initHistoryByChat(chatId)
        return chatToOpenAIAnswers.get(chatId)!!
    }

    private fun createOpenAIQuestion(answerHistories: MutableSet<OpenAIAnswerHistory>, sourceQuestion: String): String {
        return if (answerHistories.isNotEmpty()) {
            answerHistories
                .map { it.message }
                .joinToString(separator = "\n\n")
                .let { "$it\n\n$sourceQuestion" }
        } else {
            sourceQuestion
        }
    }
}
