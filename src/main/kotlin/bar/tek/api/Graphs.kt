package bar.tek.api

import bar.tek.pastData.PastTemperatureDataService
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get

fun Route.graphRouting(pastTemperatureDataService: PastTemperatureDataService) {

    get("/graphs/{daysBefore}") {
        val daysBefore = call.parameters["daysBefore"]?.toLong()
        if (daysBefore != null) {
            val dataFromSensors = pastTemperatureDataService.getPastData(daysBefore)
            call.respond(dataFromSensors)

        } else {
            call.respondText { "Dni podaj" }
        }
    }
}