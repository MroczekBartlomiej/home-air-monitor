package bar.tek.devices

import bar.tek.shared.BaseRepository

class DeviceRepository : BaseRepository() {


    fun getDevices(): List<DeviceDocument> {
        return try {
            database.getCollection<DeviceDocument>("devices")
                .find<DeviceDocument>()
                .toList()
        } catch (e: Exception) {
            emptyList()
        }
    }
}