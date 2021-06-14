package servidor

import Consulta
import Sistema
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.html.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.html.*
import org.slf4j.event.Level
import perfil.Perfil
import perfil.funcionario.Funcionario
import perfil.medico.Medico
import perfil.paciente.Paciente

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

        cadastroConsulta()

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

        //Update

        updatePaciente()
        updateMedico()
        updateFuncionario()

        //Confere login e mostra rotas
        perfil()

        //Logins e cadastros especificos
        perfilFuncionario()
        perfilMedico()
        perfilPaciente()

    }
}

fun Route.meuindex() {
    get("/") {
        call.respondHtml {
            head{
                title{ +"SmartDoc"}
            }
            body {
                h1 { +"SmartDoc"}
                p { +"Tente chamar os outros endpoints para executar operações" }
                ul {
                    ol { +"POST - /cadastro/paciente               - Cadastro de paciente" }
                    ol { +"POST - /cadastro/medico                 - Cadastro de medico" }
                    ol { +"POST - /cadastro/funcionario            - Cadastro de funcionario"}
                    ol { +"POST - /cadastro/consulta               - Cadastro de consulta" }
                    ol { +"GET  - /pacientes                       - Listar todos os pacientes"}
                    ol { +"GET  - /paciente/{numCartaoConsulta}    - Procurar por id{numCartaoConsulta}"}
                    ol { +"GET  - /medicos                         - Listar todos os medicos"}
                    ol { +"GET  - /medicos/{crm?}                  - Procurar por id{crm}"}
                    ol { +"GET  - /consultas                       - Listar todas as consultas"}
                    ol { +"GET  - /consulta/{codigo?}              - Procurar por id{codigo?}"}
                    ol { +"GET  - /funcionarios                    - Listar todos os funcionarios"}
                    ol { +"GET  - /funcionario/{matricula?}        - Procurar por id{matricula}"}
                    ol { +"DELETE - /consultas                     - Deletar todas as consultas"}
                    ol { +"DELETE - /consulta/{codigo?}            - Deletar consulta por id{codigo}"}
                    ol { +"DELETE - /medicos                       - Deletar todos os medicos"}
                    ol { +"DELETE - /medicos/{crm?}                - Deletar medicos por id{}crm"}
                    ol { +"DELETE - /pacientes                     - Deletar todos os pacientes"}
                    ol { +"DELETE - /paciente/{numCartaoConsulta?} - Deletar pacientes por id{numCartaoConsulta}"}
                    ol { +"DELETE - /funcionarios                  - Deletar todos os funcionarios"}
                    ol { +"DELETE - /funcionario/{matricula?}      - Deletar funcionarios por id{matricula}"}
                    ol { +"UPDATE - /consulta/{codigo?}            - Update de consultas por id{codigo}"}
                    ol { +"UPDATE - /paciente/{numCartaoConsulta?} - Update de pacientes por id{numeroCartaoConsulta}"}
                    ol { +"UPDATE - /medico/{crm?}                 - Update de medicos por id{crm}"}
                    ol { +"UPDATE - /funcionario/{matricula?}      - Update de funcionarios por id{matricula}"}
                }
            }
        }
    }
}

//Login

fun Route.perfil() {

    //Verifica se tem perfil logado
    get("/perfil") {
        val usuario = sistema.currentProfile
        if (usuario == null) {
            val error = ServerError(
                HttpStatusCode.NotFound.value, "Usuário não encontrado. Efetue login para " +
                        "continuar!"
            )
            call.respond(HttpStatusCode.NotFound, error)
        } else {
            call.respond(HttpStatusCode.OK, usuario)
        }
    }

    //Rota de cadastros gerais

    post("/perfil/cadastro") {
        val tipo: String = call.receive()
        if (tipo == "Paciente") {
            call.respondText("Vá para a rota /cadastro/paciente")
        }
        if (tipo == "Medidco") {
            call.respondText("Vá para a rota /cadastro/medico")
        }
        if (tipo == "Funcionario") {
            call.respondText("Vá para a rota /cadastro/funcionario")
        }
    }

    //Rota de login geral

    post("/perfil/login") {
        val tipo: String = call.receive()
        if (tipo == "Paciente") {
            call.respondText("Vá para a rota /login/paciente")
        }
        if (tipo == "Medidco") {
            call.respondText("Vá para a rota /login/medico")
        }
        if (tipo == "Funcionario") {
            call.respondText("Vá para a rota /login/funcionario")
        }
    }
}

//TODO testes, cada tipo de perfil com as suas rotas especificas

