package ru.illine.openai.telegram.bot.service.telegram.impl

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.dispatcher.Dispatcher
import com.github.kotlintelegrambot.dispatcher.message
import com.github.kotlintelegrambot.dispatcher.telegramError
import com.github.kotlintelegrambot.entities.BotCommand
import com.github.kotlintelegrambot.entities.ChatId
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import ru.illine.openai.telegram.bot.config.property.MessagesProperties
import ru.illine.openai.telegram.bot.model.type.TelegramBotCommandType
import ru.illine.openai.telegram.bot.model.type.TelegramHandlerType
import ru.illine.openai.telegram.bot.service.facade.AnswerQuestionFacade
import ru.illine.openai.telegram.bot.service.openai.OpenAIService
import ru.illine.openai.telegram.bot.service.telegram.TelegramBotCommandService
import ru.illine.openai.telegram.bot.service.telegram.TelegramBotFilterService
import ru.illine.openai.telegram.bot.service.telegram.TelegramBotService
import ru.illine.openai.telegram.bot.service.telegram.TelegramMessageHandler

@Service
class TelegramBotServiceImpl(
    private val messagesProperties: MessagesProperties,
    private val openAIService: OpenAIService,
    private val answerQuestionFacade: AnswerQuestionFacade,
    private val telegramBotFilterService: TelegramBotFilterService,
    private val telegramMessageHandlers: Set<TelegramMessageHandler>,
    private val telegramBotCommandServices: Set<TelegramBotCommandService>,
) : TelegramBotService {

    private val log = LoggerFactory.getLogger("SERVICE")

    private val commandToService: Map<TelegramBotCommandType, TelegramBotCommandService>
        get() = telegramBotCommandServices.map { it.getCommand() to it }.toMap()

    private val handlerToService: Map<TelegramHandlerType, TelegramMessageHandler>
        get() = telegramMessageHandlers.map { it.getType() to it }.toMap()

    override fun menu(bot: Bot) {
        val commands =
            enumValues<TelegramBotCommandType>().filter { it != TelegramBotCommandType.UNKNOWN }.map { BotCommand(it.command, it.description(messagesProperties)) }.toList()
        bot.setMyCommands(commands)
    }

    override fun message(dispatcher: Dispatcher) {
        dispatcher.message(telegramBotFilterService.messageUserFilter()) {
            val chatId = message.chat.id
            val question = answerQuestionFacade.buildOpenAIQuestion(message.text!!, chatId)
            answerQuestionFacade.saveLastTelegramUserMessage(message.text!!, chatId)
            openAIService.chat(question).forEach {
                handlerToService.get(TelegramHandlerType.OPEN_AI)!!.sendMessage(this.bot, chatId, it)
            }
        }
    }

    override fun command(dispatcher: Dispatcher) {
        enumValues<TelegramBotCommandType>().forEach {
            commandToService.get(it)?.apply { this.executeCommand(dispatcher) }
        }
    }

    override fun default(dispatcher: Dispatcher) {
        dispatcher.message(telegramBotFilterService.defaultFilter()) {
            val chatId = message.chat.id
            bot.sendMessage(chatId = ChatId.fromId(chatId), text = messagesProperties.wrongMessageTypeError)
            handlerToService.get(TelegramHandlerType.DEFAULT)!!.sendMessage(this.bot, chatId, messagesProperties.wrongMessageTypeError)
        }
    }

    override fun error(dispatcher: Dispatcher) {
        dispatcher.telegramError {
            log.error("Telegram threw an exception! An error message: [{}]", this.error.getErrorMessage())
        }
    }
}