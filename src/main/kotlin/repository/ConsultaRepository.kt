package com.csan.smartdoc.repository

import com.csan.smartdoc.entity.Consulta
import org.springframework.data.jpa.repository.JpaRepository

interface ConsultaRepository : JpaRepository<Consulta?, Long?> {

    fun findAllByMedicoId(medico: Long): List<Consulta>;

    fun findAllByPacienteId(idPaciente: Long): List<Consulta>;

    fun findAllByLocalId(idLocal: Long): List<Consulta>;
}