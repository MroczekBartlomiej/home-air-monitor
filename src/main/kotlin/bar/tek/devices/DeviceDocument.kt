package bar.tek.devices

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import java.time.LocalDateTime

data class DeviceDocument(
    @BsonId
    val id: ObjectId,
    val name: String,
    val ipAddress: String,
    val editDate: LocalDateTime,
    val createDate: LocalDateTime
)