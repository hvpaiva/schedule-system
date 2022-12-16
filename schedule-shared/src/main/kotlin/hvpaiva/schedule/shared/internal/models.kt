package hvpaiva.schedule.shared.internal

import java.time.Instant

data class AuditEntry(val who: String, val `when`: Instant)

abstract class AuditableAbstractEvent(open val auditEntry: AuditEntry)

abstract class AuditableAbstractCommand(open val auditEntry: AuditEntry)
