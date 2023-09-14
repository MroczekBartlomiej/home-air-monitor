package bar.tek.model

import kotlinx.serialization.Serializable

@Serializable
data class DataFromSensor(
    val temperature: String,
    val humidity: String
)
