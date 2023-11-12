package bar.tek.pastData

import io.ktor.util.logging.KtorSimpleLogger


internal val LOGGER = KtorSimpleLogger("bar.tek.service.SensorClient")

class PastTemperatureDataService(private val pastTemperatureDataRepository: PastTemperatureDataRepository) {

    fun getPastData(daysBefore: Long): List<PastTemperatureDataMongoDocument> {
        LOGGER.info("Fetching data from last $daysBefore days.")
        return pastTemperatureDataRepository.getDataFromLastDays(daysBefore)
    }
}