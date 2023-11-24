package bar.tek

import bar.tek.api.appRouting
import bar.tek.api.devicesRouting
import bar.tek.devices.DeviceRepository
import bar.tek.devices.DeviceService
import bar.tek.pastData.PastTemperatureDataRepository
import bar.tek.pastData.PastTemperatureDataService
import bar.tek.realTimeData.Every
import bar.tek.realTimeData.RealDataRepository
import bar.tek.realTimeData.Scheduler
import bar.tek.realTimeData.SensorClient
import bar.tek.realTimeData.SensorService
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.response.respondText
import io.ktor.server.routing.routing
import io.ktor.server.webjars.Webjars
import io.ktor.util.logging.KtorSimpleLogger
import kotlinx.css.CssBuilder
import kotlinx.serialization.json.Json
import java.util.concurrent.TimeUnit

val LOGGER = KtorSimpleLogger("bar.tek.App")

fun main() {
    val pastTemperatureDataService = PastTemperatureDataService(PastTemperatureDataRepository())
    val sensorService = SensorService(SensorClient(), RealDataRepository())
    val scheduler = Scheduler(sensorService::readTemperature)
    scheduler.scheduleExecution(Every(5, TimeUnit.MINUTES))

    val deviceService = DeviceService(DeviceRepository())

    embeddedServer(Netty, port = 8080, watchPaths = listOf("classes")) {
        install(CallLogging)
        install(Webjars) {
            path = "assets"
        }
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }
        routing {
            appRouting(sensorService, pastTemperatureDataService)
            devicesRouting(deviceService)
        }
    }.start(wait = true)
}

suspend inline fun ApplicationCall.respondCss(builder: CssBuilder.() -> Unit) {
    LOGGER.info("Execution method from CSS.")
    this.respondText(CssBuilder().apply(builder).toString(), ContentType.Text.CSS)
}
