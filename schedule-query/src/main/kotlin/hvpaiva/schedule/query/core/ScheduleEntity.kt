package hvpaiva.schedule.query.core

import org.apache.commons.lang3.builder.EqualsBuilder
import org.apache.commons.lang3.builder.HashCodeBuilder
import org.apache.commons.lang3.builder.ToStringBuilder
import java.math.BigDecimal
import java.time.OffsetDateTime
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class ScheduleEntity(
    @Id var id: String,
    var aggregateVersion: Long,
    var productId: String,
    var bankAccount: String,
    var amount: BigDecimal,
    var date: OffsetDateTime,
    var recurrence: String,
    var operation: String,
    var isActivated: Boolean
) {
    constructor() : this("", 0, "", "", BigDecimal.ZERO, OffsetDateTime.now(), "", "", false)

    override fun toString(): String = ToStringBuilder.reflectionToString(this)

    override fun equals(other: Any?): Boolean = EqualsBuilder.reflectionEquals(this, other)

    override fun hashCode(): Int = HashCodeBuilder.reflectionHashCode(this)
}