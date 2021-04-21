package Pessoa

open class Pessoa {
    open var idade: Int? = null
    open var nome: String? = "Luis"
    open var cpf: String? = null
    open var telefone: String? = null

    constructor(idade: Int?, nome: String?, cpf: String?, telefone: String?) {
        this.idade = idade
        this.nome = nome
        this.cpf = cpf
        this.telefone = telefone
    }

    constructor()
}