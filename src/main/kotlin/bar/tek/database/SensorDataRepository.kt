package bar.tek.database

import bar.tek.model.DataFromSensorMongoDocument
import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.ServerApi
import com.mongodb.ServerApiVersion
import com.mongodb.kotlin.client.MongoClient

class SensorDataRepository {

    fun save(dataFromSensorMongoDocument: DataFromSensorMongoDocument) {
        val connectionString =
            "mongodb+srv://tempMonitor:0Q1RW19VVx4m5wn2@temperaturemonitor.tzdorpu.mongodb.net/?retryWrites=true&w=majority&tls=true"
        val serverApi = ServerApi.builder()
            .version(ServerApiVersion.V1)
            .build()
        val mongoClientSettingsEx = MongoClientSettings.builder()
            .applyConnectionString(ConnectionString(connectionString))
            .serverApi(serverApi)
            .build()
        MongoClient.create(mongoClientSettingsEx).use { mongoClient ->
            mongoClient.getDatabase("tempMonitor")
                .getCollection<DataFromSensorMongoDocument>("sensorsData")
                .insertOne(dataFromSensorMongoDocument)
            mongoClient.close()
        }
    }

}