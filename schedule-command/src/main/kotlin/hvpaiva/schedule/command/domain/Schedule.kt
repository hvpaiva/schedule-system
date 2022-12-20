package hvpaiva.schedule.command.domain

import hvpaiva.schedule.shared.events.ScheduleAmountChangedEvent
import hvpaiva.schedule.shared.events.ScheduleCancelledEvent
import hvpaiva.schedule.shared.events.ScheduleCreatedEvent
import hvpaiva.schedule.shared.events.ScheduleRecurrenceChangedEvent
import hvpaiva.schedule.shared.vos.*
import org.apache.commons.lang3.builder.EqualsBuilder
import org.apache.commons.lang3.builder.HashCodeBuilder
import org.apache.commons.lang3.builder.ToStringBuilder
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.spring.stereotype.Aggregate
import java.time.OffsetDateTime

@Aggregate(snapshotTriggerDefinition = "scheduleSnapshotTriggerDefinition")
class Schedule {
    @AggregateIdentifier
    private lateinit var identifier: ScheduleId
    private lateinit var productId: ProductId
    private lateinit var bankAccount: BankAccount
    private lateinit var amount: Money
    private lateinit var date: OffsetDateTime
    private lateinit var recurrence: RecurrenceType
    private lateinit var operation: OperationType
    private var isActivated: Boolean = false // ASK: Is this preferred over a date field?

    constructor()

    @CommandHandler
    constructor(command: CreateScheduleCommand) {
        AggregateLifecycle.apply(
            ScheduleCreatedEvent(
                command.targetAggregateIdentifier,
                command.productId,
                command.bankAccount,
                command.amount,
                command.date,
                command.recurrence,
                command.operation,
                command.auditEntry
            )
        )
    }

    @EventSourcingHandler
    fun on(event: ScheduleCreatedEvent) {
        identifier = event.aggregateIdentifier
        productId = event.productId
        bankAccount = event.bankAccount
        amount = event.amount
        date = event.date
        recurrence = event.recurrence
        operation = event.operation
        isActivated = true
    }

    @CommandHandler
    fun handle(command: CancelScheduleCommand) {
        if (!isActivated) throw IllegalStateException("Schedule is already cancelled")

        AggregateLifecycle.apply(
            ScheduleCancelledEvent(
                command.targetAggregateIdentifier,
                command.auditEntry
            )
        )
    }

    @EventSourcingHandler
    fun on(event: ScheduleCancelledEvent) {
        isActivated = false
    }

    @CommandHandler
    fun handle(command: ChangeScheduleRecurrenceCommand) {
        if (!isActivated) throw IllegalStateException("Schedule is cancelled")

        AggregateLifecycle.apply(
            ScheduleRecurrenceChangedEvent(
                command.targetAggregateIdentifier,
                command.recurrence,
                command.auditEntry
            )
        )
    }

    @EventSourcingHandler
    fun on(event: ScheduleRecurrenceChangedEvent) {
        recurrence = event.recurrence
    }

    @CommandHandler
    fun handle(command: ChangeScheduledAmountCommand) {
        if (!isActivated) throw IllegalStateException("Schedule is cancelled")

        AggregateLifecycle.apply(
            ScheduleAmountChangedEvent(
                command.targetAggregateIdentifier,
                command.amount,
                command.auditEntry
            )
        )
    }

    @EventSourcingHandler
    fun on(event: ScheduleAmountChangedEvent) {
        amount = event.amount
    }

    override fun toString(): String = ToStringBuilder.reflectionToString(this)

    override fun equals(other: Any?): Boolean = EqualsBuilder.reflectionEquals(this, other)

    override fun hashCode(): Int = HashCodeBuilder.reflectionHashCode(this)

}