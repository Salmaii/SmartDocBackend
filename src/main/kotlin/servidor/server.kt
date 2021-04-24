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

        //Cadastro

        criarConsulta()
        cadastroPaciente()
        cadastroMedico()
        cadastroFuncionario()

        //Listagem

        listarPacientes()
        listarMedicos()
        listarConsultas()
        listarFuncionarios()

        //Procurar

        procurarPaciente()
        procurarMedico()
        procurarConsulta()
        procurarFuncionario()

        //Deletar

        deletarConsultas()
        deletarConsultaId()
        deletarMedicos()
        deletarMedicoId()
        deletarPacientes()
        deletarPacienteId()
        deletarFuncionarios()
        deletarFuncioanrioId()

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
        val medicoCadastrado = sistema.cadastroMedico(dadosCadastroMedico.nome!!, dadosCadastroMedico.idade!!,
            dadosCadastroMedico.cpf!!, dadosCadastroMedico.telefone!!, dadosCadastroMedico.crm!!, dadosCadastroMedico.especializacao!!)
        call.respond(medicoCadastrado)
    }
}

fun Route.cadastroFuncionario() {
    post("/cadastro/funcionario"){
        val dadosCadastroFuncionario = call.receive<Funcionario>()
        val funcionarioCriado = sistema.cadastroFuncionario(dadosCadastroPaciente.nome!!, dadosCadastroPaciente.idade!!, dadosCadastroPaciente.cpf!!, dadosCadastroPaciente.telefone!!, dadosCadastroPaciente.numCartaoConsulta!!)
        call.respond(pacienteCadastrado)
    }
}

//criando consulta
fun Route.criarConsulta() {
    post("/consulta"){
        var dadosConsulta = call.receive<Consulta>()
        val consultaCriada = sistema.Marcar(dadosConsulta.paciente!!, dadosConsulta.medico!!, dadosConsulta.local!!, dadosConsulta.data!!, dadosConsulta.hora!!, dadosConsulta.motivo!!)
        call.respond(consultaCriada)
    }
}

    //Listar todos os Pacientes

fun Route.listarPacientes() {
    get("/pacientes") {
        call.respond(sistema.listPaciente)
    }
}

    //Procurar por id(numCartaiConsulta)

fun Route.procurarPaciente(){
    get("/paciente/{numCartaoConsulta}"){
        var numCartaoConsulta = call.parameters["numCartaoConsulta"]
        if(numCartaoConsulta != null){
            for (i in 0 until sistema.listPaciente.size) {
                if(sistema.listPaciente[i].numCartaoConsulta == numCartaoConsulta){
                    call.respond(sistema.listPaciente[i])
                }
            }
        }else{
            call.respondText {"Cartão de Consulta invalida"}
        }
    }
}

    //Listar todos os Medicos

fun Route.listarMedicos(){
    get("/medicos"){
        call.respond(sistema.listMedico)
    }
}

    //Procurar por id(crm)

fun Route.procurarMedico(){
    get("/medicos/{crm?}"){
        var crm = call.parameters["crm"]
        if(crm != null) {
            for (i in 0 until sistema.listMedico.size) {
                if (sistema.listMedico[i].crm.toString() == crm) {
                    call.respond(sistema.listMedico[i])
                }
            }
        }else{
            call.respondText { "Crm invalido" }
        }
    }
}

    //Listar todas as Consultas

fun Route.listarConsultas() {

    get("/consultas") {
        call.respond(sistema.listConsulta)
    }
}

    //Procurar por id(codigo)

fun Route.procurarConsulta(){
    get("/Consulta/{codigo?}"){
        var codigo = call.parameters["codigo"]
        if(codigo != null){
            for (i in 0 until sistema.listConsulta.size) {
                if (sistema.listConsulta[i].codigo == codigo) {
                    call.respond(sistema.listConsulta[i])
                }
            }
        } else {
            call.respondText { "Codigo de consulta invalido" }
        }
    }
}

    //Listar todos os Funcionarios

fun Route.listarFuncionarios() {
    get("/funcionarios") {
        call.respond(sistema.listFuncionario)
    }
}

    //Procurar por id(matricula)

fun Route.procurarFuncionario() {
    get("/funcionario/{matricula?}"){
        var matricula = call.parameters["matricula"]
        if(matricula != null){
            for (i in 0 until sistema.listFuncionario.size) {
                if (sistema.listFuncionario[i].matricula == matricula) {
                    call.respond(sistema.listFuncionario[i])
                }
            }
        }else{
            call.respondText {"Matricula invalidade"}
        }
    }
}

    //deletar todas as consultas
fun Route.deletarConsultas() {
    delete("/consultas") {
        call.respond(sistema.listConsulta)
    }
}

//deletar consulta por id (codigo)
fun Route.deletarConsultaId(){
    delete("/Consulta/{codigo?}"){
        var codigo = call.parameters["codigo"]
        if(codigo != null){
            for (i in 0 until sistema.listConsulta.size) {
                if (sistema.listConsulta[i].codigo == codigo) {
                    call.respond(sistema.listConsulta[i])
                }
            }
        } else {
            call.respondText { "Codigo de consulta invalido" }
        }
    }
}

//deletar todos os medicos
fun Route.deletarMedicos() {
    delete("/medicos") {
        call.respond(sistema.listMedico)
    }
}

//deletar medicos por id (crm)
fun Route.deletarMedicoId(){
    delete ("/medicos/{crm?}"){
        var crm = call.parameters["crm"]
        if(crm != null) {
            for (i in 0 until sistema.listMedico.size) {
                if (sistema.listMedico[i].crm.toString() == crm) {
                    call.respond(sistema.listMedico[i])
                }
            }
        }else{
            call.respondText { "Crm invalido" }
        }
    }
}

//deletar todos os pacientes
fun Route.deletarPacientes(){
    delete("/pacientes") {
        call.respond(sistema.listPaciente)
    }
}

//deletar pacientes por id (numCartaoConsulta)
fun Route.deletarPacienteId(){
    get("/paciente/{numCartaoConsulta}"){
        var numCartaoConsulta = call.parameters["numCartaoConsulta"]
        if(numCartaoConsulta != null){
            for (i in 0 until sistema.listPaciente.size) {
                if(sistema.listPaciente[i].numCartaoConsulta == numCartaoConsulta){
                    call.respond(sistema.listPaciente[i])
                }
            }
        }else{
            call.respondText {"Cartão de Consulta invalida"}
        }
    }
}

//deletar todos os funcionarios
fun Route.deletarFuncionarios() {
    delete("/funcionarios") {
        call.respond(sistema.listFuncionario)
    }
}

//deletar funcionarios por id (matricula)
fun Route.deletarFuncioanrioId(){
    delete("/paciente/{numCartaoConsulta}"){
        var numCartaoConsulta = call.parameters["numCartaoConsulta"]
        if(numCartaoConsulta != null){
            for (i in 0 until sistema.listPaciente.size) {
                if(sistema.listPaciente[i].numCartaoConsulta == numCartaoConsulta){
                    call.respond(sistema.listPaciente[i])
                }
            }
        }else{
            call.respondText {"Cartão de Consulta invalida"}
        }
    }
}