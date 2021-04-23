package servidor

import Consulta
import Sistema
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.html.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.html.*
import org.slf4j.event.Level
import pessoa.medico.Medico
import pessoa.paciente.Paciente

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

val sistema = Sistema()

/**
 * Configura um módulo do servidor. Cada módulo pode ter vários endpoints (endereços) e podem ser
 * "anexados" ao servidor pelas configurações do arquivo "application.conf" na pasta RESOURCES do
 * projeto.
 */
@Suppress("unused")
@kotlin.jvm.JvmOverloads
fun Application.sistema(testing: Boolean = false) {
    /**
     * Configura o servidor para "imprimir" no log as mensagens e textos do servidor
     */
    install(CallLogging) {
        level = Level.INFO
        filter { call -> call.request.path().startsWith("/") }
    }

    /**
     * Configura o servidor para "entender" Json e converter para um objeto (instância de uma classe)
     */
    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
            disableHtmlEscaping()
        }
    }

    /**
     * Configura as rotas do servidor, ou seja, os endpoints ou endereços disponíveis para cada
     * operação no servidor
     */
    routing {
        meuindex()
        criarConsulta()
        cadastroPaciente()
        cadastroMedico()
//        depositar()
//        listarContas()
    }
}

fun Route.meuindex() {
    get("/") {
        call.respondHtml {
            body {
                h1 { +"SmartDoc"}
                p { +"Tente chamar os outros endpoints para executar operações" }
                ul {
                    ol { +"POST - /consulta               - Cria consulta" }
                    ol { +"POST - /cadastro/paciente      - Cadastro de paciente" }
                    ol { +"POST - /cadastro/medico        - Cadastro de medico" }
                    ol { +"GET - /contas                  - Listar todas as contas"}
                }
            }
        }
    }
}

fun Route.criarConsulta() {
    post("/consulta"){
        val dadosConsulta = call.receive<Consulta>()
        val consultaCriada = sistema.Marcar(dadosConsulta.paciente!!, dadosConsulta.medico!!, dadosConsulta.local!!, dadosConsulta.data!!, dadosConsulta.hora!!, dadosConsulta.motivo!!)
        call.respond(consultaCriada)
    }
}

fun Route.cadastroPaciente() {
    post("/cadastro/paciente"){
        val dadosCadastroPaciente = call.receive<Paciente>()
        val pacienteCadastrado = sistema.cadastroPaciente(dadosCadastroPaciente.nome!!, dadosCadastroPaciente.idade!!, dadosCadastroPaciente.cpf!!, dadosCadastroPaciente.telefone!!, dadosCadastroPaciente.numCartaoConsulta!!)
        call.respond(pacienteCadastrado)
    }
}

fun Route.cadastroMedico() {
    post("/cadastro/medico"){
        val dadosCadastroMedico = call.receive<Medico>()
        val medicoCadastrado = sistema.cadastroMedico(dadosCadastroMedico.nome!!, dadosCadastroMedico.idade!!, dadosCadastroMedico.cpf!!, dadosCadastroMedico.telefone!!, dadosCadastroMedico.crm!!, dadosCadastroMedico.especializacao!!)
        call.respond(medicoCadastrado)
    }
}

//fun Route.depositar() {
//    post("/contas/corrente/depositar"){
//        val deposito = call.receive<Deposito>()
//        banco.depositar(deposito.tipoConta, deposito.agencia, deposito.conta, deposito.valor)
//        call.respondText { "DEPÓSITO EFETUADO COM SUCESSO" }
//    }
//}
//
//fun Route.listarContas() {
//    get("/contas") {
//        call.respond(banco.contasCorrente)
//    }
//}