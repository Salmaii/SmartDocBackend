import Pessoa.Funcionario.Funcionario
import Pessoa.Medico.Medico
import Pessoa.Paciente.Paciente
import kotlin.random.Random

class Sistema {
    var listConsulta = mutableListOf<Consulta>()
    var listMedico = mutableListOf<Medico>()
    var listFuncionario = mutableListOf<Funcionario>()
    var listPaciente = mutableListOf<Paciente>()
    val geradorRandom = Random(4350)

    fun Marcar (nomePaciente: String, cpfPaciente: String, nomeMedico: String, crm: Int, nomeClinica: String, cidade: String, estado: String, cep: String, data: String, hora: String ): Consulta {
        var consulta: Consulta = Consulta()

        val numRandom = geradorRandom.nextInt()

        consulta.codigo = numRandom.toString()
        consulta.nomePaciente = nomePaciente
        consulta.cpfPaciente = cpfPaciente
        consulta.nomeMedico = nomeMedico
        consulta.crm = crm
        consulta.nomeClinica = nomeClinica
        consulta.cidade = cidade
        consulta.estado = estado
        consulta.cep = cep
        consulta.data = data
        consulta.hora = hora

        //to do motivo do paciente

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
}