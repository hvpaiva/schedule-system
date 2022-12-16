package hvpaiva.schedule.command

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication/*(scanBasePackages = ["hvpaiva.schedule.command.*"])*/
class ScheduleCommandApplication

fun main(args: Array<String>) {
    runApplication<ScheduleCommandApplication>(*args)
}