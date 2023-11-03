package bar.tek.pastData

class PastTemperatureDataService(private val pastTemperatureDataRepository: PastTemperatureDataRepository) {

    fun getPastData(daysBefore: Long): List<PastTemperatureDataMongoDocument> {
        return pastTemperatureDataRepository.getDataFromLastDays(daysBefore)
    }
}