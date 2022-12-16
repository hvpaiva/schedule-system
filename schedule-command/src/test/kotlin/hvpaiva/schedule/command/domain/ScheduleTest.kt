package hvpaiva.schedule.command.domain

import hvpaiva.schedule.shared.events.ScheduleCreatedEvent
import hvpaiva.schedule.shared.internal.AuditEntry
import hvpaiva.schedule.shared.vos.*
import org.axonframework.messaging.interceptors.BeanValidationInterceptor
import org.axonframework.test.aggregate.AggregateTestFixture
import org.axonframework.test.aggregate.FixtureConfiguration
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.Clock
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId

class ScheduleTest {
    private lateinit var fixture: FixtureConfiguration<Schedule>

    private val clock = Clock.fixed(Instant.now(), ZoneId.systemDefault())
    private val auditEntry = AuditEntry("user", Instant.now())
    private val scheduleId = ScheduleId("scheduleId")
    private val productId = ProductId("productId")
    private val bankAccount = BankAccount("bankAccount")

    @BeforeEach
    fun setUp() {
        fixture = AggregateTestFixture(Schedule::class.java)
        fixture.registerCommandDispatchInterceptor(BeanValidationInterceptor())
    }

    @Test
    fun `should create schedule`() {
        val createCommand = CreateScheduleCommand(
            targetAggregateIdentifier = scheduleId,
            productId = productId,
            bankAccount = bankAccount,
            amount = Money(BigDecimal.TEN),
            date = OffsetDateTime.now(clock),
            recurrence = RecurrenceType.MONTHLY,
            operation = OperationType.BUY,
            auditEntry = auditEntry
        )
        val createdEvent = ScheduleCreatedEvent(
            aggregateIdentifier = scheduleId,
            productId = productId,
            bankAccount = bankAccount,
            amount = Money(BigDecimal.TEN),
            date = OffsetDateTime.now(clock),
            recurrence = RecurrenceType.MONTHLY,
            operation = OperationType.BUY,
            auditEntry = auditEntry
        )
        fixture.givenNoPriorActivity()
            .`when`(createCommand)
            .expectSuccessfulHandlerExecution()
            .expectEvents(createdEvent)
    }
}