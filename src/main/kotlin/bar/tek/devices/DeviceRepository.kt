package bar.tek.devices

import bar.tek.shared.BaseRepository
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import org.bson.BsonValue
import org.bson.types.ObjectId
import java.time.LocalDateTime

class DeviceRepository: BaseRepository() {

    fun saveDevice(command: CreateDeviceCommand): BsonValue? {
        val deviceDocument = DeviceDocument(
            id = ObjectId(),
            name = command.name,
            ipAddress = command.ipAddress,
            editDate = LocalDateTime.now(),
            createDate = LocalDateTime.now()
        )
        return database.getCollection<DeviceDocument>("devices")
            .insertOne(deviceDocument)
            .insertedId
    }

    fun getDevices(): List<DeviceDocument> {
        return database.getCollection<DeviceDocument>("devices")
            .find<DeviceDocument>()
            .toList()
    }

    fun deleteDevice(objectId: ObjectId): Long {
        val filters = Filters.eq("_id", objectId)
        return database.getCollection<DeviceDocument>("devices")
            .deleteOne(filters)
            .deletedCount
    }

    fun updateDevice(command: UpdateDeviceCommand): Long {
        val filters = Filters.eq("_id", ObjectId(command.id))
        val update = Updates.combine(
            Updates.set(DeviceDocument::name.name, command.name),
            Updates.set(DeviceDocument::ipAddress.name, command.ipAddress)
        )
        return database.getCollection<DeviceDocument>("devices")
            .updateOne(filters, update)
            .modifiedCount
    }
}