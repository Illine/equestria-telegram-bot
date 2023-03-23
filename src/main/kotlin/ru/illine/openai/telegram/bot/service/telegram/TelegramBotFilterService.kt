package ru.illine.openai.telegram.bot.service.telegram

import com.github.kotlintelegrambot.extensions.filters.Filter

interface TelegramBotFilterService {

    fun userFilter(): Filter

    fun messageUserFilter(): Filter

    fun commandUserFilter(): Filter

    fun defaultFilter(): Filter
}
