package bar.tek.pastData

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import java.time.LocalDateTime

data class PastTemperatureDataMongoDocument(
    @BsonId
    val id: ObjectId,
    val temperature: String,
    val humidity: String,
    val readTime: LocalDateTime,
    val sensorId: String
)