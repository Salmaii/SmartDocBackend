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
import pessoa.funcionario.Funcionario
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

fun Route.listarPacientes(){
    //Listar todos os Pacientes
    get("/pacientes") {
        call.respond(sistema.listPaciente)
    }

    //Procurar por id(numCartaiConsulta)

    val paciente : Paciente = Paciente()

    get("/paciente/${paciente.numCartaoConsulta}"){
        for (i in 0 until sistema.listPaciente.size) {
            if(sistema.listPaciente[i].numCartaoConsulta == paciente.numCartaoConsulta){
                call.respond(paciente)
            }
        }
    }
}

fun Route.listarMedicos(){
    //Listar todos os Medicos
    get("/medicos"){
        call.respond(sistema.listMedico)
    }

    val medico:Medico = Medico()

    //Procurar por id(crm)

    get("/medico/${medico.crm}"){
        for(i in 0 until sistema.listMedico.size){
            if(sistema.listMedico[i].crm == medico.crm){
                call.respond(medico)
            }
        }
    }
}

fun Route.listarConsultas(){
    //Listar todas as Consultas
    get("/consultas") {
        call.respond(sistema.listConsulta)
    }

    val consulta: Consulta = Consulta()

    //Procurar por id(codigo)

    get("/Consulta/${consulta.codigo}"){
        for(i in 0 until sistema.listConsulta.size){
            if(sistema.listConsulta[i].codigo == consulta.codigo){
                call.respond(consulta)
            }
        }
    }
}

fun Route.listarFuncionarios(){
    //Listar todos os Funcionarios
    get("/funcionarios"){
        call.respond(sistema.listFuncionario)
    }

    val funcionario:Funcionario = Funcionario()

    //Procurar por id(matricula)

    get("/funcionario/${funcionario.matricula}"){
        for(i in 0 until sistema.listFuncionario.size){
            if(sistema.listFuncionario[i].matricula == funcionario.matricula){
                call.respond(funcionario)
            }
        }
    }
}