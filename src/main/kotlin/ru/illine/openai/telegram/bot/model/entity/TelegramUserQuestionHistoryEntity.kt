package ru.illine.openai.telegram.bot.model.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table
import ru.illine.openai.telegram.bot.model.dto.InternalTelegramUserQuestionHistoryDto
import java.time.OffsetDateTime

@Entity
@Table(name = "telegram_user_question_histories")
class TelegramUserQuestionHistoryEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "telegramUserQuestionHistorySeqGenerator")
    @SequenceGenerator(
        name = "telegramUserQuestionHistorySeqGenerator",
        sequenceName = "telegram_user_question_history_seq",
        allocationSize = 1
    )
    val id: Long? = null,

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "telegram_user_id", nullable = false)
    val telegramUser: TelegramUserEntity,

    @Column(name = "telegram_chat_id", nullable = false, updatable = false)
    val telegramChatId: Long,

    @Column(name = "telegram_message_id", nullable = false, updatable = false)
    val telegramMessageId: Long,

    @Column(name = "telegram_user_question", nullable = false, updatable = false)
    val telegramUserQuestion: String,

    @Column(name = "created", nullable = false, updatable = false)
    @JsonIgnore
    val created: OffsetDateTime = OffsetDateTime.now()

) {

    fun toDto() = InternalTelegramUserQuestionHistoryDto(
        id = id,
        telegramUser = telegramUser.toDto(),
        telegramMessageId = telegramMessageId,
        telegramChatId = telegramChatId,
        telegramUserQuestion = telegramUserQuestion,
        created = created
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TelegramUserQuestionHistoryEntity

        if (id != other.id) return false
        if (telegramChatId != other.telegramChatId) return false
        if (telegramMessageId != other.telegramMessageId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + telegramChatId.hashCode()
        result = 31 * result + telegramMessageId.hashCode()
        return result
    }
}
