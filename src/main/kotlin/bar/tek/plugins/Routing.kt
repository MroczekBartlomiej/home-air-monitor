package bar.tek.plugins

import bar.tek.service.SensorService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.html.respondHtml
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import kotlinx.html.body
import kotlinx.html.h1
import kotlinx.html.head
import kotlinx.html.p
import kotlinx.html.title


fun Route.temperature(sensorService: SensorService) {
    get("/temperature") {
        call.respondHtml(HttpStatusCode.OK) {
            val dataFromSensor = sensorService.readTemperature()
            head { title("Home Air Monitor") }
            body {
                h1 { +"Home Air Monitor" }
                p { +"temperature ${dataFromSensor.temperature}" }
                p { +"humidity ${dataFromSensor.humidity}" }
            }
        }
    }

    get("/temperature-read") {
        call.respondHtml(HttpStatusCode.OK) {
            val dataFromSensor = sensorService.savedTemperature()
            head { title("Home Air Monitor") }
        }
    }
}