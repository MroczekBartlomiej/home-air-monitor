package bar.tek.realTimeData

import bar.tek.pastData.PastTemperatureDataRepository
import bar.tek.pastData.PastTemperatureDataMongoDocument
import kotlinx.coroutines.runBlocking
import org.bson.types.ObjectId
import java.time.LocalDateTime

class SensorService(
    private val sensorClient: SensorClient,
    private val dataFromSensorRepository: PastTemperatureDataRepository
) {

    fun readTemperature(): DataFromSensor {
        val dataFromSensor = runBlocking {
            return@runBlocking sensorClient.callSensor()
        }
        val dataFromSensorDocument =
            PastTemperatureDataMongoDocument(
                ObjectId(),
                dataFromSensor.temperature,
                dataFromSensor.humidity,
                LocalDateTime.now(),
                "sypialnia"
            )
        dataFromSensorRepository.save(dataFromSensorDocument)
        return dataFromSensor
    }

}