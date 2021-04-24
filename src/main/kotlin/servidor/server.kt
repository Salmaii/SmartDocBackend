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

val sistema:Sistema = Sistema()

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
        listarPacientes()
        listarMedicos()
        listarConsultas()
        listarFuncionarios()

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
        call.respondText("Vem na mao, nao afoba nao")
    }
}

fun Route.cadastroMedico() {
    post("/cadastro/medico"){
        val dadosCadastroMedico = call.receive<Medico>()
        val medicoCadastrado = sistema.cadastroMedico(dadosCadastroMedico.nome!!, dadosCadastroMedico.idade!!, dadosCadastroMedico.cpf!!, dadosCadastroMedico.telefone!!, dadosCadastroMedico.crm!!, dadosCadastroMedico.especializacao!!)
        call.respond(medicoCadastrado)
    }
}

fun Route.listarPacientes(){
    //Listar todos os Pacientes
    get("/pacientes") {
        call.respond(sistema.listPaciente)
    }

    //todo() mostrar um paciente buscando pelo numCartaoConsulta
    val paciente : Paciente = Paciente()

    /*get("/paciente/${paciente.numCartaoConsulta}"){
        for (i in 0 until sistema.listPaciente.size) {
            if(sistema.listPaciente.get(i) == paciente.numCartaoConsulta){
                call.respond(paciente)
            }
        }
    }*/
}
fun Route.listarMedicos(){
    //Listar todos os Medicos
    get("/medicos"){
        call.respond(sistema.listMedico)
    }

    //todo() mostrar um medico buscando pelo crm

}

fun Route.listarConsultas(){
    //Listar todas as Consultas
    get("/consultas") {
        call.respond(sistema.listConsulta)
    }
    //todo() mostrar uma consulta buscando pelo Codigo

}

fun Route.listarFuncionarios(){
    //Listar todos os Funcionarios
    get("/funcionarios"){
        call.respond(sistema.listFuncionario)
    }

    //todo() mostrar um funcionario buscando pela matricula

}