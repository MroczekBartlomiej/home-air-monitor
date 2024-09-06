package bar.tek.devices

import bar.tek.shared.BaseRepository
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import org.bson.BsonValue
import org.bson.types.ObjectId
import java.time.LocalDateTime

class DeviceRepository : BaseRepository() {

    fun saveDevice(command: CreateDeviceCommand): BsonValue? {
        return try {
            val deviceDocument = DeviceDocument(
                id = ObjectId(),
                name = command.name,
                ipAddress = command.ipAddress,
                editDate = LocalDateTime.now(),
                createDate = LocalDateTime.now()
            )
            database.getCollection<DeviceDocument>("devices")
                .insertOne(deviceDocument)
                .insertedId
        } catch (e: Exception) {
            null
        }
    }

    fun getDevices(): List<DeviceDocument> {
        return try {
            database.getCollection<DeviceDocument>("devices")
                .find<DeviceDocument>()
                .toList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun deleteDevice(objectId: ObjectId): Long {
        return try {
            val filters = Filters.eq("_id", objectId)
            database.getCollection<DeviceDocument>("devices")
                .deleteOne(filters)
                .deletedCount
        } catch (e: Exception) {
            0L
        }
    }

    fun updateDevice(command: UpdateDeviceCommand): Long {
        return try {
            val filters = Filters.eq("_id", ObjectId(command.id))
            val update = Updates.combine(
                Updates.set(DeviceDocument::name.name, command.name),
                Updates.set(DeviceDocument::ipAddress.name, command.ipAddress)
            )
            database.getCollection<DeviceDocument>("devices")
                .updateOne(filters, update)
                .modifiedCount
        } catch (e: Exception) {
            0L
        }
    }
}