package ru.illine.openai.telegram.bot.service.telegram.bot

import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import ru.illine.openai.telegram.bot.config.property.TelegramBotProperties
import ru.illine.openai.telegram.bot.model.TelegramBotType
import ru.illine.openai.telegram.bot.service.openai.OpenAIService
import ru.illine.openai.telegram.bot.util.FunctionHelper.catchAnyWithReturn

@Service
class OpenAIAskerTelegramBot(
    telegramBotProperties: TelegramBotProperties,
    private val openAIService: OpenAIService
) : AbstractTelegramBot(telegramBotProperties) {

    override fun onUpdateReceived(update: Update) {
        if (update.hasMessage() && update.message.hasText()) {

            catchAnyWithReturn(
                action = {
                    val openAIAnswer = openAIService.chatSingleAnswer(update.message.text)
                    val message = buildAnswer(update, openAIAnswer)
                    execute(message)
                },
                ifException = {
                    //ToDo Сделать формирование сообщения через i18n
                    val errorMessage =
                        buildAnswer(update, "Unknown error!")
                    //ToDo Сделать handling
                    execute(errorMessage)
                },
                errorLogging = {
                    log.error("Unknown error!", it)
                }
            )

        }
    }

    override fun getType() = TelegramBotType.OPEN_AI_ASKER

    private fun buildAnswer(update: Update, answer: String): SendMessage {
        val message = SendMessage()
        message.text = answer
        message.chatId = update.message.chatId.toString()
        message.replyToMessageId = update.message.messageId
        return message
    }
}