package bar.tek.api

import bar.tek.devices.*
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put

fun Route.devicesRouting(deviceService: DeviceService) {

    get("/devices") {
        val deviceResponseList = deviceService.getAllDevices().map { it.toResponse() }
        call.respond(HttpStatusCode.OK, deviceResponseList)
    }

    post("/devices") {
        val command = call.receive<CreateDeviceCommand>()
        when (val result = deviceService.addNewDevice(command)) {
            is Result.Success -> call.respond(HttpStatusCode.Created, result.data)
            is Result.Failure -> call.respond(HttpStatusCode.ExpectationFailed, result.error.message ?: "Unknown error")
        }
    }

    put("/devices") {
        val command = call.receive<UpdateDeviceCommand>()
        when (val result = deviceService.updateDevice(command)) {
            is Result.Success -> call.respond(HttpStatusCode.Accepted, "Device updated successfully.")
            is Result.Failure -> call.respond(HttpStatusCode.ExpectationFailed, result.error.message ?: "Unknown error")
        }
    }

    delete("/devices/{deviceId}") {
        val deviceId = call.parameters["deviceId"]
        if (deviceId != null) {
            when (val result = deviceService.removeDevice(deviceId)) {
                is Result.Success -> call.respond(HttpStatusCode.Accepted, "Device removed successfully.")
                is Result.Failure -> call.respond(HttpStatusCode.ExpectationFailed, result.error.message ?: "Unknown error")
            }
        } else {
            call.respond(HttpStatusCode.BadRequest, "Device ID is missing.")
        }
    }
}

private fun DeviceDocument.toResponse() = DeviceResponse(
    id = this.id.toString(),
    name = this.name,
    ipAddress = this.ipAddress,
    editDate = this.editDate,
    createDate = this.createDate
)