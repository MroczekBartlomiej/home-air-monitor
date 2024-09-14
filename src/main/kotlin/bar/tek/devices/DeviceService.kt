package bar.tek.devices

import io.ktor.util.logging.KtorSimpleLogger

val LOGGER = KtorSimpleLogger("bar.tek.devices.DeviceService")

class DeviceService(private val deviceRepository: DeviceRepository) {

    fun getAllDevices(): List<DeviceDto> {
        LOGGER.info("Fetching all devices")
        return deviceRepository.getDevices()
            .map { it.toDto() }
    }

    private fun DeviceDocument.toDto() = DeviceDto(
        id = id.toHexString(),
        name = name,
        enabled = enabled,
        ipAddress = ipAddress
    )

}

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Failure(val error: Throwable) : Result<Nothing>()
}