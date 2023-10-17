package bar.tek.service

import bar.tek.database.SensorDataRepository
import bar.tek.model.DataFromSensor
import bar.tek.model.DataFromSensorMongoDocument
import kotlinx.coroutines.runBlocking
import org.bson.types.ObjectId
import java.time.LocalDateTime

class SensorService(
    private val sensorClient: SensorClient,
    private val dataFromSensorRepository: SensorDataRepository
) {

    fun readTemperature(): DataFromSensor {
        val dataFromSensor = runBlocking {
            return@runBlocking sensorClient.callSensor()
        }
        val dataFromSensorDocument =
            DataFromSensorMongoDocument(
                ObjectId(),
                dataFromSensor.temperature,
                dataFromSensor.humidity,
                LocalDateTime.now(),
                "sypialnia"
            )
        dataFromSensorRepository.save(dataFromSensorDocument)
        return dataFromSensor
    }

    fun savedTemperature() {
        //dataFromSensorRepository.readLastTemperature()
    }
}