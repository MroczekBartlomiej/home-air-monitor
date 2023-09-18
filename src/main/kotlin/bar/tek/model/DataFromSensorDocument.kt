package bar.tek.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
class DataFromSensorDocument(
    val temperature: String,
    val humidity: String,
    @Contextual
    val readTime: LocalDateTime,
    val sensorId: String
)