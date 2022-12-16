package hvpaiva.schedule.command.api

import hvpaiva.schedule.command.domain.CreateScheduleCommand
import hvpaiva.schedule.shared.internal.AuditEntry
import hvpaiva.schedule.shared.vos.*
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal
import java.time.Instant
import java.time.OffsetDateTime

@RestController
@RequestMapping("/schedules")
class ScheduleController(
    val gateway: CommandGateway
) {
    @PostMapping
    fun createSchedule(@RequestBody request: CreateScheduleInputModel): ScheduleId =
        gateway.sendAndWait(
            CreateScheduleCommand(
                productId = ProductId(request.productId),
                bankAccount = BankAccount(request.bankAccount),
                amount = Money(BigDecimal.TEN),
                date = OffsetDateTime.now(),
                recurrence = RecurrenceType.MONTHLY,
                operation = OperationType.BUY,
                auditEntry = AuditEntry("User", Instant.now())
            )
        )
}