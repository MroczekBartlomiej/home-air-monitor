package bar.tek.service

import bar.tek.model.DataFromSensor
import kotlinx.coroutines.runBlocking

class SensorService(private val sensorClient: SensorClient) {

    fun readTemperature(): DataFromSensor {
        return runBlocking {
            return@runBlocking sensorClient.callSensor()
        }
    }
}