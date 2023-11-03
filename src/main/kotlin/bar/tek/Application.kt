package bar.tek

import bar.tek.pastData.PastTemperatureDataRepository
import bar.tek.api.appRouting
import bar.tek.pastData.PastTemperatureDataService
import bar.tek.realTimeData.Every
import bar.tek.realTimeData.Scheduler
import bar.tek.realTimeData.SensorClient
import bar.tek.realTimeData.SensorService
import io.ktor.http.ContentType
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.response.respondText
import io.ktor.server.routing.routing
import io.ktor.server.webjars.Webjars
import kotlinx.css.CssBuilder
import java.util.concurrent.TimeUnit

fun main() {
    val pastTemperatureDataService = PastTemperatureDataService(PastTemperatureDataRepository())
    val sensorService = SensorService(SensorClient(), PastTemperatureDataRepository())
    val scheduler = Scheduler(sensorService::readTemperature)
    scheduler.scheduleExecution(Every(5, TimeUnit.MINUTES))

    embeddedServer(Netty, port = 8080) {
        install(Webjars) {
            path = "assets"
        }
        routing {
            appRouting(sensorService, pastTemperatureDataService)
        }
    }.start(wait = true)
}

suspend inline fun ApplicationCall.respondCss(builder: CssBuilder.() -> Unit) {
    this.respondText(CssBuilder().apply(builder).toString(), ContentType.Text.CSS)
}
