package bar.tek.pastData

import bar.tek.shared.BaseRepository
import com.mongodb.client.model.Filters
import java.time.LocalDateTime

class PastTemperatureDataRepository : BaseRepository() {

    fun getDataFromLastDays(daysBefore: Long): List<PastTemperatureDataMongoDocument> {
        val filters = Filters.gte("readTime", LocalDateTime.now().minusDays(daysBefore))
        return database.getCollection<PastTemperatureDataMongoDocument>("sensorsData")
            .find(filters)
            .toList()

    }
}
