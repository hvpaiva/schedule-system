package hvpaiva.schedule.command.api

data class CreateScheduleInputModel(
    val productId: String,
    val bankAccount: String
)