fun Route.perfilFuncionario() {

    post("/perfil/cadastro/funcionario") {

        val dadosCadastroFuncionario = call.receive<Funcionario>()

        val funcionarioCriado = sistema.cadastroFuncionario(
            dadosCadastroFuncionario.nome!!, dadosCadastroFuncionario.idade!!,
            dadosCadastroFuncionario.cpf!!, dadosCadastroFuncionario.telefone!!, dadosCadastroFuncionario.matricula!!,
            dadosCadastroFuncionario.email!!, dadosCadastroFuncionario.nomeUsuario!!
        )

        call.respond(funcionarioCriado)

        call.respond(HttpStatusCode.Created)
    }

    post("/perfil/login/funcionario") {
        val perfil = call.receive<Funcionario>()
        // Verifica se o e-mail foi envidado na requisição
        if (perfil.email == null) {
            val error = ServerError(HttpStatusCode.BadRequest.value, "O E-MAIL é obrigatório efetuar login!")
            call.respond(HttpStatusCode.BadRequest, error)
            return@post
        }

        val result = sistema.login(perfil.email!!)

        if (!result) {
            val error = ServerError(
                HttpStatusCode.BadRequest.value, "Não foi possível efetuar login." +
                        " Por favor verifique os dados."
            )
            call.respond(HttpStatusCode.BadRequest, error)
            return@post
        }

        call.respond(HttpStatusCode.NoContent)
    }

    /**TODO Funções de deletar
     * TODO Funções de Atualizar parcialmente(sem o id)
     * TODO Funções de cadastro de consulta
     * TODO Alteraçoes na classe Consulta, retirar as instancias e colocar só os ids
     */

    /*post("/perfil/{id}/cadastro/consulta") {
        val dadosConsulta = call.receive<Consulta>()
        val consultaCriada = sistema.Marcar(
            dadosConsulta.paciente!!, dadosConsulta.medico!!, dadosConsulta.local!!,
            dadosConsulta.data!!, dadosConsulta.hora!!, dadosConsulta.motivo!!, dadosConsulta.funcionario!!
        )
        call.respond(consultaCriada)
    }
    */

    //todas as paradas de funcionarios
}

fun Route.perfilMedico() {

    post("/perfil/cadastro/medico") {

        val dadosCadastroMedico = call.receive<Medico>()

        val medicoCadastrado = sistema.cadastroMedico(
            dadosCadastroMedico.nome!!,
            dadosCadastroMedico.idade!!,
            dadosCadastroMedico.cpf!!,
            dadosCadastroMedico.telefone!!,
            dadosCadastroMedico.crm!!,
            dadosCadastroMedico.especializacao!!,
            dadosCadastroMedico.email!!,
            dadosCadastroMedico.nomeUsuario!!
        )

        call.respond(medicoCadastrado)

        call.respond(HttpStatusCode.Created)
    }

    post("/perfil/login/medico") {
        val perfil = call.receive<Medico>()
        // Verifica se o e-mail foi envidado na requisição
        if (perfil.email == null) {
            val error = ServerError(HttpStatusCode.BadRequest.value, "O E-MAIL é obrigatório efetuar login!")
            call.respond(HttpStatusCode.BadRequest, error)
            return@post
        }

        val result = sistema.login(perfil.email!!)

        if (!result) {
            val error = ServerError(
                HttpStatusCode.BadRequest.value, "Não foi possível efetuar login." +
                        " Por favor verifique os dados."
            )
            call.respond(HttpStatusCode.BadRequest, error)
            return@post
        }

        call.respond(HttpStatusCode.NoContent)
    }

    get("/perfil/medico/{crm}/consultas"){}

    /**TODO Leitura dos dados do medico
     * TODO Buscar consultas pelo id do medico, atualização parcial(nome, telefone)
     */

    //todas as paradas de paciente
}

