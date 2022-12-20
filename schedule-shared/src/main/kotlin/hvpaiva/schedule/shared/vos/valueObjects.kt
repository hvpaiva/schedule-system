package hvpaiva.schedule.shared.vos

import java.io.Serializable
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*

data class ScheduleId(val identifier: String) : Serializable {
    constructor() : this(UUID.randomUUID().toString())
}

data class ProductId(val identifier: String) : Serializable

data class BankAccount(val value: String) : Serializable

data class Money(val amount: BigDecimal, val currency: Currency) : Serializable {
    constructor(amount: BigDecimal) : this(amount, Currency.getInstance("BRL"))

    fun toBigDecimal() = amount.setScale(2, RoundingMode.HALF_EVEN)

    override fun toString(): String = "${currency.symbol} ${amount.setScale(2, RoundingMode.DOWN)}"
}

enum class RecurrenceType {
    DAILY, WEEKLY, MONTHLY, YEARLY, ONCE
}

enum class OperationType {
    BUY, SELL
}