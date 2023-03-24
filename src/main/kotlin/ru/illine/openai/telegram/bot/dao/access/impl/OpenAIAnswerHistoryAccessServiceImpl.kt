package ru.illine.openai.telegram.bot.dao.access.impl

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.illine.openai.telegram.bot.dao.access.OpenAIAnswerHistoryAccessService
import ru.illine.openai.telegram.bot.dao.repository.OpenAIAnswerHistoryRepository
import ru.illine.openai.telegram.bot.model.dto.InnerOpenAIAnswerHistoryDto

@Service
class OpenAIAnswerHistoryAccessServiceImpl(
    private val openAIAnswerHistoryRepository: OpenAIAnswerHistoryRepository
) : OpenAIAnswerHistoryAccessService {

    @Transactional
    override fun findByChatId(chatId: Long): Set<InnerOpenAIAnswerHistoryDto> {
        return openAIAnswerHistoryRepository.findAllByTelegramChatId(chatId)
            .map { it.toDto() }
            .toMutableSet()
    }

    @Transactional
    override fun save(dto: InnerOpenAIAnswerHistoryDto): InnerOpenAIAnswerHistoryDto {
        val entity = openAIAnswerHistoryRepository.save(dto.toEntity())
        return entity.toDto()
    }

    @Transactional
    override fun deleteById(id: Long) {
        openAIAnswerHistoryRepository.deleteById(id)
    }

    @Transactional
    override fun deleteAllByChatId(chatId: Long) {
        openAIAnswerHistoryRepository.deleteAllByTelegramChatId(chatId)
    }
}
