package bar.tek.pastData

import kotlinx.serialization.Serializable

@Serializable
data class PastTemperatureDataResponse(
    val label: String,
    val temperature: String,
    val humidity: String
)
