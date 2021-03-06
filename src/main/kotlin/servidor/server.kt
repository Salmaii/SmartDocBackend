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

        //Confere login e mostra rotas

        perfil()

        //Perfis especificos

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
                    //IDs dos perfis
                    ol { +"Consulta                  - Id{codigo?}" }
                    ol { +"Funcionário               - Id{matricula?}" }
                    ol { +"Médico                    - Id{crm?}" }
                    ol { +"Paciente                  - Id{numCartaoConsulta?}" }

                    //Perfil Geral
                    ol { +"GET  - /perfil                                                      - Verifica se tem perfil logado" }

                    ol { +"GET - /perfil/cadastro                                              - Mostra rotas de cadastro de perfil em geral " }
                    ol { +"GET - /perfil/login                                                 - Mostra rotas de login de perfil em geral" }


                    //Perfil funcionario
                    ol { +"POST - /perfil/cadastro/funcionario                                  - Cadastro de funcionário" }
                    ol { +"POST - /perfil/login/funcionario                                     - Login de funcionário" }
                    ol { +"POST - /perfil/funcionario/{id}/cadastro/consulta                    - Cadastro de consulta" }

                    ol { +"GET - /perfil/funcionario/{id?}/funcionarios                         - Mostra todos os funcionários" }
                    ol { +"GET - /perfil/funcionario/{id?}/consultas                            - Mostra todas as consultas" }
                    ol { +"GET - /perfil/funcionario/{id?}/medicos                              - Mostra todos os médicos" }
                    ol { +"GET - /perfil/funcionario/{id?}/pacientes                            - Mostra todos os pacientes" }

                    ol { +"GET - /perfil/funcionario/{id?}/funcionario/{matricula?}/dados       - Mostra funcionario por id{matricula?}" }
                    ol { +"GET - /perfil/funcionario/{id?}/consulta/{codigo?}/dados             - Mostra consulta por id{codigo?}" }
                    ol { +"GET - /perfil/funcionario/{id?}/medico/{crm?}/dados                  - Mostra médico por id{crm?}" }
                    ol { +"GET - /perfil/funcionario/{id?}/paciente/{numCartaoConsulta?}/dados  - Mostra paciente por id{numCartaoConsulta?}" }

                    ol { +"PUT - /perfil/funcionario/{id?}/funcionario/{matricula?}             - Atualização geral do perfil de funcionário de id{matricula?}" }
                    ol { +"PUT - /perfil/funcionario/{id?}/consulta/{codigo?}                   - Atualização geral da consulta de id{código?}" }

                    ol { +"DELETE - /perfil/funcionario/{id?}/consultas                         - Deleta todas as consultas" }
                    ol { +"DELETE - /perfil/funcionario/{id?}/medicos                           - Deleta todos os medicos" }
                    ol { +"DELETE - /perfil/funcionario/{id?}/pacientes                         - Deleta todos os pacientes" }
                    ol { +"DELETE - /perfil/funcionario/{id?}/funcionarios                      - Deleta todos os funcionarios" }

                    ol { +"DELETE - /perfil/funcionario/{id?}/consulta/{codigo?}                - Deleta uma consulta de {codigo?}" }
                    ol { +"DELETE - /perfil/funcionario/{id?}/medico/{crm?}                     - Delete um médico de id{crm?}" }
                    ol { +"DELETE - /perfil/funcionario/{id?}/paciente/{numCartaoConsulta?}     - Deleta um paciente de id{numCartaoConsulta?}" }
                    ol { +"DELETE - /perfil/funcionario/{id?}/funcionario/{matricula?}          - Deleta um funcionario de id{matricula?}" }


                    //Perfil Medico
                    ol { +"POST - /perfil/cadastro/medico                                       - Cadastro de medico" }
                    ol { +"POST - /perfil/login/medico                                          - Login de médico" }

                    ol { +"GET - /perfil/medico/{id?}/consultas                                 - Mostra todas as consultas do médico de id{crm?}" }
                    ol { +"GET - /perfil/medico/{id?}/dados                                     - Mostra perfil do médico de id{crm?}" }

                    ol { +"PUT - /perfil/medico/{id?}/atualizar                                 - Atualização geral do perfil do médico de id{crm?}" }

                    ol { +"DELETE - /perfil/medico/{id?}/delete                                 - Delete do perfil de médico de id{crm?}" }


                    //Perfil Paciente
                    ol { +"POST - /perfil/cadastro/paciente                                     - Cadastro de paciente"}
                    ol { +"POST - /perfil/login/paciente                                        - Login de paciente"}

                    ol { +"GET - /perfil/paciente/{id?}/buscarConsultas                         - Mostra todas as consultas do paciente de id{numCartaoConsulta?}"}
                    ol { +"GET - /perfil/paciente/{id?}/dados                                   - Mostra perfil do paciente de id{numCartaoConsulta?}"}

                    ol { +"PUT - /perfil/paciente/{id?}                                         - Atualização geral do perfil do paciente de id{numCartaoConsulta?}"}

                    ol { +"DELETE - /perfil/paciente/{id?}/delete                               - Delete do perfil de paciente de id{numCartaoConsulta?}"}
                }
            }
        }
    }
}

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

    get("/perfil/cadastro") {
        call.respondText(
            "Vá para a rota /cadastro/paciente" +
                    " Vá para a rota /cadastro/medico" +
                    " Vá para a rota /cadastro/funcionario"
        )
    }


    //Rota de login geral

    get("/perfil/login"){
        call.respondText("Vá para a rota /login/paciente" +
                " Vá para a rota /login/medico" +
                " Vá para a rota /login/funcionario")
    }
}

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

    post("/perfil/funcionario/{id}/cadastro/consulta") {
        val idPessoa = call.parameters["id"]
        val validado = sistema.validaFuncionario(idPessoa.toString())
        var error = ServerError(
            HttpStatusCode.NotFound.value, "Id não encontrado na requisição . Passe o parametro id para " +
                    "continuar!"
        )
        if(idPessoa == null) {
            call.respond(HttpStatusCode.NotFound, error)
        }else{
            if(validado == true){
                val dadosConsulta = call.receive<Consulta>()
                val consultaCriada = sistema.Marcar(dadosConsulta.idPaciente!!,
                    dadosConsulta.idMedico!!, dadosConsulta.idFuncionario!!, dadosConsulta.local!!,
                    dadosConsulta.data!!, dadosConsulta.hora!!, dadosConsulta.motivo!!
                )
                call.respond(consultaCriada)
            }else{
                error = ServerError(
                    HttpStatusCode.Unauthorized.value, "Id inválido . Passe o parametro id para " +
                            "continuar!"
                )
                call.respond(HttpStatusCode.Unauthorized, error)
            }
        }
    }

    //Buscar todos

    get("/perfil/funcionario/{id?}/funcionarios") {
        val idPessoa = call.parameters["id"]
        val validado = sistema.validaFuncionario(idPessoa.toString())
        var error = ServerError(
            HttpStatusCode.NotFound.value, "Id não encontrado na requisição . Passe o parametro id para " +
                    "continuar!"
        )
        if(idPessoa == null) {
            call.respond(HttpStatusCode.NotFound, error)
        }else{
            if(validado == true){
                call.respond(sistema.listFuncionario)
            }else{
                error = ServerError(
                    HttpStatusCode.Unauthorized.value, "Id inválido . Passe o parametro id para " +
                            "continuar!"
                )
                call.respond(HttpStatusCode.Unauthorized, error)
            }
        }
    }

    get("/perfil/funcionario/{id?}/consultas") {
        val idPessoa = call.parameters["id"]
        val validado = sistema.validaFuncionario(idPessoa.toString())
        var error = ServerError(
            HttpStatusCode.NotFound.value, "Id não encontrado na requisição . Passe o parametro id para " +
                    "continuar!"
        )
        if (idPessoa == null) {
            call.respond(HttpStatusCode.NotFound, error)
        } else {
            if (validado == true) {
                call.respond(sistema.listConsulta)
            } else {
                error = ServerError(
                    HttpStatusCode.Unauthorized.value, "Id inválido . Passe o parametro id para " +
                            "continuar!"
                )
                call.respond(HttpStatusCode.Unauthorized, error)
            }
        }
    }

    get("/perfil/funcionario/{id?}/medicos"){
        val idPessoa = call.parameters["id"]
        val validado = sistema.validaFuncionario(idPessoa.toString())
        var error = ServerError(
            HttpStatusCode.NotFound.value, "Id não encontrado na requisição . Passe o parametro id para " +
                    "continuar!"
        )
        if(idPessoa == null) {
            call.respond(HttpStatusCode.NotFound, error)
        }else{
            if(validado == true){
                call.respond(sistema.listMedico)
            }else{
                error = ServerError(
                    HttpStatusCode.Unauthorized.value, "Id inválido . Passe o parametro id para " +
                            "continuar!"
                )
                call.respond(HttpStatusCode.Unauthorized, error)
            }
        }
    }

    get("/perfil/funcionario/{id?}/pacientes") {
        val idPessoa = call.parameters["id"]
        val validado = sistema.validaFuncionario(idPessoa.toString())
        var error = ServerError(
            HttpStatusCode.NotFound.value, "Id não encontrado na requisição . Passe o parametro id para " +
                    "continuar!"
        )
        if (idPessoa == null) {
            call.respond(HttpStatusCode.NotFound, error)
        } else {
            if (validado == true) {
                call.respond(sistema.listPaciente)
            } else {
                error = ServerError(
                    HttpStatusCode.Unauthorized.value, "Id inválido . Passe o parametro id para " +
                            "continuar!"
                )
                call.respond(HttpStatusCode.Unauthorized, error)
            }
        }
    }

    //Buscar por id

    get("/perfil/funcionario/{id?}/funcionario/{matricula?}/dados"){
        var matricula = call.parameters["matricula"]
        val idPessoa = call.parameters["id"]
        val validado = sistema.validaFuncionario(idPessoa.toString())
        var error = ServerError(
            HttpStatusCode.NotFound.value, "Id não encontrado na requisição . Passe o parametro id para " +
                    "continuar!"
        )
        if(idPessoa == null) {
            call.respond(HttpStatusCode.NotFound, error)
        }else{
            if(validado == true){
                if(matricula != null){
                    for (i in 0 until sistema.listFuncionario.size) {
                        if (sistema.listFuncionario[i].matricula == matricula) {
                            call.respond(sistema.listFuncionario[i])
                        }
                    }
                }else{
                    error = ServerError(
                        HttpStatusCode.NotFound.value,
                        "matricula inválida . Passe o parametro matricula correto para " +
                                "continuar!"
                    )
                    call.respond(HttpStatusCode.Unauthorized, error)
                }
            }else{
                error = ServerError(
                    HttpStatusCode.Unauthorized.value, "Id inválido . Passe o parametro id para " +
                            "continuar!"
                )
                call.respond(HttpStatusCode.Unauthorized, error)
            }
        }
    }

    get("/perfil/funcionario/{id?}/consulta/{codigo?/dados"){
        var codigo = call.parameters["codigo"]
        val idPessoa = call.parameters["id"]
        val validado = sistema.validaFuncionario(idPessoa.toString())
        var error = ServerError(
            HttpStatusCode.NotFound.value, "Id não encontrado na requisição . Passe o parametro id para " +
                    "continuar!"
        )
        if(idPessoa == null) {
            call.respond(HttpStatusCode.NotFound, error)
        }else{
            if(validado == true){
                if(codigo != null){
                    for (i in 0 until sistema.listConsulta.size) {
                        if (sistema.listConsulta[i].codigo == codigo) {
                            call.respond(sistema.listConsulta[i])
                        }
                    }
                } else {
                    error = ServerError(
                        HttpStatusCode.NotFound.value,
                        "codigo inválido . Passe o parametro codigo correto para " +
                                "continuar!"
                    )
                    call.respond(HttpStatusCode.Unauthorized, error)
                }
            }else{
                error = ServerError(
                    HttpStatusCode.Unauthorized.value, "Id inválido . Passe o parametro id para " +
                            "continuar!"
                )
                call.respond(HttpStatusCode.Unauthorized, error)
            }
        }
    }

    get("/perfil/funcionario/{id?}/medico/{crm?}/dados"){
        var crm = call.parameters["crm"]
        val idPessoa = call.parameters["id"]
        val validado = sistema.validaFuncionario(idPessoa.toString())
        var error = ServerError(
            HttpStatusCode.NotFound.value, "Id não encontrado na requisição . Passe o parametro id para " +
                    "continuar!"
        )
        if(idPessoa == null) {
            call.respond(HttpStatusCode.NotFound, error)
        }else{
            if(validado == true){
                if(crm != null) {
                    for (i in 0 until sistema.listMedico.size) {
                        if (sistema.listMedico[i].crm.toString() == crm) {
                            call.respond(sistema.listMedico[i])
                        }
                    }
                }else{
                    error = ServerError(
                        HttpStatusCode.NotFound.value,
                        "crm inválido . Passe o parametro crm correto para " +
                                "continuar!"
                    )
                    call.respond(HttpStatusCode.Unauthorized, error)
                }
            }else{
                error = ServerError(
                    HttpStatusCode.Unauthorized.value, "Id inválido . Passe o parametro id para " +
                            "continuar!"
                )
                call.respond(HttpStatusCode.Unauthorized, error)
            }
        }
    }

    get("/perfil/funcionario/{id?}/paciente/{numCartaoConsulta?}/dados"){
        var numCartaoConsulta = call.parameters["numCartaoConsulta"]
        val idPessoa = call.parameters["id"]
        val validado = sistema.validaFuncionario(idPessoa.toString())
        var error = ServerError(
            HttpStatusCode.NotFound.value, "Id não encontrado na requisição . Passe o parametro id para " +
                    "continuar!"
        )
        if(idPessoa == null) {
            call.respond(HttpStatusCode.NotFound, error)
        }else{
            if(validado == true){
                if (numCartaoConsulta != null) {
                    for (i in 0 until sistema.listPaciente.size) {
                        if (sistema.listPaciente[i].numCartaoConsulta == numCartaoConsulta) {
                            call.respond(sistema.listPaciente[i])
                        }
                    }
                } else {
                    error = ServerError(
                        HttpStatusCode.NotFound.value,
                        "numCartaoConsulta inválido . Passe o parametro numCartaoConsulta correto para " +
                                "continuar!"
                    )
                    call.respond(HttpStatusCode.Unauthorized, error)
                }
            }else{
                error = ServerError(
                    HttpStatusCode.Unauthorized.value, "Id inválido . Passe o parametro id para " +
                            "continuar!"
                )
                call.respond(HttpStatusCode.Unauthorized, error)
            }
        }
    }


    //Atualização do funcionario

    put("/perfil/funcionario/{id?}/funcionario/{matricula?}"){
        var matricula = call.parameters["matricula"]
        val idPessoa = call.parameters["id"]
        val validado = sistema.validaFuncionario(idPessoa.toString())
        val dadosCadastroFuncionario = call.receive<Funcionario>()
        var error = ServerError(
            HttpStatusCode.NotFound.value, "Id não encontrado na requisição . Passe o parametro id para " +
                    "continuar!"
        )
        if(idPessoa == null) {
            call.respond(HttpStatusCode.NotFound, error)
        }else{
            if(validado == true){
                if(matricula != null){
                    for (i in 0 until sistema.listFuncionario.size) {
                        if(sistema.listFuncionario[i].matricula == matricula){
                            sistema.listFuncionario.remove(sistema.listFuncionario[i])
                            val funcionarioAtualizado = sistema.cadastroFuncionario(dadosCadastroFuncionario.nome!!,
                                dadosCadastroFuncionario.idade!!, dadosCadastroFuncionario.cpf!!, dadosCadastroFuncionario.telefone!!,
                                dadosCadastroFuncionario.matricula!!, dadosCadastroFuncionario.email!!, dadosCadastroFuncionario.nomeUsuario!!)
                            call.respond(funcionarioAtualizado)
                        }
                    }
                }else{
                    error = ServerError(
                        HttpStatusCode.NotFound.value, "Matrícula não encontrada na requisição ." +
                                " Passe o parametro Matricula para continuar!"
                    )
                    call.respond(HttpStatusCode.NotFound, error)
                }
            }else{
                error = ServerError(
                    HttpStatusCode.Unauthorized.value, "Id inválido . Passe o parametro id para " +
                            "continuar!"
                )
                call.respond(HttpStatusCode.Unauthorized, error)
            }
        }
    }

    //Atualização de consulta

    put("/perfil/funcionario/{id?}/consulta/{codigo?}"){
        var codigo = call.parameters["codigo"]
        val dadosCriacaoConsulta = call.receive<Consulta>()
        val idPessoa = call.parameters["id"]
        val validado = sistema.validaFuncionario(idPessoa.toString())
        var error = ServerError(
            HttpStatusCode.NotFound.value, "Id não encontrado na requisição . Passe o parametro id para " +
                    "continuar!"
        )
        if(idPessoa == null) {
            call.respond(HttpStatusCode.NotFound, error)
        }else{
            if(validado == true){
                if(codigo != null){
                    for (i in 0 until sistema.listConsulta.size) {
                        if(sistema.listConsulta[i].codigo == codigo){
                            sistema.listConsulta.remove(sistema.listConsulta[i])
                            val consultaAtualizada = sistema.Marcar(dadosCriacaoConsulta.idPaciente!!,
                                dadosCriacaoConsulta.idMedico!!, dadosCriacaoConsulta.idFuncionario!!, dadosCriacaoConsulta.local!!,
                                dadosCriacaoConsulta.data!!,dadosCriacaoConsulta.hora!!, dadosCriacaoConsulta.motivo!!)
                            call.respond(consultaAtualizada)
                        }
                    }
                }else{
                    error = ServerError(
                        HttpStatusCode.NotFound.value, "Codigo não encontrado na requisição . Passe o parametro Codigo para " +
                                "continuar!"
                    )
                    call.respond(HttpStatusCode.NotFound, error)
                }
            }else{
                error = ServerError(
                    HttpStatusCode.Unauthorized.value, "Id inválido . Passe o parametro id para " +
                            "continuar!"
                )
                call.respond(HttpStatusCode.Unauthorized, error)
            }
        }
    }

    //Deletar todos

    delete("/perfil/funcionario/{id?}/consultas") {
        val idPessoa = call.parameters["id"]
        val validado = sistema.validaFuncionario(idPessoa.toString())
        var error = ServerError(
            HttpStatusCode.NotFound.value, "Id não encontrado na requisição . Passe o parametro id para " +
                    "continuar!"
        )
        if(idPessoa == null) {
            call.respond(HttpStatusCode.NotFound, error)
        }else{
            if(validado == true){
                sistema.listConsulta.clear()
            }else{
                error = ServerError(
                    HttpStatusCode.Unauthorized.value, "Id inválido . Passe o parametro id para " +
                            "continuar!"
                )
                call.respond(HttpStatusCode.Unauthorized, error)
            }
        }

    }

    delete("/perfil/funcionario/{id?}/medicos") {
        val idPessoa = call.parameters["id"]
        val validado = sistema.validaFuncionario(idPessoa.toString())
        var error = ServerError(
            HttpStatusCode.NotFound.value, "Id não encontrado na requisição . Passe o parametro id para " +
                    "continuar!"
        )
        if(idPessoa == null) {
            call.respond(HttpStatusCode.NotFound, error)
        }else{
            if(validado == true){
                sistema.listMedico.clear()
            }else{
                error = ServerError(
                    HttpStatusCode.Unauthorized.value, "Id inválido . Passe o parametro id para " +
                            "continuar!"
                )
                call.respond(HttpStatusCode.Unauthorized, error)
            }
        }
    }

    delete("/perfil/funcionario/{id?}/pacientes") {
        val idPessoa = call.parameters["id"]
        val validado = sistema.validaFuncionario(idPessoa.toString())
        var error = ServerError(
            HttpStatusCode.NotFound.value, "Id não encontrado na requisição . Passe o parametro id para " +
                    "continuar!"
        )
        if(idPessoa == null) {
            call.respond(HttpStatusCode.NotFound, error)
        }else{
            if(validado == true){
                sistema.listPaciente.clear()
            }else{
                error = ServerError(
                    HttpStatusCode.Unauthorized.value, "Id inválido . Passe o parametro id para " +
                            "continuar!"
                )
                call.respond(HttpStatusCode.Unauthorized, error)
            }
        }
    }

    delete("/perfil/funcionario/{id?}/funcionarios") {
        val idPessoa = call.parameters["id"]
        val validado = sistema.validaFuncionario(idPessoa.toString())
        var error = ServerError(
            HttpStatusCode.NotFound.value, "Id não encontrado na requisição . Passe o parametro id para " +
                    "continuar!"
        )
        if(idPessoa == null) {
            call.respond(HttpStatusCode.NotFound, error)
        }else{
            if(validado == true){
                sistema.listFuncionario.clear()
            }else{
                error = ServerError(
                    HttpStatusCode.Unauthorized.value, "Id inválido . Passe o parametro id para " +
                            "continuar!"
                )
                call.respond(HttpStatusCode.Unauthorized, error)
            }
        }
    }

    //Deletes por id

    delete("/perfil/funcionario/{id?}/consulta/{codigo?}"){
        var codigo = call.parameters["codigo"]
        val idPessoa = call.parameters["id"]
        val validado = sistema.validaFuncionario(idPessoa.toString())
        var error = ServerError(
            HttpStatusCode.NotFound.value, "Id não encontrado na requisição . Passe o parametro id para " +
                    "continuar!"
        )
        if(idPessoa == null) {
            call.respond(HttpStatusCode.NotFound, error)
        }else{
            if(validado == true){
                if(codigo != null){
                    for (i in 0 until sistema.listConsulta.size) {
                        if (sistema.listConsulta[i].codigo == codigo){
                            sistema.listConsulta.remove(sistema.listConsulta[i])
                        }
                    }
                } else {
                    error = ServerError(
                        HttpStatusCode.NotFound.value, "Código da cosulta não encontrado na requisição ." +
                                " Passe o parametro código para continuar!"
                    )
                    call.respond(HttpStatusCode.NotFound, error)
                }
            }else{
                error = ServerError(
                    HttpStatusCode.Unauthorized.value, "Id inválido . Passe o parametro id para " +
                            "continuar!"
                )
                call.respond(HttpStatusCode.Unauthorized, error)
            }
        }
    }

    delete ("/perfil/funcionario/{id?}/medico/{crm?}"){
        var crm = call.parameters["crm"]
        val idPessoa = call.parameters["id"]
        val validado = sistema.validaFuncionario(idPessoa.toString())
        var error = ServerError(
            HttpStatusCode.NotFound.value, "Id não encontrado na requisição . Passe o parametro id para " +
                    "continuar!"
        )
        if(idPessoa == null) {
            call.respond(HttpStatusCode.NotFound, error)
        }else{
            if(validado == true){
                if(crm != null) {
                    for (i in 0 until sistema.listMedico.size) {
                        if (sistema.listMedico[i].crm.toString() == crm) {
                            sistema.listMedico.remove(sistema.listMedico[i])
                        }
                    }
                }else{
                    error = ServerError(
                        HttpStatusCode.NotFound.value, "Crm não encontrado na requisição ." +
                                " Passe o parametro crm para continuar!"
                    )
                    call.respond(HttpStatusCode.NotFound, error)
                }
            }else{
                error = ServerError(
                    HttpStatusCode.Unauthorized.value, "Id inválido . Passe o parametro id para " +
                            "continuar!"
                )
                call.respond(HttpStatusCode.Unauthorized, error)
            }
        }
    }

    delete("/perfil/funcionario/{id?}/paciente/{numCartaoConsulta?}"){
        var numCartaoConsulta = call.parameters["numCartaoConsulta"]
        val idPessoa = call.parameters["id"]
        val validado = sistema.validaFuncionario(idPessoa.toString())
        var error = ServerError(
            HttpStatusCode.NotFound.value, "Id não encontrado na requisição . Passe o parametro id para " +
                    "continuar!"
        )
        if(idPessoa == null) {
            call.respond(HttpStatusCode.NotFound, error)
        }else{
            if(validado == true){
                if(numCartaoConsulta != null){
                    for (i in 0 until sistema.listPaciente.size) {
                        if(sistema.listPaciente[i].numCartaoConsulta == numCartaoConsulta){
                            sistema.listPaciente.remove(sistema.listPaciente[i])
                        }
                    }
                }else {
                    error = ServerError(
                        HttpStatusCode.NotFound.value, "numCartaoConsulta não encontrado na requisição ." +
                                " Passe o parametro numCartaoConsulta para continuar!"
                    )
                    call.respond(HttpStatusCode.NotFound, error)
                }
            }else{
                error = ServerError(
                    HttpStatusCode.Unauthorized.value, "Id inválido . Passe o parametro id para " +
                            "continuar!"
                )
                call.respond(HttpStatusCode.Unauthorized, error)
            }
        }
    }

    delete("/perfil/funcionario/{id?}/funcionario/{matricula?}"){
        var matricula = call.parameters["matricula"]
        val idPessoa = call.parameters["id"]
        val validado = sistema.validaFuncionario(idPessoa.toString())
        var error = ServerError(
            HttpStatusCode.NotFound.value, "Id não encontrado na requisição . Passe o parametro id para " +
                    "continuar!"
        )
        if(idPessoa == null) {
            call.respond(HttpStatusCode.NotFound, error)
        }else{
            if(validado == true){
                if(matricula != null){
                    for (i in 0 until sistema.listFuncionario.size) {
                        if(sistema.listFuncionario[i].matricula == matricula){
                            sistema.listFuncionario.remove(sistema.listFuncionario[i])
                        }
                    }
                }else{
                    error = ServerError(
                        HttpStatusCode.NotFound.value, "matricula não encontrado na requisição ." +
                                " Passe o parametro matricula para continuar!"
                    )
                    call.respond(HttpStatusCode.NotFound, error)
                }
            }else{
                error = ServerError(
                    HttpStatusCode.Unauthorized.value, "Id inválido . Passe o parametro id para " +
                            "continuar!"
                )
                call.respond(HttpStatusCode.Unauthorized, error)
            }
        }

    }


}

