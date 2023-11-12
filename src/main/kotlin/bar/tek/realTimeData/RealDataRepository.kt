package bar.tek.realTimeData

import bar.tek.pastData.PastTemperatureDataMongoDocument
import bar.tek.shared.BaseRepository

class RealDataRepository: BaseRepository() {

    fun save(pastTemperatureDataMongoDocument: PastTemperatureDataMongoDocument) {
        database.getCollection<PastTemperatureDataMongoDocument>("sensorsData")
            .insertOne(pastTemperatureDataMongoDocument)
    }
}