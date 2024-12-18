package bar.tek.realTimeData

import io.ktor.util.logging.KtorSimpleLogger
import java.time.Duration
import java.time.LocalDateTime
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

val LOG = KtorSimpleLogger("bar.tek.service.Scheduler")

class Scheduler(private val task: Runnable) {
    private val executor = Executors.newScheduledThreadPool(1)

    fun scheduleExecutionAtFixedMinutes(minutes: List<Int>) {
        val taskWrapper = Runnable {
            task.runCatching { task.run() }
                .onSuccess { LOG.info("Task completed successfully.") }
                .onFailure { LOG.error("Task failed: ${it.message}") }
        }

        val now = LocalDateTime.now()
        val nextExecutionTime = minutes.minOf { minute ->
            now.withMinute(minute).withSecond(0).withNano(0).takeIf { it.isAfter(now) }
                ?: now.plusHours(1).withMinute(minute).withSecond(0).withNano(0)
        }

        val initialDelay = Duration.between(now, nextExecutionTime).toMillis()
        executor.scheduleAtFixedRate(taskWrapper, initialDelay, TimeUnit.MINUTES.toMillis(5), TimeUnit.MILLISECONDS)
    }
}

data class Every(val n: Long, val unit: TimeUnit);