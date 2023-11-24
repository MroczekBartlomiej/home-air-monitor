package bar.tek.api

import bar.tek.devices.CreateDeviceCommand
import bar.tek.devices.DeleteDeviceCommand
import bar.tek.devices.DeviceResponse
import bar.tek.devices.DeviceService
import bar.tek.devices.UpdateDeviceCommand
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
        val deviceResponseList = deviceService.getAllDevices().map {
            DeviceResponse(id = it.id.toString(), name = it.name, ipAddress = it.ipAddress, editDate = it.editDate, createDate = it.createDate)
        }
        call.respond(status = HttpStatusCode.OK, message = deviceResponseList)
    }

    post("/devices") {
        val command = call.receive<CreateDeviceCommand>()
        val bsonValue = deviceService.addNewDevice(command)
        if (bsonValue != null) {
            call.respond(status = HttpStatusCode.Created, bsonValue.asObjectId().value.toString())
        } else {
            call.respond(status = HttpStatusCode.ExpectationFailed, "")
        }
    }

    put("/devices") {
        val command = call.receive<UpdateDeviceCommand>()
        val updateDevice = deviceService.updateDevice(command)
        if (updateDevice == 1L) {
            call.respond(status = HttpStatusCode.Accepted, "")
        } else {
            call.respond(status = HttpStatusCode.ExpectationFailed, "Device not updated.")
        }
    }

    delete("/devices") {
        val command = call.receive<DeleteDeviceCommand>()
        val removeDevice = deviceService.removeDevice(command)
        if (removeDevice == 1L) {
            call.respond(status = HttpStatusCode.Accepted, "")
        } else {
            call.respond(status = HttpStatusCode.ExpectationFailed, "Device not removed.")
        }
    }
}