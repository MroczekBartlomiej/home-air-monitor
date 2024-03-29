package bar.tek.realTimeData

import bar.tek.Device
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import io.ktor.client.request.get
import io.ktor.util.logging.KtorSimpleLogger

internal val LOGGER = KtorSimpleLogger("bar.tek.service.SensorClient")

class SensorClient(
    private val client: HttpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }
    }
) {

    suspend fun callSensor(sensor: Device): DataFromSensor {
        LOGGER.info("Reading temperature from sensor ${sensor.deviceName} IP: ${sensor.deviceIp}")
        val body = client.get(sensor.deviceIp)
            .also { LOGGER.info("Temperature reading completed with status: ${it.status.value } : ${it.status.description } ") }
            .body<DataFromSensor>()
        return body
    }
}