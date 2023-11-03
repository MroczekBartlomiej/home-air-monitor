package bar.tek.pastData

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.ServerApi
import com.mongodb.ServerApiVersion
import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.MongoClient
import java.time.LocalDateTime

class PastTemperatureDataRepository {

    fun save(pastTemperatureDataMongoDocument: PastTemperatureDataMongoDocument) {
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
                .getCollection<PastTemperatureDataMongoDocument>("sensorsData")
                .insertOne(pastTemperatureDataMongoDocument)
            mongoClient.close()
        }
    }

    fun getDataFromLastDays(daysBefore: Long): List<PastTemperatureDataMongoDocument> {
        val connectionString =
            "mongodb+srv://tempMonitor:0Q1RW19VVx4m5wn2@temperaturemonitor.tzdorpu.mongodb.net/?retryWrites=true&w=majority&tls=true"
        val serverApi = ServerApi.builder()
            .version(ServerApiVersion.V1)
            .build()
        val mongoClientSettingsEx = MongoClientSettings.builder()
            .applyConnectionString(ConnectionString(connectionString))
            .serverApi(serverApi)
            .build()
        val filters = Filters.gte("readTime", LocalDateTime.now().minusDays(daysBefore))
        MongoClient.create(mongoClientSettingsEx).use { mongoClient ->
            val find = mongoClient.getDatabase("tempMonitor")
                .getCollection<PastTemperatureDataMongoDocument>("sensorsData")
                .find(filters)
            //mongoClient.close()
            return find.toList()

        }
    }

}