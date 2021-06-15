package perfil

import java.util.*

open class Perfil {
    open var id: String? = UUID.randomUUID().toString()
    open var idade: Int? = null
    open var nome: String? = null
    open var cpf: String? = null
    open var telefone: String? = null

    open var email: String? = null
    open var nomeUsuario : String? = null

    constructor(idade: Int?, nome: String?, cpf: String?, telefone: String?) {
        this.idade = idade
        this.nome = nome
        this.cpf = cpf
        this.telefone = telefone
    }

    constructor()
}