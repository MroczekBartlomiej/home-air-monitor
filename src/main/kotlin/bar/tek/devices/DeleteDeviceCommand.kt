package bar.tek.devices

import kotlinx.serialization.Serializable

@Serializable
class DeleteDeviceCommand(val deviceId: String)