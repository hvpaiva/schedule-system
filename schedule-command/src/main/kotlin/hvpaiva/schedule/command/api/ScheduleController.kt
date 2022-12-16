package hvpaiva.schedule.command.api

import hvpaiva.schedule.command.domain.CreateScheduleCommand
import hvpaiva.schedule.shared.internal.AuditEntry
import hvpaiva.schedule.shared.vos.*
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.web.bind.annotation.*
import java.time.Instant

@RestController
@RequestMapping("/schedules")
class ScheduleController(
    val gateway: CommandGateway
) {
    @PostMapping
    fun createSchedule(
        @RequestHeader("X-Requester-Name") requesterName: String = "Unknown",
        @RequestBody request: CreateScheduleInputModel
    ): ScheduleId = gateway.sendAndWait(
        CreateScheduleCommand(
            productId = ProductId(request.productId),
            bankAccount = BankAccount(request.bankAccount),
            amount = Money(request.amount),
            date = request.date,
            recurrence = RecurrenceType.valueOf(request.recurrence),
            operation = OperationType.valueOf(request.operation),
            auditEntry = AuditEntry(requesterName, Instant.now())
        )
    )
}