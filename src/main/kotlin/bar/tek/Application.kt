package bar.tek

import bar.tek.api.appRouting
import bar.tek.api.devicesRouting
import bar.tek.api.graphRouting
import bar.tek.devices.DeviceRepository
import bar.tek.devices.DeviceService
import bar.tek.pastData.PastTemperatureDataRepository
import bar.tek.pastData.PastTemperatureDataService
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
import kotlinx.serialization.json.Json
import java.util.concurrent.TimeUnit

val LOGGER = KtorSimpleLogger("bar.tek.App")

fun main() {
    val sensors: List<Device> = listOf(
        Device("http://192.168.0.151:88/", "Sypialnia"),
        Device("http://192.168.0.152:88/", "Pokój Witka"),
        Device("http://192.168.0.153:88/", "Salon"),
//        Device("http://192.168.0.154:88/", "Kuchnia"),
//        Device("http://192.168.0.155:88/", "Balkon ")
    )

    val pastTemperatureDataService = PastTemperatureDataService(PastTemperatureDataRepository())
    val sensorService = SensorService(sensors, SensorClient(), RealDataRepository())
    val scheduler = Scheduler(sensorService::readTemperature)
    scheduler.scheduleExecution(Every(1, TimeUnit.MINUTES))

    val deviceService = DeviceService(DeviceRepository())



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
            appRouting(sensorService, pastTemperatureDataService)
            devicesRouting(deviceService)
            graphRouting(pastTemperatureDataService)
        }
    }.start(wait = true)
}

data class Device(val deviceIp: String, val deviceName: String)

