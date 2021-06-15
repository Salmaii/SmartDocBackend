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

        //cadastroConsulta()

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

    //Buscar todos

    get("/funcionarios") {
        call.respond(sistema.listFuncionario)
    }

    get("/consultas") {
        call.respond(sistema.listConsulta)
    }

    get("/medicos"){
        call.respond(sistema.listMedico)
    }

    get("/pacientes") {
        call.respond(sistema.listPaciente)
    }

    //Buscar por id

    get("/perfil/funcionario/{id?}/funcionario/{matricula?}"){
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

    get("/perfil/funcionario/{id?}/consulta/{codigo?}"){
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

    get("/perfil/funcionario/{id?}/medico/{crm}/dados"){
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

    /*post("/perfil/{id}/cadastro/consulta") {
        val dadosConsulta = call.receive<Consulta>()
        val consultaCriada = sistema.Marcar(
            dadosConsulta.paciente!!, dadosConsulta.medico!!, dadosConsulta.local!!,
            dadosConsulta.data!!, dadosConsulta.hora!!, dadosConsulta.motivo!!, dadosConsulta.funcionario!!
        )
        call.respond(consultaCriada)
    }
    */

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

    delete("/pacientes") {
        sistema.listPaciente.clear()
    }

    delete("/funcionarios") {
        sistema.listFuncionario.clear()
    }

    //Deletes por id

    delete("/consulta/{codigo?}"){
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

    delete ("/perfil/funcionario/{id?}/medicos/{crm?}"){
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
            if(validado == true && idPessoa == matricula){
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

    get("/perfil/medico/{crm}/consultas"){}

    //Buscar dados do medico

    get("/perfil/medico/{crm}/dados"){
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

    //Atualização parcial do medico(nome, telefone)

    put("/medico/{crm?}"){
        var crm = call.parameters["crm"]
        val dadosCadastroMedico = call.receive<Medico>()
        if(crm != null){
            for (i in 0 until sistema.listPaciente.size) {
                if (sistema.listMedico[i].crm.toString() == crm) {
                    sistema.listMedico.remove(sistema.listMedico[i])
                    val medicoAtualizado = sistema.cadastroMedico(dadosCadastroMedico.nome!!,
                        dadosCadastroMedico.idade!!, dadosCadastroMedico.cpf!!, dadosCadastroMedico.telefone!!,
                        dadosCadastroMedico.crm!!, dadosCadastroMedico.especializacao!!, dadosCadastroMedico.email!!,
                        dadosCadastroMedico.nomeUsuario!!)
                    call.respond(medicoAtualizado)
                }
            }
        }else{
            call.respondText {"crm invalido"}
        }
    }

    /**TODO Buscar consultas pelo id do medico, atualização parcial(nome, telefone)
     */
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

    //atualizar o nome e o telefone do paciente

    /**TODO Atualizar parcialmente(nome e telefone)*/

    put("/perfil/paciente/{id}/atualizar"){
        var nome = call.receive<Paciente>()
    }

    //Busca pelo id do paciente

    get("/perfil/paciente/{id?}/dados"){
        val idPessoa = call.parameters["id"]
        var error = ServerError(
            HttpStatusCode.NotFound.value, "Id não encontrado na requisição . Passe o parametro id para " +
                    "continuar!"
        )
        if(idPessoa == null) {
            call.respond(HttpStatusCode.NotFound, error)
        }else{
            for (i in 0 until sistema.listFuncionario.size) {
                if (sistema.listFuncionario[i].id == idPessoa) {
                    if (idPessoa != null) {
                        for (i in 0 until sistema.listPaciente.size) {
                            if (sistema.listPaciente[i].id == idPessoa) {
                                call.respond(sistema.listPaciente[i])
                            }
                        }
                    } else {
                        call.respondText { "id invalido" }
                    }
                    break
                } else if (i == sistema.listFuncionario.size && sistema.listFuncionario[i].id != idPessoa) {
                    error = ServerError(
                        HttpStatusCode.NotFound.value,
                        "Id inválido . Passe o parametro id correto para" +
                                "continuar!"
                    )
                    call.respond(HttpStatusCode.Unauthorized, error)
                }
            }
        }



    }

    //Busca de consulta pelo numCartaoConsulta do paciente

    get("/perfil/paciente/{id?}/buscarConsultas"){
        var numCartaoConsulta = call.parameters["id"]
        if (numCartaoConsulta != null) {
            for (i in 0 until sistema.listConsulta.size) {
                if (sistema.listConsulta[i].idPaciente == numCartaoConsulta) {
                    call.respond(sistema.listConsulta[i])
                }
            }
        } else {
            call.respondText { "numCartaoConsulta invalido" }
        }
    }
}