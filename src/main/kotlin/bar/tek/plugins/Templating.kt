package bar.tek.plugins

import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.routing.*
import kotlinx.css.body
import kotlinx.css.h1
import kotlinx.html.body
import kotlinx.html.h1
import kotlinx.html.head
import kotlinx.html.link

fun Application.configureTemplating() {
    routing {
        get("/html-dsl") {
            call.respondHtml {
                head {
                    link(rel = "stylesheet", href = "/styles.css", type = "text/css")
                }
                body {
                    h1(classes = "page-title") {
                        +"Hello from Ktor!"
                    }
                }
            }
        }
    }
}
