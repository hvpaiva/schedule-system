package hvpaiva.schedule.shared.events

import hvpaiva.schedule.shared.internal.AuditEntry
import hvpaiva.schedule.shared.internal.AuditableAbstractEvent
import hvpaiva.schedule.shared.vos.*
import java.time.OffsetDateTime

abstract class ScheduleEvent(
    open val aggregateIdentifier: ScheduleId,
    override val auditEntry: AuditEntry
) : AuditableAbstractEvent(auditEntry)

data class ScheduleCreatedEvent(
    override val aggregateIdentifier: ScheduleId,
    val productId: ProductId,
    val bankAccount: BankAccount,
    val amount: Money,
    val date: OffsetDateTime,
    val recurrence: RecurrenceType,
    val operation: OperationType,
    override val auditEntry: AuditEntry
) : ScheduleEvent(aggregateIdentifier, auditEntry)

data class ScheduleCancelledEvent(
    override val aggregateIdentifier: ScheduleId,
    override val auditEntry: AuditEntry
) : ScheduleEvent(aggregateIdentifier, auditEntry)

data class ScheduleRecurrenceChangedEvent(
    override val aggregateIdentifier: ScheduleId,
    val recurrence: RecurrenceType,
    override val auditEntry: AuditEntry
) : ScheduleEvent(aggregateIdentifier, auditEntry)

data class ScheduleAmountChangedEvent(
    override val aggregateIdentifier: ScheduleId,
    val amount: Money,
    override val auditEntry: AuditEntry
) : ScheduleEvent(aggregateIdentifier, auditEntry)