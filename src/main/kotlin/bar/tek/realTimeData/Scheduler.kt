package bar.tek.realTimeData

import io.ktor.util.logging.KtorSimpleLogger
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
class Scheduler(private val task: Runnable) {
    private val executor = Executors.newScheduledThreadPool(1)

    fun scheduleExecution(every: Every) {
        val taskWrapper = Runnable {
            task.runCatching { task.run() }
                .onSuccess { LOGGER.info("Task completed successfully.") }
                .onFailure { LOGGER.error("Task failed: ${it.message}") }
        }
        executor.scheduleWithFixedDelay(task, every.n, every.n, every.unit)
    }

    companion object {
        val LOGGER = KtorSimpleLogger("bar.tek.service.Scheduler")
    }
}
data class Every(val n: Long, val unit: TimeUnit);