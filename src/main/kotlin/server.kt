import io.ktor.application.*
import io.ktor.features.*
import io.ktor.html.respondHtml
import io.ktor.http.HttpStatusCode
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.server.engine.embeddedServer
import kotlinx.html.*
import org.slf4j.event.Level
import io.ktor.server.netty.*


/*
fun HTML.index() {
    head {
        title("Hello from Ktor!")
    }
    body {
        div {
            +"Hello from Ktor"
        }
    }
}
*/


fun main(args: Array<String>): Unit = EngineMain.main(args)

val consulta = Consulta()

fun Route.cadastroPaciente(){
    post(path:"/sistema"){
        val pacienteParaCriar = call.receive<cadastroPaciente>()
        call.respond()
    }
}

/*
fun main() {
    embeddedServer(Netty, port = 8080, host = "127.0.0.1") {
        routing {
            cadastroPaciente()
            get("/") {
                call.respondHtml(HttpStatusCode.OK, HTML::index)
            }
        }
    }.start(wait = true)
}


*/





