package bar.tek.realTimeData

import bar.tek.devices.DeviceDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.logging.KtorSimpleLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory

internal val LOGGER = KtorSimpleLogger("bar.tek.realTimeData.SensorClient")
//internal val LOGGER = LoggerFactory.getLogger(SensorClient::class.java)


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
        return withContext(Dispatchers.IO) {
            LOGGER.info("Reading temperature from sensor ${sensor.name} IP: ${sensor.ipAddress}")
            val body = client.get(sensor.ipAddress)
                .also { LOGGER.info("Temperature reading completed with status: ${it.status.value} : ${it.status.description}") }
                .body<DataFromSensor>()
            body
        }
    }
}