package hvpaiva.schedule.command.api

import hvpaiva.schedule.command.domain.CancelScheduleCommand
import hvpaiva.schedule.command.domain.ChangeScheduleRecurrenceCommand
import hvpaiva.schedule.command.domain.ChangeScheduledAmountCommand
import hvpaiva.schedule.command.domain.CreateScheduleCommand
import hvpaiva.schedule.shared.internal.AuditEntry
import hvpaiva.schedule.shared.vos.*
import org.axonframework.commandhandling.callbacks.LoggingCallback
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

    @PatchMapping("/{id}/deactivate")
    fun deactivateSchedule(
        @RequestHeader("X-Requester-Name") requesterName: String = "Unknown",
        @PathVariable id: String
    ) = gateway.send(
        CancelScheduleCommand(
            targetAggregateIdentifier = ScheduleId(id),
            auditEntry = AuditEntry(requesterName, Instant.now())
        ),
        LoggingCallback.INSTANCE
    )

    @PatchMapping("/{id}/change-amount")
    fun chengeAmountSchedule(
        @RequestHeader("X-Requester-Name") requesterName: String = "Unknown",
        @PathVariable id: String,
        @RequestBody request: ChangeAmountInputModel
    ) = gateway.send(
        ChangeScheduledAmountCommand(
            targetAggregateIdentifier = ScheduleId(id),
            amount = Money(request.amount),
            auditEntry = AuditEntry(requesterName, Instant.now())
        ),
        LoggingCallback.INSTANCE
    )

    @PatchMapping("/{id}/change-recurrence")
    fun chengeRecurrenceSchedule(
        @RequestHeader("X-Requester-Name") requesterName: String = "Unknown",
        @PathVariable id: String,
        @RequestBody request: ChangeRecurrenceInputModel
    ) = gateway.send(
        ChangeScheduleRecurrenceCommand(
            targetAggregateIdentifier = ScheduleId(id),
            recurrence = RecurrenceType.valueOf(request.recurrence),
            auditEntry = AuditEntry(requesterName, Instant.now())
        ),
        LoggingCallback.INSTANCE
    )
}