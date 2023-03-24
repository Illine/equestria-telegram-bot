package ru.illine.openai.telegram.bot.service.telegram.impl.command

import com.github.kotlintelegrambot.dispatcher.Dispatcher
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.dispatcher.handlers.CommandHandlerEnvironment
import com.github.kotlintelegrambot.entities.Message
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import ru.illine.openai.telegram.bot.config.property.MessagesProperties
import ru.illine.openai.telegram.bot.dao.access.TelegramUserAccessService
import ru.illine.openai.telegram.bot.model.dto.InnerTelegramUserDto
import ru.illine.openai.telegram.bot.model.type.TelegramBotCommandType
import ru.illine.openai.telegram.bot.service.telegram.AbstractTelegramBotCommandService
import ru.illine.openai.telegram.bot.service.telegram.TelegramBotFilterService
import ru.illine.openai.telegram.bot.service.telegram.TelegramMessageHandler
import ru.illine.openai.telegram.bot.util.FunctionHelper.catchAny
import ru.illine.openai.telegram.bot.util.FunctionHelper.check
import ru.illine.openai.telegram.bot.util.TelegramUsernameHelper.matchUsername

@Service
class TelegramBotCommandAddUserImpl(
    private val defaultTelegramMessageHandler: TelegramMessageHandler,
    private val messagesProperties: MessagesProperties,
    private val userAccessService: TelegramUserAccessService,
    telegramBotFilterService: TelegramBotFilterService
) : AbstractTelegramBotCommandService(telegramBotFilterService) {

    private val usernameNumberForSave = 1

    private val log = LoggerFactory.getLogger("SERVICE")

    override fun executeCommand(dispatcher: Dispatcher) {
        dispatcher.command(getCommand().command) {
            val access = hasAccess(this.message)
            val chatId = message.chat.id
            access.check(
                ifTrue = {
                    catchAny(
                        action = { save(chatId) },
                        ifException = { defaultTelegramMessageHandler.sendMessage(bot, chatId, messagesProperties.addUserCommandSaveError) },
                        logging = { log.error("Unknown exception!", it) }
                    )
                },
                ifFalse = {
                    defaultTelegramMessageHandler.sendMessage(bot, chatId, messagesProperties.accessError)
                }
            )
        }
    }

    private fun CommandHandlerEnvironment.save(chatId: Long) {
        if (args.size == usernameNumberForSave) {
            saveNewUser(chatId, args.first())
        } else if (args.size > usernameNumberForSave) {
            val username = args.first()
            val message = "${messagesProperties.addUserCommandMuchMoreError}$username"
            defaultTelegramMessageHandler.sendMessage(bot, chatId, message)
            saveNewUser(chatId, username)
        } else if (args.isEmpty()) {
            defaultTelegramMessageHandler.sendMessage(bot, chatId, messagesProperties.addUserCommandEmptyError)
        }
    }

    private fun CommandHandlerEnvironment.saveNewUser(chatId: Long, username: String) {
        if (matchUsername(username)) {
            userAccessService.save(InnerTelegramUserDto(username = username))
            val message = "${messagesProperties.addUserCommand}@$username"
            defaultTelegramMessageHandler.sendMessage(bot, chatId, message)
        } else {
            defaultTelegramMessageHandler.sendMessage(bot, chatId, messagesProperties.addUserCommandFilterError)
        }
    }

    override fun getCommand() = TelegramBotCommandType.ADD_USER

    override fun hasAccess(message: Message) = telegramBotFilterService.adminFilter().checkFor(message)
}