fun Route.perfilPaciente() {

    post("/perfil/cadastro/paciente") {

        val dadosCadastroPaciente = call.receive<Paciente>()

        val pacienteCadastrado = sistema.cadastroPaciente(
            dadosCadastroPaciente.nome!!,
            dadosCadastroPaciente.idade!!,
            dadosCadastroPaciente.cpf!!,
            dadosCadastroPaciente.telefone!!,
            dadosCadastroPaciente.numCartaoConsulta!!,
            dadosCadastroPaciente.email!!,
            dadosCadastroPaciente.nomeUsuario!!
        )

        call.respond(pacienteCadastrado)

        call.respond(HttpStatusCode.Created)
    }

    post("/perfil/login/paciente") {
        val perfil = call.receive<Paciente>()
        // Verifica se o e-mail foi envidado na requisição
        if (perfil.email == null) {
            val error = ServerError(HttpStatusCode.BadRequest.value, "O E-MAIL é obrigatório efetuar login!")
            call.respond(HttpStatusCode.BadRequest, error)
            return@post
        }

        val result = sistema.login(perfil.email!!)

        if (!result) {
            val error = ServerError(
                HttpStatusCode.BadRequest.value, "Não foi possível efetuar login." +
                        " Por favor verifique os dados."
            )
            call.respond(HttpStatusCode.BadRequest, error)
            return@post
        }

        call.respond(HttpStatusCode.NoContent)
    }

    /**TODO Atualizar parcialmente(nome e telefone)
     * TODO Buscar consulta pelo id do paciente
     */
}


















/*
// http://localhost:8888/profile/81c4ae49-e0e3-483a-a1ae-540e3e412fdd/address/HOME
post("/profile/{id}/address/{type}") {
    val userId = call.parameters["id"]
    val type = call.parameters["type"]
    val address = call.receive<Address>()

    val user = site.currentProfile
    if (user == null) {
        val error = ServerError(HttpStatusCode.NotFound.value, "Usuário não encontrado. Efetue login para continuar com suas compras!")
        call.respond(HttpStatusCode.NotFound, error)
        return@post
    }

    if (userId == null) {
        val error = ServerError(HttpStatusCode.BadRequest.value, "O ID do Usuário é obrigatório!")
        call.respond(HttpStatusCode.BadRequest, error)
        return@post
    }

    if (type == null) {
        val error = ServerError(HttpStatusCode.BadRequest.value, "O tipo do endereço é obrigatório!")
        call.respond(HttpStatusCode.BadRequest, error)
        return@post
    }
    address.type = AddressType.valueOf(type)

    site.addOrUpdateAddress(address)

    call.respond(HttpStatusCode.Accepted)
}

 */
}

//Cadastros

fun Route.cadastroConsulta() {
    post("/cadastro/consulta"){
        val dadosConsulta = call.receive<Consulta>()
        val consultaCriada = sistema.Marcar(
            dadosConsulta.paciente!!, dadosConsulta.medico!!, dadosConsulta.local!!,
            dadosConsulta.data!!, dadosConsulta.hora!!, dadosConsulta.motivo!!
        )
        call.respond(consultaCriada)
    }
}


//Listar todos

fun Route.listarPacientes() {
    get("/pacientes") {
        call.respond(sistema.listPaciente)
    }
}

fun Route.listarMedicos(){
    get("/medicos"){
        call.respond(sistema.listMedico)
    }
}

fun Route.listarConsultas() {

    get("/consultas") {
        call.respond(sistema.listConsulta)
    }
}

fun Route.listarFuncionarios() {
    get("/funcionarios") {
        call.respond(sistema.listFuncionario)
    }
}

//Procurar por id

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

