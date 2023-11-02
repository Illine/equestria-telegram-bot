package ru.illine.openai.telegram.bot.dao.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.illine.openai.telegram.bot.model.entity.TelegramUserEntity

interface TelegramUserRepository : JpaRepository<TelegramUserEntity, Long> {

    fun findByUsername(username: String): TelegramUserEntity
}
