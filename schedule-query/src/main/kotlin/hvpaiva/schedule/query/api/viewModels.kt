package hvpaiva.schedule.query.api

import java.math.BigDecimal
import java.time.OffsetDateTime

data class ScheduleViewModel(
    val id: String,
    val aggregateVersion: Long,
    val productId: String,
    val bankAccount: String,
    val amount: BigDecimal,
    val date: OffsetDateTime,
    val recurrence: String,
    val operation: String,
    val isActivated: Boolean
)