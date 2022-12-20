package hvpaiva.schedule.query.api

import hvpaiva.schedule.shared.vos.*
import org.axonframework.messaging.responsetypes.ResponseTypes
import org.axonframework.queryhandling.QueryGateway
import org.springframework.web.bind.annotation.*
import java.util.concurrent.CompletableFuture

@RestController
@RequestMapping("/schedules")
class ScheduleController(
    val gateway: QueryGateway,
) {

    @GetMapping
    fun findAll(): CompletableFuture<List<ScheduleViewModel>> =
        gateway.query(
            FindAllSchedulesQuery(),
            ResponseTypes.multipleInstancesOf(ScheduleViewModel::class.java)
        )
}