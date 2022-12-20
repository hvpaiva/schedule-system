package hvpaiva.schedule.query.core

import org.springframework.data.jpa.repository.JpaRepository

interface ScheduleRepository : JpaRepository<ScheduleEntity, String>