fun Route.perfilMedico() {

    //Cadastro de novo medico

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

    // Verifica se o e-mail foi envidado na requisição

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

    //Buscar consultas do medico

    get("/perfil/medico/{id?}/consultas"){
        val idPessoa = call.parameters["id"]
        val validado = sistema.validaMedico(idPessoa.toString())
        var medicoConsultas = mutableListOf<Consulta>()
        var error = ServerError(
            HttpStatusCode.NotFound.value, "Id não encontrado na requisição . Passe o parametro id para " +
                    "continuar!"
        )
        if(idPessoa == null) {
            call.respond(HttpStatusCode.NotFound, error)
        }else{
            if(validado){
                for (i in 0 until sistema.listConsulta.size) {
                    if (sistema.listConsulta[i].idMedico == idPessoa) {
                        medicoConsultas.add(sistema.listConsulta[i])
                    }
                }
                call.respond(medicoConsultas)
            }else{
                error = ServerError(
                    HttpStatusCode.Unauthorized.value, "Id inválido . Passe o parametro id para " +
                            "continuar!")
                call.respond(HttpStatusCode.Unauthorized, error)
            }
        }
    }

    //Buscar dados do medico

    get("/perfil/medico/{id?}/dados"){
        val idPessoa = call.parameters["id"]
        val validado = sistema.validaMedico(idPessoa.toString())

        var error = ServerError(
            HttpStatusCode.NotFound.value, "Id não encontrado na requisição ." +
                    " Passe o parametro id para continuar!"
        )

        if(idPessoa == null) {
            call.respond(HttpStatusCode.NotFound, error)
        }else{
            if(validado){
                    for (i in 0 until sistema.listMedico.size) {
                        if (sistema.listMedico[i].crm.toString() == idPessoa) {
                            call.respond(sistema.listMedico[i])
                        }
                    }
            }else{
                error = ServerError(
                    HttpStatusCode.Unauthorized.value, "Id inválido . " +
                            "Passe o parametro id para continuar!"
                )
                call.respond(HttpStatusCode.Unauthorized, error)
            }
        }
    }

    //Atualização do medico

    put("/perfil/medico/{id?}/atualizar"){
        val dadosCadastroMedico = call.receive<Medico>()
        val idPessoa = call.parameters["id"]
        val validado = sistema.validaMedico(idPessoa.toString())
        var error = ServerError(
            HttpStatusCode.NotFound.value, "Id não encontrado na requisição . Passe o parametro id para " +
                    "continuar!"
        )
        if(idPessoa == null) {
            call.respond(HttpStatusCode.NotFound, error)
        }else{
            if(validado){
                for (i in 0 until sistema.listMedico.size) {
                    if (sistema.listMedico[i].id == idPessoa) {
                        sistema.listMedico.remove(sistema.listMedico[i])
                        val medicoAtualizado = sistema.cadastroMedico(dadosCadastroMedico.nome!!,
                            dadosCadastroMedico.idade!!, dadosCadastroMedico.cpf!!, dadosCadastroMedico.telefone!!,
                            dadosCadastroMedico.crm!!, dadosCadastroMedico.especializacao!!, dadosCadastroMedico.email!!,
                            dadosCadastroMedico.nomeUsuario!!)
                        call.respond(medicoAtualizado)
                    }
                }
            }else{
                error = ServerError(
                    HttpStatusCode.Unauthorized.value, "Id inválido . Passe o parametro id para " +
                            "continuar!"
                )
                call.respond(HttpStatusCode.Unauthorized, error)
            }
        }
    }

    //Delete Prorprio

    delete ("/perfil/medico/{id?}/delete"){
        val idPessoa = call.parameters["id"]
        val validado = sistema.validaMedico(idPessoa.toString())
        var error = ServerError(
            HttpStatusCode.NotFound.value, "Id não encontrado na requisição . Passe o parametro id para " +
                    "continuar!"
        )
        if(idPessoa == null) {
            call.respond(HttpStatusCode.NotFound, error)
        }else{
            if(validado){
                    for (i in 0 until sistema.listMedico.size) {
                        if (sistema.listMedico[i].id == idPessoa) {
                            sistema.listMedico.remove(sistema.listMedico[i])
                        }
                    }
            }else{
                error = ServerError(
                    HttpStatusCode.Unauthorized.value, "Id inválido . Passe o parametro id para " +
                            "continuar!"
                )
                call.respond(HttpStatusCode.Unauthorized, error)
            }
        }
    }
}

