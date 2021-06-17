
import perfil.Perfil
import perfil.funcionario.Funcionario
import perfil.medico.Medico
import perfil.paciente.Paciente
import servidor.sistema
import kotlin.random.Random

class Sistema {
    var listConsulta = mutableListOf<Consulta>()
    var listMedico = mutableListOf<Medico>()
    var listFuncionario = mutableListOf<Funcionario>()
    var listPaciente = mutableListOf<Paciente>()
    val geradorRandom = Random(4350)

    var currentProfile: Perfil? = null
        private set
    private val profiles = mutableListOf<Perfil>()

    fun Marcar (
        idPaciente: String, idMedico: String, idFuncionario: String, local: String, data: String,
        hora: String, motivo: String): Consulta {
        val consulta: Consulta = Consulta()
        val numRandom = geradorRandom.nextInt()

        consulta.codigo = numRandom.toString()
        consulta.idMedico = idMedico
        consulta.idPaciente = idPaciente
        consulta.idFuncionario = idFuncionario
        consulta.local = local
        consulta.data = data
        consulta.hora = hora
        consulta.motivo = motivo

        listConsulta.add(consulta)
        return consulta
    }

    fun cadastroPaciente(nomePaciente: String, idadePaciente: Int, cpfPaciente: String, telefonePaciente: String,
                         numCartaoConsultaPaciente: String , emailPaciente:String,  nomeUsuarioPaciente : String): Paciente {
        var paciente: Paciente = Paciente()

        paciente.numCartaoConsulta = numCartaoConsultaPaciente

        paciente.nome = nomePaciente
        paciente.idade = idadePaciente
        paciente.cpf = cpfPaciente
        paciente.telefone = telefonePaciente

        paciente.email = emailPaciente
        paciente.nomeUsuario = nomeUsuarioPaciente

        listPaciente.add(paciente)
        return paciente
    }

    fun cadastroMedico(
        nomeMedico: String, idadeMedico: Int, cpfMedico: String, telefonePaciente: String, numCrm: Int,
        especialização: String, emailMedico: String?, nomeUsuarioMedico: String
    ): Medico {
        var medico: Medico = Medico()

        medico.nome = nomeMedico
        medico.idade = idadeMedico
        medico.cpf = cpfMedico
        medico.telefone = telefonePaciente

        medico.crm = numCrm
        medico.especializacao = especialização

        medico.email = emailMedico
        medico.nomeUsuario = nomeUsuarioMedico

        listMedico.add(medico)

        return medico
    }

    fun cadastroFuncionario(nomeFuncionario: String, idadeFuncionario: Int, cpfFuncionario: String,
                            telefoneFuncionario: String, matriculaFuncionario: String, emailFuncionario: String, nomeUsuarioFuncionario   : String): Funcionario {
        var funcionario: Funcionario = Funcionario()

        funcionario.nome = nomeFuncionario
        funcionario.idade = idadeFuncionario
        funcionario.cpf = cpfFuncionario
        funcionario.telefone = telefoneFuncionario
        funcionario.matricula = matriculaFuncionario

        funcionario.email = emailFuncionario
        funcionario.nomeUsuario = nomeUsuarioFuncionario

        listFuncionario.add(funcionario)

        return funcionario
    }

    fun login(email: String): Boolean {
            currentProfile = profiles.firstOrNull { p -> p.email == email }
            return currentProfile != null
    }

    fun validaFuncionario(idFuncionario: String): Boolean {
        var idPessoa = idFuncionario
        for (i in 0 until sistema.listFuncionario.size) {
            if (sistema.listFuncionario[i].id == idPessoa) {
                return true
            }
        }
        return false
    }

    fun validaMedico(idMedico: String) : Boolean {
        var idPessoa = idMedico
        for (i in 0 until sistema.listMedico.size) {
            if (sistema.listMedico[i].id == idPessoa) {
                return true
            }
        }
        return false
    }

    fun validaPaciente(idPaciente: String) : Boolean {
        var idPessoa = idPaciente
        for (i in 0 until sistema.listPaciente.size) {
            if (sistema.listPaciente[i].id == idPessoa) {
                return true
            }
        }
        return false
    }
}