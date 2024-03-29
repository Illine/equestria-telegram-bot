package ru.illine.openai.telegram.bot.service.telegram.impl

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.dispatcher.Dispatcher
import com.github.kotlintelegrambot.dispatcher.handlers.MessageHandlerEnvironment
import com.github.kotlintelegrambot.dispatcher.message
import com.github.kotlintelegrambot.entities.BotCommand
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.illine.openai.telegram.bot.config.property.MessagesProperties
import ru.illine.openai.telegram.bot.model.type.TelegramBotCommandType
import ru.illine.openai.telegram.bot.model.type.TelegramHandlerType
import ru.illine.openai.telegram.bot.service.facade.AnswerQuestionFacade
import ru.illine.openai.telegram.bot.service.telegram.TelegramBotCommandService
import ru.illine.openai.telegram.bot.service.telegram.TelegramBotFilterService
import ru.illine.openai.telegram.bot.service.telegram.TelegramBotService
import ru.illine.openai.telegram.bot.service.telegram.TelegramMessageHandler
import ru.illine.openai.telegram.bot.util.FunctionHelper.catchAny
import java.net.SocketTimeoutException

@Service
class TelegramBotServiceImpl(
    private val messagesProperties: MessagesProperties,
    private val answerQuestionFacade: AnswerQuestionFacade,
    private val telegramBotFilterService: TelegramBotFilterService,
    private val telegramMessageHandlers: Set<TelegramMessageHandler>,
    private val telegramBotCommandServices: Set<TelegramBotCommandService>,
    private val openAICCoroutineScope: CoroutineScope
) : TelegramBotService {

    private val log = LoggerFactory.getLogger("SERVICE")

    private val commandToService: Map<TelegramBotCommandType, TelegramBotCommandService>
        get() = telegramBotCommandServices.map { it.getCommand() to it }.toMap()

    private val handlerToService: Map<TelegramHandlerType, TelegramMessageHandler>
        get() = telegramMessageHandlers.map { it.getType() to it }.toMap()

    override fun createMenu(bot: Bot) {
        val commands =
            enumValues<TelegramBotCommandType>()
                .filter { it.visible }
                .map { BotCommand(it.command, it.description(messagesProperties)) }
                .toList()
        bot.setMyCommands(commands)
    }

    @Transactional
    override fun askOpenAI(dispatcher: Dispatcher) {
        dispatcher.message(telegramBotFilterService.messageUserFilter()) {
            val handler = this
            openAICCoroutineScope.launch {
                catchAny(
                    action = {
                        askOpenAI(handler)
                    },
                    ifException = {
                        val errorMessage = when (it) {
                            is SocketTimeoutException -> messagesProperties.openaiError
                            else -> messagesProperties.unknownError
                        }
                        handlerToService[TelegramHandlerType.DEFAULT]
                            ?.sendMessage(handler.bot, message.chat.id, errorMessage, handler.message.messageId)
                    },
                    logging = {
                        log.error("Unknown exception!", it)
                    }
                )
            }
        }
    }

    override fun performCommand(dispatcher: Dispatcher) {
        enumValues<TelegramBotCommandType>().forEach {
            commandToService[it]?.apply { this.executeCommand(dispatcher) }
        }
    }

    override fun getDefault(dispatcher: Dispatcher) {
        dispatcher.message(telegramBotFilterService.defaultFilter()) {
            handlerToService[TelegramHandlerType.DEFAULT]
                ?.sendMessage(
                    this.bot,
                    message.chat.id,
                    messagesProperties.wrongMessageTypeError
                )
        }
    }

    private fun MessageHandlerEnvironment.askOpenAI(handler: MessageHandlerEnvironment) {
        val chatId = message.chat.id
        val messageId = message.messageId
        val messageText = message.text!!
        val username = message.chat.username!!
        val question = answerQuestionFacade.buildOpenAIQuestion(messageText, chatId, username)
        answerQuestionFacade.saveLastTelegramUserMessage(messageText, chatId, messageId, username)
        answerQuestionFacade.clearOldTelegramUserMessages(chatId)
        val answer = answerQuestionFacade.askOpenAI(question)
        handlerToService[TelegramHandlerType.OPEN_AI]?.sendMessage(handler.bot, chatId, answer, messageId, messageText)
    }
}
