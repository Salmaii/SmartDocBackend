package pessoa

open class Pessoa {
    open var id: Int? = null
    open var idade: Int? = null
    open var nome: String? = null
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