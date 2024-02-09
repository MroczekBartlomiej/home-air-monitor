package bar.tek.devices

import kotlinx.serialization.Serializable

@Serializable
data class UpdateDeviceCommand(
    val id: String,
    val name: String,
    val ipAddress: String,
)