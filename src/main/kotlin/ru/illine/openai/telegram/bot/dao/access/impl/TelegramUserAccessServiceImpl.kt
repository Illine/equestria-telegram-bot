package ru.illine.openai.telegram.bot.dao.access.impl

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.illine.openai.telegram.bot.dao.access.TelegramUserAccessService
import ru.illine.openai.telegram.bot.dao.repository.TelegramUserRepository
import ru.illine.openai.telegram.bot.model.dto.InnerTelegramUserDto

@Service
class TelegramUserAccessServiceImpl(
    private val telegramUserRepository: TelegramUserRepository
) : TelegramUserAccessService {

    @Transactional
    override fun save(dto: InnerTelegramUserDto): InnerTelegramUserDto {
        val entity = telegramUserRepository.save(dto.toEntity())
        return entity.toDto()
    }

    @Transactional
    override fun save(dtos: Collection<InnerTelegramUserDto>): Set<InnerTelegramUserDto> {
        val entities = dtos.map { it.toEntity() }
        return telegramUserRepository.saveAll(entities).map { it.toDto() }.toSet()
    }

    @Transactional
    override fun findAll(): Set<InnerTelegramUserDto> {
        return telegramUserRepository.findAll().map { it.toDto() }.toSet()
    }

    @Transactional
    override fun findByUsername(username: String): InnerTelegramUserDto {
        return telegramUserRepository.findByUsername(username).toDto()
    }
}
