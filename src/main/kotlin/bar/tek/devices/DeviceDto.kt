package bar.tek.devices

import kotlinx.serialization.Serializable

@Serializable
data class DeviceDto(
    val id: String,
    val name: String,
    val enabled: Boolean,
    val ipAddress: String,

    )
