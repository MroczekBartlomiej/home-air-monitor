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

    fun addNewDevice(createDeviceCommand: CreateDeviceCommand): Result<BsonValue> {
        return try {
            LOGGER.info("Adding new device: $createDeviceCommand")
            val result = deviceRepository.saveDevice(createDeviceCommand)
            if (result != null) {
                Result.Success(result)
            } else {
                Result.Failure(Exception("Failed to add device"))
            }
        } catch (e: Exception) {
            LOGGER.error("Error adding device", e)
            Result.Failure(e)
        }
    }

    fun removeDevice(deviceId: String): Result<Long> {
        return try {
            LOGGER.info("Removing device with id: $deviceId")
            val result = deviceRepository.deleteDevice(ObjectId(deviceId))
            if (result == 1L) {
                Result.Success(result)
            } else {
                Result.Failure(Exception("Failed to remove device"))
            }
        } catch (e: Exception) {
            LOGGER.error("Error removing device", e)
            Result.Failure(e)
        }
    }

    fun updateDevice(command: UpdateDeviceCommand): Result<Long> {
        return try {
            LOGGER.info("Updating device: $command")
            val result = deviceRepository.updateDevice(command)
            if (result == 1L) {
                Result.Success(result)
            } else {
                Result.Failure(Exception("Failed to update device"))
            }
        } catch (e: Exception) {
            LOGGER.error("Error updating device", e)
            Result.Failure(e)
        }
    }
}

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Failure(val error: Throwable) : Result<Nothing>()
}