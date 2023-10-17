package bar.tek.service

import bar.tek.model.DataFromSensor
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import io.ktor.client.request.get
import io.ktor.util.logging.KtorSimpleLogger

internal val LOGGER = KtorSimpleLogger("com.example.RequestTracePlugin")

class SensorClient(
    val client: HttpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }
    }
) {

    suspend fun callSensor(): DataFromSensor {
        LOGGER.info("Reading temperature from sensor.")
        val body = client.get("http://192.168.0.114:88/")
            .also { LOGGER.info("Temperature reading completed with status: ${it.status.value } : ${it.status.description } ") }
            .body<DataFromSensor>()
        return body
    }
}