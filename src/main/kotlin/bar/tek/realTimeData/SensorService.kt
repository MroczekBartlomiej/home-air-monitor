package bar.tek.realTimeData

import bar.tek.devices.DeviceDto
import bar.tek.pastData.PastTemperatureDataMongoDocument
import java.time.LocalDateTime
import kotlinx.coroutines.runBlocking
import org.bson.types.ObjectId

class SensorService(
    private val getSensorsList: () -> List<DeviceDto>,
    private val sensorClient: SensorClient,
    private val realDataRepository: RealDataRepository
) {

    fun readTemperature(): List<DataFromSensor> {
        val dataFromSensors = mutableListOf<DataFromSensor>()
        val sensorsList = getSensorsList()
        println("sensorList: $sensorsList")
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
                    it.name
                )
            realDataRepository.save(dataFromSensorDocument)
            dataFromSensors.add(dataFromSensor)
        }
        return dataFromSensors
    }


    //New sensors show to height value compared to Xiaomi sensor.
    private fun calibrateHumiditySensor(humidity:String): String {
        return (humidity.toFloat() - 10).toString()
    }

}