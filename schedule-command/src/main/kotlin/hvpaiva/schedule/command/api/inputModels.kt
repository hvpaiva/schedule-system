package hvpaiva.schedule.command.api

import java.math.BigDecimal
import java.time.OffsetDateTime

data class CreateScheduleInputModel(
    val productId: String,
    val bankAccount: String,
    val amount: BigDecimal,
    val date: OffsetDateTime,
    val recurrence: String,
    val operation: String
)

data class ChangeAmountInputModel(
    val amount: BigDecimal
)

data class ChangeRecurrenceInputModel(
    val recurrence: String
)