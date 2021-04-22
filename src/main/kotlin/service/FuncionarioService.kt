package com.csan.smartdoc.service

import com.csan.smartdoc.entity.Funcionario
import com.csan.smartdoc.entity.Local
import com.csan.smartdoc.repository.FuncionarioRepository
import com.csan.smartdoc.repository.LocalRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.lang.Exception
import javax.persistence.EntityNotFoundException

@Service
class FuncionarioService {

    @Autowired
    private val funcionarioRepository: FuncionarioRepository? = null

    fun salvarFuncionario(funcionario: Funcionario) {
        funcionarioRepository!!.save(funcionario)
    }

    fun buscarFuncionarios(): List<Funcionario?> {
        return funcionarioRepository!!.findAll()
    }

    @Throws(Exception::class)
    fun buscarFuncionarioPorId(id: Long): Funcionario {
        return funcionarioRepository!!.findById(id).orElseThrow { EntityNotFoundException() }!!
    }

    fun deletarLocalPorId(id: Long?) {
        funcionarioRepository!!.deleteById(id!!)
    }
}