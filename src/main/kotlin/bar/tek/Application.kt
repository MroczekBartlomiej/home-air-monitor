package bar.tek

import bar.tek.devices.DeviceDto
import bar.tek.devices.DeviceRepository
import bar.tek.devices.DeviceService
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
import io.ktor.server.websocket.WebSockets
import io.ktor.server.websocket.pingPeriod
import io.ktor.server.websocket.webSocket
import io.ktor.util.logging.KtorSimpleLogger
import io.ktor.websocket.Frame
import java.time.Duration
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.serialization.json.Json

val LOGGER = KtorSimpleLogger("bar.tek.App")
val logChannel = BroadcastChannel<String>(Channel.BUFFERED)


@OptIn(ObsoleteCoroutinesApi::class)
fun main() {
//    val sensors = listOf(
//        Device("http://192.168.0.151:88/", "Sypialnia"),
//        Device("http://192.168.0.152:88/", "Pokój Witka"),
//        Device("http://192.168.0.153:88/", "Salon"),
//        Device("http://192.168.0.154:88/", "Kuchnia"),
//        Device("http://192.168.0.155:88/", "Balkon ")
//    )


    val sensors = emptyList<DeviceDto>()
    val deviceService = DeviceService(DeviceRepository(), sensors)
    val sensorService = SensorService({ deviceService.sensorsList }, SensorClient(), RealDataRepository())
    deviceService.getAllDevices()

    Scheduler(deviceService::getAllDevices).apply {
        scheduleExecutionAtFixedMinutes(listOf(3, 8, 13, 18, 23, 28, 33, 38, 43, 48, 53, 58))
    }

    Scheduler(sensorService::readTemperature).apply {
        scheduleExecutionAtFixedMinutes(listOf(0, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55))
    }


    embeddedServer(Netty, port = 8080, host = "0.0.0.0", watchPaths = listOf("classes")) {
        install(WebSockets) {
            pingPeriod = Duration.ofMinutes(1)
        }
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
            webSocket("/logs") {
                val subscription = logChannel.openSubscription()
                try {
                    subscription.consumeEach { message ->
                        outgoing.send(Frame.Text(message))
                    }
                } finally {
                    subscription.cancel()
                }
            }
        }
        LOGGER.info("Application started")
    }.start(wait = true)
}