fun Route.procurarConsulta(){
    get("/consulta/{codigo?}"){
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


//deletar todos

fun Route.deletarConsultas() {
    delete("/consultas") {
        sistema.listConsulta.clear()
    }
}

fun Route.deletarMedicos() {
    delete("/medicos") {
        sistema.listMedico.clear()
    }
}

fun Route.deletarPacientes(){
    delete("/pacientes") {
        sistema.listPaciente.clear()
    }
}

fun Route.deletarFuncionarios(){
    delete("/funcionarios") {
        sistema.listFuncionario.clear()
    }
}

//deletar por id

fun Route.deletarConsultaId(){
    delete("/consulta/{codigo?}"){
        var codigo = call.parameters["codigo"]
        if(codigo != null){
            for (i in 0 until sistema.listConsulta.size) {
                if (sistema.listConsulta[i].codigo == codigo){
                    sistema.listConsulta.remove(sistema.listConsulta[i])
                }
            }
        } else {
            call.respondText { "Codigo de consulta invalido" }
        }
    }
}

fun Route.deletarMedicoId(){
    delete ("/medicos/{crm?}"){
        var crm = call.parameters["crm"]
        if(crm != null) {
            for (i in 0 until sistema.listMedico.size) {
                if (sistema.listMedico[i].crm.toString() == crm) {
                    sistema.listMedico.remove(sistema.listMedico[i])
                }
            }
        }else{
            call.respondText { "Crm invalido" }
        }
    }
}

fun Route.deletarPacienteId(){
    delete("/paciente/{numCartaoConsulta?}"){
        var numCartaoConsulta = call.parameters["numCartaoConsulta"]
        if(numCartaoConsulta != null){
            for (i in 0 until sistema.listPaciente.size) {
                if(sistema.listPaciente[i].numCartaoConsulta == numCartaoConsulta){
                    sistema.listPaciente.remove(sistema.listPaciente[i])
                }
            }
        }else{
            call.respondText {"Cartão de Consulta invalida"}
        }
    }
}

fun Route.deletarFuncioanrioId(){
    delete("/funcionario/{matricula?}"){
        var matricula = call.parameters["matricula"]
        if(matricula != null){
            for (i in 0 until sistema.listFuncionario.size) {
                if(sistema.listFuncionario[i].matricula == matricula){
                    sistema.listFuncionario.remove(sistema.listFuncionario[i])
                }
            }
        }else{
            call.respondText {"matricula invalida"}
        }
    }
}


//Update

fun Route.updateConsulta(){

    put("/consulta/{codigo?}"){
        var codigo = call.parameters["codigo"]
        val dadosCriacaoConsulta = call.receive<Consulta>()
        if(codigo != null){
            for (i in 0 until sistema.listConsulta.size) {
                if(sistema.listConsulta[i].codigo == codigo){
                    sistema.listConsulta.remove(sistema.listConsulta[i])
                    val consultaAtualizada = sistema.Marcar(dadosCriacaoConsulta.paciente!!,
                        dadosCriacaoConsulta.medico!!, dadosCriacaoConsulta.local!!, dadosCriacaoConsulta.data!!,
                        dadosCriacaoConsulta.hora!! , dadosCriacaoConsulta.motivo!!)
                    call.respond(consultaAtualizada)
                }
            }
        }else{
            call.respondText {"codigo invalido"}
        }
    }
}

fun Route.updatePaciente(){
    put("/paciente/{numCartaoConsulta?}"){
        var numCartaoConsulta = call.parameters["numCartaoConsulta"]
        val dadosCadastroPaciente = call.receive<Paciente>()
        if(numCartaoConsulta != null){
            for (i in 0 until sistema.listPaciente.size) {
                if(sistema.listPaciente[i].numCartaoConsulta == numCartaoConsulta){
                    sistema.listPaciente.remove(sistema.listPaciente[i])
                    val pacienteAtualizado = sistema.cadastroPaciente(dadosCadastroPaciente.nome!!, dadosCadastroPaciente.idade!!, dadosCadastroPaciente.cpf!!, dadosCadastroPaciente.telefone!!, dadosCadastroPaciente.numCartaoConsulta!!)
                    call.respond(pacienteAtualizado)
                }
            }
        }else{
            call.respondText {"Número Do Cartão Consulta Inválido"}
        }
    }
}

fun Route.updateMedico(){
    put("/medico/{crm?}"){
        var crm = call.parameters["crm"]
        val dadosCadastroMedico = call.receive<Medico>()
        if(crm != null){
            for (i in 0 until sistema.listPaciente.size) {
                if (sistema.listMedico[i].crm.toString() == crm) {
                    sistema.listMedico.remove(sistema.listMedico[i])
                    val medicoAtualizado = sistema.cadastroMedico(dadosCadastroMedico.nome!!, dadosCadastroMedico.idade!!, dadosCadastroMedico.cpf!!, dadosCadastroMedico.telefone!!, dadosCadastroMedico.crm!!, dadosCadastroMedico.especializacao!!)
                    call.respond(medicoAtualizado)
                }
            }
        }else{
            call.respondText {"crm invalido"}
        }
    }
}

fun Route.updateFuncionario(){
    put("/funcionario/{matricula?}"){
        var matricula = call.parameters["matricula"]
        val dadosCadastroFuncionario = call.receive<Funcionario>()
        if(matricula != null){
            for (i in 0 until sistema.listFuncionario.size) {
                if(sistema.listFuncionario[i].matricula == matricula){
                    sistema.listFuncionario.remove(sistema.listFuncionario[i])
                    val funcionarioAtualizado = sistema.cadastroFuncionario(dadosCadastroFuncionario.nome!!, dadosCadastroFuncionario.idade!!, dadosCadastroFuncionario.cpf!!, dadosCadastroFuncionario.telefone!!, dadosCadastroFuncionario.matricula!!)
                    call.respond(funcionarioAtualizado)
                }
            }
        }else{
            call.respondText {"matricula invalida"}
        }
    }
}