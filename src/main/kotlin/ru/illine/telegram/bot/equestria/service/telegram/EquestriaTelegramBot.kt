package ru.illine.telegram.bot.equestria.service.telegram

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import ru.illine.telegram.bot.equestria.config.property.TelegramBotProperties
import ru.illine.telegram.bot.equestria.model.GptModelType
import ru.illine.telegram.bot.equestria.service.gpt.GptService
import ru.illine.telegram.bot.equestria.util.FunctionHelper.catchAnyWithReturn

@Service
class EquestriaTelegramBot(
    telegramBotProperties: TelegramBotProperties,
    gptServices: Set<GptService>,
    private val defaultGptService: GptService,
    private val defaultCoroutineScope: CoroutineScope
) : TelegramLongPollingBot(telegramBotProperties.token) {

    private val log = LoggerFactory.getLogger("BOT")
    private val gptModelToService = gptServices.associateBy { it.getModel() }

    override fun getBotUsername() = "Equestria Telegram Bot"

    //ToDo Сделать фабрику по сообщениям
    override fun onUpdateReceived(update: Update) {
        if (!shouldReply(update)) {
            log.warn("This bot shouldn't answer for the update. Do nothing. Return")
            return
        }

        defaultCoroutineScope.launch {
            catchAnyWithReturn(
                action = {
                    val openAIAnswer =
                        getGptService(GptModelType.GPT_4_BETA_TEXT_128_K).chatSingleAnswer(update.message.text)
                    val message = buildAnswer(update, openAIAnswer)
                    execute(message)
                },
                ifException = {
                    //ToDo Сделать формирование сообщения через i18n
                    val failureMessage = buildAnswer(update, "Unknown error!")
                    //ToDo Сделать handling execute(errorMessage)
                    execute(failureMessage)
                },
                errorLogging = {
                    log.error("Unknown error!", it)
                }
            )
        }
    }

    private fun getGptService(model: GptModelType): GptService { // ToDo Перенести в отдельный класс ?..
        return gptModelToService.getOrElse(
            model,
            {
                log.warn("Not found a necessary gpt service by model! Return default")
                defaultGptService
            }
        )
    }

    private fun shouldReply(update: Update) = update.hasMessage() && update.message.hasText()

    private fun buildAnswer(update: Update, answer: String): SendMessage {
        val message = SendMessage()
        message.text = answer
        message.chatId = update.message.chatId.toString()
        message.replyToMessageId = update.message.messageId
        return message
    }
}