package ru.illine.openai.telegram.bot.dao.access

import ru.illine.openai.telegram.bot.model.dto.InnerTelegramUserDto

interface TelegramUserAccessService {

    fun save(dto: InnerTelegramUserDto): InnerTelegramUserDto

    fun save(dtos: Collection<InnerTelegramUserDto>): Set<InnerTelegramUserDto>

    fun findAll(): Set<InnerTelegramUserDto>

    fun findByUsername(username: String): InnerTelegramUserDto
}
