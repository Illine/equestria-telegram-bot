package ru.illine.openai.telegram.bot.model.dto

import ru.illine.openai.telegram.bot.dao.entity.TelegramUserEntity

data class InnerTelegramUserDto(
    var id: Long? = null,

    val username: String,

    var administrator: Boolean = false,

    var deleted: Boolean = false
) {
    fun toEntity() = TelegramUserEntity(
        id = id,
        username = username,
        administrator = administrator
    )
}
