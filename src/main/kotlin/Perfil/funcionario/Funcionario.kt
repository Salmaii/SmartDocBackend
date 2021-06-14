package perfil.funcionario

import perfil.Perfil

class Funcionario: Perfil() {
    var matricula : String? = null
    val permissao = permissoes.FUNCIONARIO

}

