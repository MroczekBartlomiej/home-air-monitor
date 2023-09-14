package bar.tek.service

import bar.tek.model.DataFromSensor
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import io.ktor.client.request.get


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
        val body = client.get("http://192.168.0.114:88/").body<DataFromSensor>()
        return body
    }
}