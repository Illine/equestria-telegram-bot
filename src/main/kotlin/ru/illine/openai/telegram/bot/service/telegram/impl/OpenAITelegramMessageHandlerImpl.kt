package ru.illine.openai.telegram.bot.service.telegram.impl

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.ParseMode
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import ru.illine.openai.telegram.bot.config.property.MessagesProperties
import ru.illine.openai.telegram.bot.model.type.TelegramHandlerType
import ru.illine.openai.telegram.bot.service.facade.AnswerQuestionFacade
import ru.illine.openai.telegram.bot.service.telegram.TelegramMessageHandler

@Service("openAITelegramMessageHandler")
class OpenAITelegramMessageHandlerImpl(
    private val messagesProperties: MessagesProperties,
    private val answerQuestionFacade: AnswerQuestionFacade
) : TelegramMessageHandler {

    private val log = LoggerFactory.getLogger("SERVICE")

    override fun sendMessage(bot: Bot, chatId: Long, message: String, sourceMessageId: Long?) {
        bot.sendMessage(
            chatId = ChatId.fromId(chatId),
            text = message,
            parseMode = ParseMode.MARKDOWN,
            replyToMessageId = sourceMessageId
        ).fold(
            ifSuccess = {
                log.debug("An answer has sent to Telegram successfully")
                answerQuestionFacade.enrichOpenAIAnswerHistory(it.text!!, chatId)
            },
            ifError = {
                log.error("An answer hasn't sent to Telegram! Send an error message to Telegram!")
                bot.sendMessage(chatId = ChatId.fromId(chatId), text = messagesProperties.telegramError, replyToMessageId = sourceMessageId)
                it.get()
            }
        )
    }

    override fun getType() = TelegramHandlerType.OPEN_AI
}
