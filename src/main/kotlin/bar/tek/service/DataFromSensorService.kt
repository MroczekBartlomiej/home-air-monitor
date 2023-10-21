package bar.tek.service

import bar.tek.database.SensorDataRepository
import bar.tek.model.DataFromSensorMongoDocument

class DataFromSensorService(private val sensorDataRepository: SensorDataRepository) {

    fun getDataFromSensors(daysBefore: Long): List<DataFromSensorMongoDocument> {
        return sensorDataRepository.getDataFromLastDays(daysBefore)
    }
}