package ru.illine.openai.telegram.bot.dao.access.impl

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.illine.openai.telegram.bot.dao.access.TelegramUserQuestionHistoryAccessService
import ru.illine.openai.telegram.bot.dao.repository.TelegramUserQuestionHistoryRepository
import ru.illine.openai.telegram.bot.model.dto.InternalTelegramUserQuestionHistoryDto

@Service
class TelegramUserQuestionHistoryAccessServiceImpl(
    private val telegramUserQuestionHistoryRepository: TelegramUserQuestionHistoryRepository
) : TelegramUserQuestionHistoryAccessService {

    @Transactional
    override fun findAllByChatId(chatId: Long): Set<InternalTelegramUserQuestionHistoryDto> {
        return telegramUserQuestionHistoryRepository.findAllByTelegramChatId(chatId)
            .map { it.toDto() }
            .toSet()
    }

    @Transactional
    override fun findLastMessageByChatId(chatId: Long): InternalTelegramUserQuestionHistoryDto? {
        return findAllByChatId(chatId).maxByOrNull { it.created!! }
    }

    @Transactional
    override fun save(dto: InternalTelegramUserQuestionHistoryDto): InternalTelegramUserQuestionHistoryDto {
        val savedEntity = telegramUserQuestionHistoryRepository.save(dto.toEntity())
        return savedEntity.toDto()
    }

    @Transactional
    override fun deleteAll(ids: Collection<Long>) {
        telegramUserQuestionHistoryRepository.deleteAllById(ids)
    }
}
