package bar.tek.pastData

import io.ktor.util.logging.KtorSimpleLogger


internal val LOGGER = KtorSimpleLogger("bar.tek.service.SensorClient")

class PastTemperatureDataService(private val pastTemperatureDataRepository: PastTemperatureDataRepository) {

    fun getPastData(daysBefore: Long): List<PastTemperatureDataResponse> {
        LOGGER.info("Fetching data from last $daysBefore days.")
        return pastTemperatureDataRepository.getDataFromLastDays(daysBefore)
            .map { document -> PastTemperatureDataResponse(document.readTime.toString(), document.temperature, document.humidity) }
    }
}