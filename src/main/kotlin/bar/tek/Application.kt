package bar.tek

import bar.tek.devices.DeviceDto
import bar.tek.devices.DeviceRepository
import bar.tek.devices.DeviceService
import bar.tek.realTimeData.Every
import bar.tek.realTimeData.RealDataRepository
import bar.tek.realTimeData.Scheduler
import bar.tek.realTimeData.SensorClient
import bar.tek.realTimeData.SensorService
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.routing
import io.ktor.server.webjars.Webjars
import io.ktor.util.logging.KtorSimpleLogger
import java.util.concurrent.TimeUnit
import kotlinx.serialization.json.Json

val LOGGER = KtorSimpleLogger("bar.tek.App")

fun main() {
//    val sensors = listOf(
//        Device("http://192.168.0.151:88/", "Sypialnia"),
//        Device("http://192.168.0.152:88/", "Pokój Witka"),
//        Device("http://192.168.0.153:88/", "Salon"),
//        Device("http://192.168.0.154:88/", "Kuchnia"),
//        Device("http://192.168.0.155:88/", "Balkon ")
//    )


    val sensors = emptyList<DeviceDto>()
    val deviceService = DeviceService(DeviceRepository())
    val sensorService = SensorService(sensors, SensorClient(), RealDataRepository())

    Scheduler(deviceService::getAllDevices).apply {
        scheduleExecution(Every(3, TimeUnit.MINUTES))
    }

    Scheduler(sensorService::readTemperature).apply {
        scheduleExecution(Every(1, TimeUnit.MINUTES))
    }


    embeddedServer(Netty, port = 8080, watchPaths = listOf("classes")) {
        install(CallLogging)
        install(Webjars) {
            path = "assets"
        }
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                isLenient = true
            })
        }
        routing {
        }
        LOGGER.info("Application started")
    }.start(wait = true)
}

