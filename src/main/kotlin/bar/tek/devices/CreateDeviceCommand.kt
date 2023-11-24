package bar.tek.devices

import kotlinx.serialization.Serializable

@Serializable
class CreateDeviceCommand (
    val name: String,
    val ipAddress: String,
)
