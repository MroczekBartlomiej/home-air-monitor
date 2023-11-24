package bar.tek.devices

import kotlinx.serialization.Serializable

@Serializable
data class UpdateDeviceCommand(
    val deviceId: String,
    val name: String,
    val ipAddress: String,
)