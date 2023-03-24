package ru.illine.openai.telegram.bot.service.openai.impl

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.illine.openai.telegram.bot.config.property.OpenAIProperties
import ru.illine.openai.telegram.bot.dao.access.OpenAIAnswerHistoryAccessService
import ru.illine.openai.telegram.bot.dao.access.TelegramUserAccessService
import ru.illine.openai.telegram.bot.model.dto.InnerOpenAIAnswerHistoryDto
import ru.illine.openai.telegram.bot.service.openai.OpenAIAnswerService
import ru.illine.openai.telegram.bot.util.StringHelper

@Service
class OpenAIAnswerServiceImpl(
    private val openAIProperties: OpenAIProperties,
    private val openAIAnswerHistoryAccessService: OpenAIAnswerHistoryAccessService,
    private val userAccessService: TelegramUserAccessService
) : OpenAIAnswerService {

    private val log = LoggerFactory.getLogger("SERVICE")

    override fun buildQuestion(sourceQuestion: String, chatId: Long): String {
        log.debug("Building an openai questions for a chat: [$chatId]")
        log.debug("The question:\n${sourceQuestion}\n")
        val answerHistories = getAnswerHistory(chatId)
        return createOpenAIQuestion(answerHistories, sourceQuestion)
    }

    @Transactional
    override fun enrichAnswerHistory(newAnswer: String, chatId: Long, username: String) {
        log.debug("Enriching an answer history for a chat: [$chatId]")
        val openAIAnswerHistories = getAnswerHistory(chatId)
        if (openAIAnswerHistories.size < openAIProperties.answerHistoryCount) {
            openAIAnswerHistoryAccessService.save(buildAnswer(username, chatId, newAnswer))
        } else {
            log.debug("An answer limit ([${openAIProperties.answerHistoryCount}]) was achieved.")
            log.debug("The oldest answer will be removed!")
            val oldestOpenAIAnswerHistory = openAIAnswerHistories.minBy { it.created!! }
            openAIAnswerHistoryAccessService.deleteById(oldestOpenAIAnswerHistory.id!!)
            openAIAnswerHistoryAccessService.save(buildAnswer(username, chatId, newAnswer))
        }
    }

    override fun clearAnswerHistory(chatId: Long) {
        openAIAnswerHistoryAccessService.deleteAllByChatId(chatId)
    }

    private fun getAnswerHistory(chatId: Long): Set<InnerOpenAIAnswerHistoryDto> {
        return openAIAnswerHistoryAccessService.findByChatId(chatId).toMutableSet()
    }

    private fun createOpenAIQuestion(answerHistories: Collection<InnerOpenAIAnswerHistoryDto>, sourceQuestion: String): String {
        return if (answerHistories.isNotEmpty()) {
            answerHistories
                .map { it.openAIAnswer }
                .joinToString(separator = StringHelper.DEFAULT_SEPARATOR)
                .let { "$it\n\n$sourceQuestion" }
        } else {
            sourceQuestion
        }
    }

    private fun buildAnswer(
        username: String,
        chatId: Long,
        newAnswer: String
    ): InnerOpenAIAnswerHistoryDto {
        val user = userAccessService.findByUsername(username)
        val savedNewAnswer =
            InnerOpenAIAnswerHistoryDto(
                telegramUser = user,
                telegramChatId = chatId,
                openAIAnswer = newAnswer
            )
        return savedNewAnswer
    }
}
