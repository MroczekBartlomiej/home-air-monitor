package bar.tek.plugins

import bar.tek.respondCss
import bar.tek.service.SensorClient
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.html.respondHtml
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import kotlinx.css.Color
import kotlinx.css.backgroundColor
import kotlinx.css.body
import kotlinx.css.color
import kotlinx.css.margin
import kotlinx.css.px
import kotlinx.html.body
import kotlinx.html.h1
import kotlinx.html.head
import kotlinx.html.title

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondHtml(HttpStatusCode.OK) {
                head { title("Home Air Monitor") }
                body { h1 { +"Home Air Monitor" } }
            }
            val sensorClient = SensorClient()
            val callSensor = sensorClient.callSensor()
            println("===============================")
            println(callSensor.toString())
            println("===============================")

        }

        get("/styles.css") {
            call.respondCss {
                body {
                    backgroundColor = Color.darkBlue
                    margin(0.px)
                }
                rule("h1.page-title") {
                    color = Color.white
                }
            }
        }
    }
}


