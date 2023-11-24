package bar.tek.devices

import io.ktor.util.logging.KtorSimpleLogger
import org.bson.BsonValue
import org.bson.types.ObjectId

val LOGGER = KtorSimpleLogger("bar.tek.devices.DeviceService")

class DeviceService(private val deviceRepository: DeviceRepository) {

    fun getAllDevices(): List<DeviceDocument> {
        LOGGER.info("Fetching all devices")
        return deviceRepository.getDevices()
    }

    fun addNewDevice(createDeviceCommand: CreateDeviceCommand): BsonValue? {
        LOGGER.info("Adding new devices: $createDeviceCommand")
        //TODO: Add checking that device exist in local network
        return deviceRepository.saveDevice(createDeviceCommand)
    }

    fun removeDevice(command: DeleteDeviceCommand): Long {
        LOGGER.info("Removing devices with id: ${command.deviceId}")
        return deviceRepository.deleteDevice(ObjectId(command.deviceId))
    }

    fun updateDevice(command: UpdateDeviceCommand): Long {
        LOGGER.info("Updating devices: $command")
        return deviceRepository.updateDevice(command)
    }
}