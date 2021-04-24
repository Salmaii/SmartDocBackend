import pessoa.funcionario.Funcionario
import pessoa.medico.Medico
import pessoa.paciente.Paciente
import kotlin.random.Random

class Sistema {
    var listConsulta = mutableListOf<Consulta>()
    var listMedico = mutableListOf<Medico>()
    var listFuncionario = mutableListOf<Funcionario>()
    var listPaciente = mutableListOf<Paciente>()
    val geradorRandom = Random(4350)

    fun Marcar (paciente: Paciente, medico: Medico, local: Local, data: String, hora: String, motivo: String): Consulta {
        var consulta: Consulta = Consulta()
        val numRandom = geradorRandom.nextInt()

        consulta.codigo = numRandom.toString()
        consulta.medico = medico
        consulta.paciente = paciente
        consulta.local = local
        consulta.data = data
        consulta.hora = hora
        consulta.motivo = motivo


        listConsulta.add(consulta)
        return consulta
    }

    fun cadastroPaciente(nomePaciente: String, idadePaciente: Int, cpfPaciente: String, telefonePaciente: String, numCartaoConsultaPaciente: String): Paciente {
        var paciente: Paciente = Paciente()

        paciente.numCartaoConsulta = numCartaoConsultaPaciente

        paciente.nome = nomePaciente
        paciente.idade = idadePaciente
        paciente.cpf = cpfPaciente
        paciente.telefone = telefonePaciente

        listPaciente.add(paciente)
        return paciente
    }

    fun cadastroMedico(nomeMedico:String, idadeMedico: Int, cpfMedico: String, telefonePaciente: String, numCrm: Int, especialização: String): Medico {
        var medico: Medico = Medico()

        medico.nome = nomeMedico
        medico.idade = idadeMedico
        medico.cpf = cpfMedico
        medico.telefone = telefonePaciente

        medico.crm = numCrm
        medico.especializacao = especialização

        listMedico.add(medico)

        return medico
    }

    fun cadastroFuncionario(nomeFuncionario: String, idadeFuncionario: Int, cpfFuncionario: String, telefoneFuncionario: String, matriculaFuncionario: String): Funcionario {
        var funcionario: Funcionario = Funcionario()

        funcionario.nome = nomeFuncionario
        funcionario.idade = idadeFuncionario
        funcionario.cpf = cpfFuncionario
        funcionario.telefone = telefoneFuncionario
        funcionario.matricula = matriculaFuncionario

        listFuncionario.add(funcionario)

        return funcionario
    }

}