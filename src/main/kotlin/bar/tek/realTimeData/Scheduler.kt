package bar.tek.realTimeData

import io.ktor.util.logging.KtorSimpleLogger
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

val LOG = KtorSimpleLogger("bar.tek.service.Scheduler")
class Scheduler(private val task: Runnable) {
    private val executor = Executors.newScheduledThreadPool(1)

    fun scheduleExecution(every: Every) {
        val taskWrapper = Runnable {
            task.runCatching { task.run() }
                .onSuccess { LOG.info("Task completed successfully.") }
                .onFailure { LOG.error("Task failed: ${it.message}")
                    executor.scheduleWithFixedDelay(task, every.n, every.n, every.unit)
                }
        }
        executor.scheduleWithFixedDelay(task, every.n, every.n, every.unit)
    }

}
data class Every(val n: Long, val unit: TimeUnit);