package bar.tek

import bar.tek.database.DataFromSensorRepository
import bar.tek.database.DatabaseConfiguration
import bar.tek.plugins.temperature
import bar.tek.service.SensorClient
import bar.tek.service.SensorService
import io.ktor.http.ContentType
import io.ktor.server.application.ApplicationCall
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.response.respondText
import io.ktor.server.routing.routing
import kotlinx.css.CssBuilder

fun main() {
    val firebaseApp = DatabaseConfiguration().initDatabase()
    val sensorService = SensorService(SensorClient(), DataFromSensorRepository(firebaseApp))
    embeddedServer(Netty, port = 8080) {
        routing {
            temperature(sensorService)
        }
    }
        .start(wait = true)
}

suspend inline fun ApplicationCall.respondCss(builder: CssBuilder.() -> Unit) {
    this.respondText(CssBuilder().apply(builder).toString(), ContentType.Text.CSS)
}
