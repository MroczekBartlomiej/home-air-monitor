package bar.tek.realTimeData

import bar.tek.Device
import bar.tek.pastData.PastTemperatureDataMongoDocument
import kotlinx.coroutines.runBlocking
import org.bson.types.ObjectId
import java.time.LocalDateTime

class SensorService(
    private val sensorsList: List<Device>,
    private val sensorClient: SensorClient,
    private val realDataRepository: RealDataRepository
) {

    fun readTemperature(): List<DataFromSensor> {
        val dataFromSensors = mutableListOf<DataFromSensor>()
        sensorsList.forEach {
            val dataFromSensor = runBlocking {
                return@runBlocking sensorClient.callSensor(it)
            }
            val dataFromSensorDocument =
                PastTemperatureDataMongoDocument(
                    ObjectId(),
                    dataFromSensor.temperature,
                    calibrateHumiditySensor(dataFromSensor.humidity),
                    LocalDateTime.now(),
                    it.deviceName
                )
            realDataRepository.save(dataFromSensorDocument)
            dataFromSensors.add(dataFromSensor)
        }
        return dataFromSensors
    }


    //New sensors show to height value compared to Xiaomi sensor.
    private fun calibrateHumiditySensor(humidity:String): String {
        return (humidity.toInt() - 10).toString()
    }

}