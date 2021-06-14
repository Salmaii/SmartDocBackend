package perfil.paciente

import perfil.Perfil

class Paciente: Perfil() {
     var numCartaoConsulta: String? = null
     val permissao = permissoes.PACIENTE
}
