import Pessoa.Medico.Medico
import Pessoa.Paciente.Paciente

class Consulta {
    var codigo: String? = null
    var hora: String? = null
    var data: String? = null
    var nomeClinica = Local().nomedaClinica
    var cidade = Local().cidade
    var estado = Local().estado
    var cep = Local().cep
    var nomeMedico  = Medico().nome
    var crm = Medico().crm
    var nomePaciente = Paciente().nome
    var cpfPaciente = Paciente().cpf
}