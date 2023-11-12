package bar.tek.shared

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.ServerApi
import com.mongodb.ServerApiVersion
import com.mongodb.kotlin.client.MongoClient
import com.mongodb.kotlin.client.MongoDatabase
import io.ktor.util.logging.KtorSimpleLogger


internal val LOGGER = KtorSimpleLogger("bar.tek.shared.BaseRepository")

open class BaseRepository {

    companion object {
        private var db: MongoDatabase? = null
        val database = database()
        private fun database(): MongoDatabase {
            LOGGER.debug("Fetching database client.")
            return db ?: synchronized(this) {
                db ?: createConnection().also { db = it }
            }
        }

        private fun createConnection(): MongoDatabase {
            LOGGER.info("Creating database client.")
            val connectionString =
                "mongodb+srv://tempMonitor:0Q1RW19VVx4m5wn2@temperaturemonitor.tzdorpu.mongodb.net/?retryWrites=true&w=majority&tls=true"
            val serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build()
            val mongoClientSettingsEx = MongoClientSettings.builder()
                .applyConnectionString(ConnectionString(connectionString))
                .serverApi(serverApi)
                .build()
            val db: MongoDatabase = MongoClient.create(mongoClientSettingsEx).getDatabase("tempMonitor")
            LOGGER.info("Database client created.")
            return db
        }
    }
}