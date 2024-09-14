package bar.tek.realTimeData

import bar.tek.devices.DeviceDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.logging.KtorSimpleLogger
import kotlinx.serialization.json.Json

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

    suspend fun callSensor(sensor: DeviceDto): DataFromSensor {
        LOGGER.info("Reading temperature from sensor ${sensor.name} IP: ${sensor.ipAddress}")
        val body = client.get(sensor.ipAddress)
            .also { LOGGER.info("Temperature reading completed with status: ${it.status.value } : ${it.status.description } ") }
            .body<DataFromSensor>()
        return body
    }
}