fun Route.perfilPaciente() {

    //cadastro de paciente

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

    // Verifica se o e-mail foi envidado na requisição

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

    //Busca pelo id do paciente

    get("/perfil/paciente/{id?}/dados") {
        val idPessoa = call.parameters["id"]
        val validado = sistema.validaPaciente(idPessoa.toString())
        var error = ServerError(
            HttpStatusCode.NotFound.value, "Id não encontrado na requisição . Passe o parametro id para " +
                    "continuar!"
        )
        if (idPessoa == null) {
            call.respond(HttpStatusCode.NotFound, error)
        } else {
            if (validado == true) {
                for (i in 0 until sistema.listPaciente.size) {
                    if (sistema.listPaciente[i].id == idPessoa) {
                        call.respond(sistema.listPaciente[i])
                    }
                }
            } else {
                error = ServerError(
                    HttpStatusCode.Unauthorized.value, "Id inválido . Passe o parametro correto para " +
                            "continuar!"
                )
                call.respond(HttpStatusCode.Unauthorized, error)
            }
        }
    }

    //Busca de consulta pelo numCartaoConsulta do paciente

    get("/perfil/paciente/{id?}/buscarConsultas") {
        val idPessoa = call.parameters["id"]
        val validado = sistema.validaPaciente(idPessoa.toString())
        var pacienteConsultas = mutableListOf<Consulta>()
        var error = ServerError(
            HttpStatusCode.NotFound.value, "Id não encontrado na requisição . Passe o parametro id para " +
                    "continuar!"
        )
        if (idPessoa == null) {
            call.respond(HttpStatusCode.NotFound, error)
        } else {
            if (validado) {
                for (i in 0 until sistema.listConsulta.size) {
                    if (sistema.listConsulta[i].idPaciente == idPessoa) {
                        pacienteConsultas.add(sistema.listConsulta[i])
                    }
                }
                call.respond(pacienteConsultas)
            }else {
                error = ServerError(
                    HttpStatusCode.Unauthorized.value, "Id inválido . Passe o parametro correto para " +
                            "continuar!"
                )
                call.respond(HttpStatusCode.Unauthorized, error)
            }
        }
    }

    //atualização do paciente

    put("/perfil/paciente/{id?}") {
        val idPessoa = call.parameters["id"]
        var numCartaoConsulta = call.parameters["numCartaoConsulta"]
        val dadosCadastroPaciente = call.receive<Paciente>()
        val validado = sistema.validaPaciente(idPessoa.toString())
        var error = ServerError(
            HttpStatusCode.NotFound.value, "Id não encontrado na requisição . Passe o parametro id para " +
                    "continuar!"
        )
        if (idPessoa == null) {
            call.respond(HttpStatusCode.NotFound, error)
        } else {
            if (validado == true) {
                if (numCartaoConsulta != null) {
                    for (i in 0 until sistema.listPaciente.size) {
                        if (sistema.listPaciente[i].numCartaoConsulta == numCartaoConsulta) {
                            sistema.listPaciente.remove(sistema.listPaciente[i])
                            val pacienteAtualizado = sistema.cadastroPaciente(
                                dadosCadastroPaciente.nome!!,
                                dadosCadastroPaciente.idade!!,
                                dadosCadastroPaciente.cpf!!,
                                dadosCadastroPaciente.telefone!!,
                                dadosCadastroPaciente.numCartaoConsulta!!,
                                dadosCadastroPaciente.email!!,
                                dadosCadastroPaciente.nomeUsuario!!
                            )
                            call.respond(pacienteAtualizado)
                        }
                    }
                }
            } else {
                error = ServerError(
                    HttpStatusCode.Unauthorized.value,
                    "Id inválido . Passe o parametro correto para " +
                            "continuar!"
                )
                call.respond(HttpStatusCode.Unauthorized, error)
            }
        }
    }

    //Delete prorpio

    delete("/perfil/paciente/{id?}/delete"){
        val idPessoa = call.parameters["id"]
        val validado = sistema.validaPaciente(idPessoa.toString())
        var error = ServerError(
            HttpStatusCode.NotFound.value, "Id não encontrado na requisição . Passe o parametro id para " +
                    "continuar!"
        )
        if(idPessoa == null) {
            call.respond(HttpStatusCode.NotFound, error)
        }else{
            if(validado){
                for (i in 0 until sistema.listPaciente.size) {
                    if(sistema.listPaciente[i].id == idPessoa){
                        sistema.listPaciente.remove(sistema.listPaciente[i])
                    }
                }
            }else{
                error = ServerError(
                    HttpStatusCode.Unauthorized.value, "Id inválido . Passe o parametro id para " +
                            "continuar!"
                )
                call.respond(HttpStatusCode.Unauthorized, error)
            }
        }
    }
}