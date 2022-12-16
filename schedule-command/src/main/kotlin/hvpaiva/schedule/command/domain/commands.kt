package hvpaiva.schedule.command.domain

import hvpaiva.schedule.shared.internal.AuditEntry
import hvpaiva.schedule.shared.internal.AuditableAbstractCommand
import hvpaiva.schedule.shared.vos.*
import org.axonframework.modelling.command.TargetAggregateIdentifier
import java.time.OffsetDateTime
import javax.validation.Valid

abstract class ScheduleCommand(
    open val targetAggregateIdentifier: ScheduleId,
    override val auditEntry: AuditEntry
) : AuditableAbstractCommand(auditEntry)

data class CreateScheduleCommand(
    @TargetAggregateIdentifier override val targetAggregateIdentifier: ScheduleId,
    @field:Valid val productId: ProductId,
    @field:Valid val bankAccount: BankAccount,
    @field:Valid val amount: Money,
    val date: OffsetDateTime,
    val recurrence: RecurrenceType,
    val operation: OperationType,
    override val auditEntry: AuditEntry
) : ScheduleCommand(targetAggregateIdentifier, auditEntry) {
    constructor(
        productId: ProductId,
        bankAccount: BankAccount,
        amount: Money,
        date: OffsetDateTime,
        recurrence: RecurrenceType,
        operation: OperationType,
        auditEntry: AuditEntry
    ) : this(ScheduleId(), productId, bankAccount, amount, date, recurrence, operation, auditEntry)
}


data class CancelScheduleCommand(
    @TargetAggregateIdentifier override val targetAggregateIdentifier: ScheduleId,
    override val auditEntry: AuditEntry
) : ScheduleCommand(targetAggregateIdentifier, auditEntry)

data class ChangeScheduleRecurrenceCommand(
    @TargetAggregateIdentifier override val targetAggregateIdentifier: ScheduleId,
    @field:Valid val recurrence: RecurrenceType,
    override val auditEntry: AuditEntry
) : ScheduleCommand(targetAggregateIdentifier, auditEntry)

data class ChangeScheduledAmountCommand(
    @TargetAggregateIdentifier override val targetAggregateIdentifier: ScheduleId,
    @field:Valid val amount: Money,
    override val auditEntry: AuditEntry
) : ScheduleCommand(targetAggregateIdentifier, auditEntry)
