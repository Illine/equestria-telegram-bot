package ru.illine.openai.telegram.bot.service.telegram.impl.command

import com.github.kotlintelegrambot.dispatcher.Dispatcher
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.dispatcher.handlers.CommandHandlerEnvironment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import ru.illine.openai.telegram.bot.config.property.MessagesProperties
import ru.illine.openai.telegram.bot.model.type.TelegramBotCommandType
import ru.illine.openai.telegram.bot.model.type.TelegramHandlerType
import ru.illine.openai.telegram.bot.service.facade.AnswerQuestionFacade
import ru.illine.openai.telegram.bot.service.openai.OpenAIService
import ru.illine.openai.telegram.bot.service.telegram.AbstractTelegramBotCommandService
import ru.illine.openai.telegram.bot.service.telegram.TelegramBotFilterService
import ru.illine.openai.telegram.bot.service.telegram.TelegramMessageHandler
import ru.illine.openai.telegram.bot.util.FunctionHelper.catchAny
import ru.illine.openai.telegram.bot.util.FunctionHelper.check
import java.net.SocketTimeoutException

@Service
class TelegramBotCommandRepeatImpl(
    private val messagesProperties: MessagesProperties,
    private val openAIService: OpenAIService,
    private val answerQuestionFacade: AnswerQuestionFacade,
    private val telegramMessageHandlers: Set<TelegramMessageHandler>,
    private val openAICCoroutineScope: CoroutineScope,
    telegramBotFilterService: TelegramBotFilterService
) : AbstractTelegramBotCommandService(telegramBotFilterService) {

    private val log = LoggerFactory.getLogger("SERVICE")

    private val handlerToService: Map<TelegramHandlerType, TelegramMessageHandler>
        get() = telegramMessageHandlers.map { it.getType() to it }.toMap()

    override fun executeCommand(dispatcher: Dispatcher) {
        dispatcher.command(getCommand().command) {
            val handler = this
            openAICCoroutineScope.launch {
                catchAny(
                    action = {
                        executeRepeat(handler)
                    },
                    ifException = {
                        val originalMessageIdToErrorText = when (it) {
                            is SocketTimeoutException -> {
                                val lastMessageId = answerQuestionFacade.getLastTelegramUserMessage(handler.message.chat.id)!!.second
                                val errorText = messagesProperties.openaiError
                                lastMessageId to errorText
                            }

                            else -> null to messagesProperties.unknownError
                        }
                        handlerToService.get(TelegramHandlerType.DEFAULT)!!
                            .sendMessage(handler.bot, message.chat.id, originalMessageIdToErrorText.second, originalMessageIdToErrorText.first)
                    },
                    logging = {
                        log.error("Unknown exception!", it)
                    }
                )
            }
        }
    }

    private fun executeRepeat(handler: CommandHandlerEnvironment) {
        val chatId = handler.message.chat.id
        val username = handler.message.chat.username!!
        val access = hasAccess(handler.update.message!!)
        access.check(
            ifTrue = { askOpenAI(chatId, username, handler) },
            ifFalse = {
                handlerToService.get(TelegramHandlerType.DEFAULT)!!
                    .sendMessage(handler.bot, chatId, messagesProperties.accessError)
            }
        )
    }

    private fun askOpenAI(chatId: Long, username: String, handler: CommandHandlerEnvironment) {
        answerQuestionFacade.getLastTelegramUserMessage(chatId)
            ?.let {
                val lastTelegramQuestion = it.first
                val lastTelegramUserMessageId = it.second
                val question = answerQuestionFacade.buildOpenAIQuestion(lastTelegramQuestion, chatId, username)
                val answer = openAIService.chatSingleAnswer(question)
                handlerToService.get(TelegramHandlerType.OPEN_AI)!!
                    .sendMessage(handler.bot, chatId, answer, lastTelegramUserMessageId, lastTelegramQuestion)
            }
            ?: run {
                handlerToService.get(TelegramHandlerType.DEFAULT)!!
                    .sendMessage(handler.bot, chatId, messagesProperties.repeatCommandError)
            }
    }

    override fun getCommand() = TelegramBotCommandType.REPEAT
}
