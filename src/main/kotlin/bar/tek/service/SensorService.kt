package bar.tek.service

import bar.tek.database.DataFromSensorRepository
import bar.tek.model.DataFromSensor
import bar.tek.model.DataFromSensorDocument
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime

class SensorService(private val sensorClient: SensorClient, private val dataFromSensorRepository: DataFromSensorRepository) {

    fun readTemperature(): DataFromSensor {
        val dataFromSensor = runBlocking {
            return@runBlocking sensorClient.callSensor()
        }
        val dataFromSensorDocument =
            DataFromSensorDocument(dataFromSensor.temperature, dataFromSensor.humidity, LocalDateTime.now(), "1")
        dataFromSensorRepository.saveTemperature(dataFromSensorDocument)
        return dataFromSensor
    }

    fun savedTemperature(){
        dataFromSensorRepository.readLastTemperature()
    }
}