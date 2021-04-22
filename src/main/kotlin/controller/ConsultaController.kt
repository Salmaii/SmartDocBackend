package com.csan.smartdoc.controller

import com.csan.smartdoc.entity.Consulta
import com.csan.smartdoc.entity.Funcionario
import com.csan.smartdoc.service.ConsultaService
import com.csan.smartdoc.service.FuncionarioService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.lang.Exception
import javax.websocket.server.PathParam

@RestController
@RequestMapping("/consulta")
class ConsultaController {

    @Autowired
    private val consultaService: ConsultaService? = null

    @GetMapping
    fun buscar(): ResponseEntity<*> {
        return try {
            ResponseEntity<Any?>(consultaService!!.buscarConsultas(), HttpStatus.OK)
        } catch (e: Exception) {
            ResponseEntity<Any?>(null, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @GetMapping("/{id}")
    fun buscarPorId(@PathVariable("id") id: Long?): ResponseEntity<*> {
        return try {
            ResponseEntity<Any?>(consultaService!!.buscarConsultaPorId(id!!), HttpStatus.OK)
        } catch (e: Exception) {
            ResponseEntity<Any?>(null, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @GetMapping("/medico/{id}")
    fun buscarPorMedicoId(@PathVariable("id") id: Long?): ResponseEntity<*> {
        return try {
            ResponseEntity<Any?>(consultaService!!.buscarConsultasPorMedicoId(id!!), HttpStatus.OK)
        } catch (e: Exception) {
            ResponseEntity<Any?>(null, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @GetMapping("/local/{id}")
    fun buscarPorLocalId(@PathVariable("id") id: Long?): ResponseEntity<*> {
        return try {
            ResponseEntity<Any?>(consultaService!!.buscarConsultasPorLocalId(id!!), HttpStatus.OK)
        } catch (e: Exception) {
            ResponseEntity<Any?>(null, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @GetMapping("/paciente/{id}")
    fun buscarPorPacienteId(@PathVariable("id") id: Long?): ResponseEntity<*> {
        return try {
            ResponseEntity<Any?>(consultaService!!.buscarConsultasPoePaciente(id!!), HttpStatus.OK)
        } catch (e: Exception) {
            ResponseEntity<Any?>(null, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @PostMapping
    fun cadastrar(@RequestBody consulta: Consulta): ResponseEntity<*> {
        return try {
            consultaService!!.salvarConsulta(consulta!!)
            ResponseEntity<Any?>(null, HttpStatus.CREATED)
        } catch (e: Exception) {
            ResponseEntity<Any?>(null, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @PutMapping("/{id}")
    fun atualizarDados(@PathVariable("id") id: Long?, @RequestBody consulta: Consulta): ResponseEntity<*> {
        return try {
            consulta.id = id
            consultaService!!.salvarConsulta(consulta)
            ResponseEntity<Any?>(null, HttpStatus.OK)
        } catch (e: Exception) {
            ResponseEntity<Any?>(null, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @DeleteMapping("/{id}")
    fun remover(@PathVariable("id") id: Long?): ResponseEntity<*> {
        return try {
            consultaService!!.deletarLocalPorId(id)
            ResponseEntity<Any?>(null, HttpStatus.OK)
        } catch (e: Exception) {
            ResponseEntity<Any?>(null, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}