package ru.illine.openai.telegram.bot.service.telegram.impl

import org.springframework.stereotype.Service
import ru.illine.openai.telegram.bot.service.telegram.TelegramQuestionService
import java.util.concurrent.ConcurrentHashMap

@Service
class ImMemoryTelegramQuestionServiceImpl(
    private val chatToTelegramMessage: ConcurrentHashMap<Long, String>,
) : TelegramQuestionService {

    override fun getLastUserMessage(chatId: Long) = chatToTelegramMessage.get(chatId)

    override fun saveLastUserMessage(message: String, chatId: Long) {
        chatToTelegramMessage.put(chatId, message)
    }
}