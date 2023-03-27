package ru.illine.openai.telegram.bot.dao.entity

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
import ru.illine.openai.telegram.bot.model.dto.InnerOpenAIAnswerHistoryDto
import java.time.OffsetDateTime

@Entity
@Table(name = "openai_answer_histories")
class OpenAIAnswerHistoryEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "openAIAnswerHistorySeqGenerator")
    @SequenceGenerator(
        name = "openAIAnswerHistorySeqGenerator",
        sequenceName = "openai_answer_history_seq",
        allocationSize = 1
    )
    val id: Long? = null,

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "telegram_user_id", nullable = false)
    val telegramUser: TelegramUserEntity,

    @Column(name = "telegram_chat_id", nullable = false, updatable = false)
    val telegramChatId: Long,

    @Column(name = "openai_answer", nullable = false, updatable = false)
    val openAIAnswer: String,

    @Column(name = "openai_question", nullable = false, updatable = false)
    val openAIQuestion: String,

    @Column(name = "created", nullable = false, updatable = false)
    @JsonIgnore
    val created: OffsetDateTime = OffsetDateTime.now()

) {

    fun toDto() = InnerOpenAIAnswerHistoryDto(
        id = id,
        telegramUser = telegramUser.toDto(),
        telegramChatId = telegramChatId,
        openAIAnswer = openAIAnswer,
        openAIQuestion = openAIQuestion,
        created = created
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as OpenAIAnswerHistoryEntity

        if (id != other.id) return false
        if (telegramChatId != other.telegramChatId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + telegramChatId.hashCode()
        return result
    }
}
