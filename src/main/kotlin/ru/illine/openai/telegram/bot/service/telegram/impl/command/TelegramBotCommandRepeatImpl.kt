package ru.illine.openai.telegram.bot.service.telegram.impl.command

import com.github.kotlintelegrambot.dispatcher.Dispatcher
import com.github.kotlintelegrambot.dispatcher.command
import org.springframework.stereotype.Service
import ru.illine.openai.telegram.bot.config.property.MessagesProperties
import ru.illine.openai.telegram.bot.model.type.TelegramBotCommandType
import ru.illine.openai.telegram.bot.model.type.TelegramHandlerType
import ru.illine.openai.telegram.bot.service.facade.AnswerQuestionFacade
import ru.illine.openai.telegram.bot.service.openai.OpenAIService
import ru.illine.openai.telegram.bot.service.telegram.TelegramBotCommandService
import ru.illine.openai.telegram.bot.service.telegram.TelegramMessageHandler

@Service
class TelegramBotCommandRepeatImpl(
    private val messagesProperties: MessagesProperties,
    private val openAIService: OpenAIService,
    private val answerQuestionFacade: AnswerQuestionFacade,
    private val telegramMessageHandlers: Set<TelegramMessageHandler>
) : TelegramBotCommandService {

    private val handlerToService: Map<TelegramHandlerType, TelegramMessageHandler>
        get() = telegramMessageHandlers.map { it.getType() to it }.toMap()

    override fun executeCommand(dispatcher: Dispatcher) {
        dispatcher.command(getCommand().command) {
            val chatId = message.chat.id
            answerQuestionFacade
                .getLastTelegramUserMessage(update.message!!.chat.id)
                ?.let {
                    val question = answerQuestionFacade.buildOpenAIQuestion(it, chatId)
                    answerQuestionFacade.saveLastTelegramUserMessage(message.text!!, chatId)
                    openAIService.chat(question).forEach {
                        handlerToService.get(TelegramHandlerType.OPEN_AI)!!
                            .sendMessage(this.bot, chatId, it)
                    }
                }
                ?: run {
                    handlerToService.get(TelegramHandlerType.DEFAULT)!!
                        .sendMessage(this.bot, chatId, messagesProperties.repeatCommandError)
                }
        }
    }

    override fun getCommand() = TelegramBotCommandType.REPEAT
}
