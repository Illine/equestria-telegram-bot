package ru.illine.openai.telegram.bot.dao.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table
import org.hibernate.annotations.ResultCheckStyle
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.Where
import ru.illine.openai.telegram.bot.model.dto.InnerTelegramUserDto

@Entity
@Table(
    name = "telegram_users",
    indexes = [Index(name = "telegram_users_username_unique_index", columnList = "username", unique = true)]
)
@SQLDelete(
    sql = "update telegram_users set deleted = true, updated = current_timestamp where id = ?",
    check = ResultCheckStyle.COUNT
)
@Where(clause = "deleted = false")
class TelegramUserEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userSeqGenerator")
    @SequenceGenerator(
        name = "userSeqGenerator",
        sequenceName = "telegram_user_seq",
        allocationSize = 1
    )
    val id: Long? = null,

    @Column(name = "username", nullable = false, updatable = false)
    val username: String,

    @Column(name = "administrator", nullable = false)
    var administrator: Boolean = false,

    @Column(name = "deleted", nullable = false)
    var deleted: Boolean = false

) {

    fun toDto() = InnerTelegramUserDto(
        id = id,
        username = username,
        administrator = administrator,
        deleted = deleted
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TelegramUserEntity

        if (id != other.id) return false
        if (username != other.username) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + username.hashCode()
        return result
    }
}
