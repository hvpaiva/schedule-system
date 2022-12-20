package hvpaiva.schedule.query.core

import hvpaiva.schedule.query.api.FindAllSchedulesQuery
import hvpaiva.schedule.query.api.FindScheduleByIdQuery
import hvpaiva.schedule.query.api.ScheduleViewModel
import hvpaiva.schedule.shared.events.ScheduleAmountChangedEvent
import hvpaiva.schedule.shared.events.ScheduleCancelledEvent
import hvpaiva.schedule.shared.events.ScheduleCreatedEvent
import hvpaiva.schedule.shared.events.ScheduleRecurrenceChangedEvent
import org.axonframework.config.ProcessingGroup
import org.axonframework.eventhandling.AllowReplay
import org.axonframework.eventhandling.EventHandler
import org.axonframework.eventhandling.SequenceNumber
import org.axonframework.queryhandling.QueryHandler
import org.axonframework.queryhandling.QueryUpdateEmitter
import org.springframework.stereotype.Component

@Component
@ProcessingGroup("schedule")
internal class ScheduleProjection(
    private val repository: ScheduleRepository,
    private val emitter: QueryUpdateEmitter
) {

    @EventHandler
    @AllowReplay(true)
    fun handle(event: ScheduleCreatedEvent, @SequenceNumber sequenceNumber: Long) {
        val entity = ScheduleEntity(
            id = event.aggregateIdentifier.identifier,
            aggregateVersion = sequenceNumber,
            productId = event.productId.identifier,
            bankAccount = event.bankAccount.value,
            amount = event.amount.amount,
            date = event.date,
            recurrence = event.recurrence.name,
            operation = event.operation.name,
            isActivated = true
        )

        saveAndBroadcast(entity)
    }

    @EventHandler
    @AllowReplay(true)
    fun handle(event: ScheduleCancelledEvent, @SequenceNumber sequenceNumber: Long) {
        val entity = repository
            .findById(event.aggregateIdentifier.identifier)
            .orElseThrow { IllegalStateException("Schedule not found") }

        entity.aggregateVersion = sequenceNumber
        entity.isActivated = false

        saveAndBroadcast(entity)
    }

    @EventHandler
    @AllowReplay(true)
    fun handle(event: ScheduleRecurrenceChangedEvent, @SequenceNumber sequenceNumber: Long) {
        val entity = repository
            .findById(event.aggregateIdentifier.identifier)
            .orElseThrow { IllegalStateException("Schedule not found") }

        entity.aggregateVersion = sequenceNumber
        entity.recurrence = event.recurrence.name

        saveAndBroadcast(entity)
    }

    @EventHandler
    @AllowReplay(true)
    fun handle(event: ScheduleAmountChangedEvent, @SequenceNumber sequenceNumber: Long) {
        val entity = repository
            .findById(event.aggregateIdentifier.identifier)
            .orElseThrow { IllegalStateException("Schedule not found") }

        entity.aggregateVersion = sequenceNumber
        entity.amount = event.amount.toBigDecimal()

        saveAndBroadcast(entity)
    }

    @QueryHandler
    fun handle(query: FindScheduleByIdQuery): ScheduleViewModel {
        return repository
            .findById(query.id)
            .map { toViewModel(it) }
            .orElseThrow { IllegalStateException("Schedule not found") }

    }

    @QueryHandler
    fun handle(query: FindAllSchedulesQuery): List<ScheduleViewModel> {
        return repository
            .findAll()
            .map { toViewModel(it) }
    }

    private fun toViewModel(entity: ScheduleEntity): ScheduleViewModel {
        return ScheduleViewModel(
            id = entity.id,
            aggregateVersion = entity.aggregateVersion,
            productId = entity.productId,
            bankAccount = entity.bankAccount,
            amount = entity.amount,
            date = entity.date,
            recurrence = entity.recurrence,
            operation = entity.operation,
            isActivated = entity.isActivated
        )
    }

    private fun saveAndBroadcast(entity: ScheduleEntity) {
        repository.save(entity)

        val viewModel = toViewModel(entity)

        emitter.emit(
            FindScheduleByIdQuery::class.java,
            { query -> query.id == viewModel.id },
            viewModel
        )

        emitter.emit(
            FindAllSchedulesQuery::class.java,
            { true },
            viewModel
        )
    }

}