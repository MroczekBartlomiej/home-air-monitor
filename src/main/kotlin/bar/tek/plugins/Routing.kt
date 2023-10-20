package bar.tek.plugins

import bar.tek.respondCss
import bar.tek.service.DataFromSensorService
import bar.tek.service.SensorService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.html.respondHtml
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import kotlinx.css.Color
import kotlinx.css.backgroundColor
import kotlinx.css.body
import kotlinx.css.color
import kotlinx.css.margin
import kotlinx.css.pre
import kotlinx.css.px
import kotlinx.html.DIV
import kotlinx.html.HEAD
import kotlinx.html.LI
import kotlinx.html.a
import kotlinx.html.body
import kotlinx.html.div
import kotlinx.html.h1
import kotlinx.html.h5
import kotlinx.html.head
import kotlinx.html.li
import kotlinx.html.link
import kotlinx.html.p
import kotlinx.html.script
import kotlinx.html.span
import kotlinx.html.title
import kotlinx.html.ul
import kotlinx.html.unsafe


fun Route.temperature(sensorService: SensorService, dataFromSensorService: DataFromSensorService) {
    get("/temperature") {
        call.respondHtml(HttpStatusCode.OK) {
            val dataFromSensor = sensorService.readTemperature()
            head { title("Home Air Monitor") }
            body {
                h1 { +"Home Air Monitor" }
                p { +"temperature ${dataFromSensor.temperature}" }
                p { +"humidity ${dataFromSensor.humidity}" }
            }
        }
    }

    get("/temperature-read") {
        call.respondHtml(HttpStatusCode.OK) {
            val dataFromSensor = sensorService.savedTemperature()
            head { title("Home Air Monitor") }
        }
    }

//HTML and CSS
    get("/home") {
        call.respondHtml {
            head {
                head()
            }
            body {
                div(classes = "px-3 py-2 text-bg-dark border-bottom", block = header())
            }
        }
    }
    get("/sensors") {
        call.respondHtml {
            head {
                head()
            }
            body {
                div(classes = "px-3 py-2 text-bg-dark border-bottom", block = header())
                div(block = sensorsBody(sensorService))
            }
        }
    }
    get("/graphs") {
        val dataFromSensors = dataFromSensorService.getDataFromSensors(7L)
        val temperature = dataFromSensors.map { dataFromSensorMongoDocument -> dataFromSensorMongoDocument.temperature }
        val labels = dataFromSensors.map { dataFromSensorMongoDocument -> dataFromSensorMongoDocument.readTime.toString() }

        println(temperature.joinToString(prefix = "'", separator = "','", postfix = "'"))
        println(labels.joinToString(prefix = "'", separator = "','", postfix = "'"))

        val label = labels.joinToString(prefix = "'", separator = "','", postfix = "'")
        val data = temperature.joinToString(prefix = "'", separator = "','", postfix = "'")

        call.respondHtml {
            head {
                head()
            }
            body {
                div(classes = "px-3 py-2 text-bg-dark border-bottom", block = header())
                div { h1 { +"graphs" } }
                div(classes = "d-flex flex-wrap align-items-center justify-content-center justify-content-lg-start") {
                    div(classes = "container") {
                        unsafe {
                            +"""
                    <div>
              <canvas id="myChart"></canvas>
            </div>

            <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

            <script>
              const ctx = document.getElementById('myChart');

              new Chart(ctx, {
                type: 'line',
                data: {
                  labels:[$label],
                  datasets: [{
                    data: [$data],
                    borderWidth: 1,
                    tension: 0.5
                  }]
                },
                options: {
                  scales: {
                    y: {
                      beginAtZero: false
                    }
                  }
                }
              });
            </script>
                """.trimIndent()
                        }
                    }
                }
            }
        }
    }

    get("/devices") {
        call.respondHtml {
            head {
                head()
            }
            body {
                div(classes = "px-3 py-2 text-bg-dark border-bottom", block = header())
                div { h1 { +"devices" } }
            }
        }
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

private fun HEAD.head() {
    link(
        href = "/assets/bootstrap/bootstrap.css",
        rel = "stylesheet"
    )
    link(
        href = "/assets/bootstrap-icons/font/bootstrap-icons.css",
        rel = "stylesheet"
    )
    script {
        src = "/assets/bootstrap/js/bootstrap.bundle.min.js"
    }
}


fun sensorsBody(sensorService: SensorService): DIV.() -> Unit = {
    div(classes = "d-flex flex-wrap align-items-center justify-content-center justify-content-lg-start") {
        div(classes = "container") {
            div(
                classes = "d-flex flex-wrap align-items-center justify-content-center justify-content-lg-start",
                block = sensorCard(sensorService)
            )

        }
    }
}

fun sensorCard(sensorService: SensorService): DIV.() -> Unit = {
    val readTemperature = sensorService.readTemperature()
    div(classes = "card-group") {
        div(classes = "card") {
            div(classes = "card-body") {
                h5(classes = "card-title") { +"Sypialnia" }
                p(classes = "card-text") {
                    p(classes = "bi bi-thermometer-half") { +" Temperature: ${readTemperature.temperature}" }
                    p(classes = "bi bi-moisture") { +" Humidity: ${readTemperature.humidity}" }
                }
            }
            div(classes = "card-footer") {
            }
        }
    }
}

fun header(): DIV.() -> Unit = {
    div(classes = "container") {
        div(classes = "d-flex flex-wrap align-items-center justify-content-center justify-content-lg-start") {
            a(
                href = "/home",
                classes = "d-flex align-items-center my-2 my-lg-0 me-lg-auto text-white text-decoration-none"
            ) {
                div(classes = "container") {
                    div(classes = "row") {
                        div(classes = "col align-bottom text-center") {
                            span(classes = "fs-3 bi bi-house")
                        }
                    }
                    div(classes = "row") {
                        div(classes = "col") {
                            span(classes = "fs-6 align-top")
                        }
                    }
                }
            }
            ul(classes = "nav col-12 col-lg-auto my-2 justify-content-center my-md-0 text-small text-whit") {
                li(
                    classes = "nav-link text-secondary",
                    block = navigationItem("bi-speedometer2", "/sensors", "Sensors")
                )
                li(classes = "nav-link text-secondary", block = navigationItem("bi-graph-up", "/graphs", "Graphs"))
                li(classes = "nav-link text-secondary", block = navigationItem("bi-thermometer", "/devices", "Devices"))

            }
        }
    }
}

private fun navigationItem(iconName: String, loacation: String, itemName: String): LI.() -> Unit = {
    a(
        href = loacation,
        classes = "d-flex align-items-center my-2 my-lg-0 me-lg-auto text-white text-decoration-none"
    ) {
        div(classes = "container") {
            div(classes = "row") {
                div(classes = "col align-bottom text-center") {
                    span(classes = "fs-3 bi $iconName")
                }
            }
            div(classes = "row") {
                div(classes = "col") {
                    span(classes = "fs-6 align-top") { +"$itemName" }
                }
            }
        }
    }
}