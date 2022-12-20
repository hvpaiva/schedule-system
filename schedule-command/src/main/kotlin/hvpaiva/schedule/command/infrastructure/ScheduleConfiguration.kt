package hvpaiva.schedule.command.infrastructure

import org.axonframework.eventsourcing.EventCountSnapshotTriggerDefinition
import org.axonframework.eventsourcing.Snapshotter
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
internal class ScheduleConfiguration {

    @Bean
    fun scheduleSnapshotTriggerDefinition(
        snapshotter: Snapshotter,
        @Value("\${axon.snapshot.trigger.threshold.schedule:100}") threshold: Int
    ) =
        EventCountSnapshotTriggerDefinition(snapshotter, threshold)
}