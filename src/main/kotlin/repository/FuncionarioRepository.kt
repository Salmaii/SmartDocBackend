package com.csan.smartdoc.repository

import com.csan.smartdoc.entity.Funcionario
import org.springframework.data.jpa.repository.JpaRepository

interface FuncionarioRepository : JpaRepository<Funcionario?, Long?>