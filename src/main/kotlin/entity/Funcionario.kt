package com.csan.smartdoc.entity

import Pessoa
import javax.persistence.Column
import javax.persistence.Entity

@Entity
class Funcionario : Pessoa() {

    @Column(name = "MATRICULA")
    open var matricula: String? = null
}