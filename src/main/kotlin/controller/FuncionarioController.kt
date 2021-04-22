package com.csan.smartdoc.controller

import com.csan.smartdoc.entity.Funcionario
import com.csan.smartdoc.entity.Local
import com.csan.smartdoc.service.FuncionarioService
import com.csan.smartdoc.service.LocalService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.lang.Exception
import javax.websocket.server.PathParam

@RestController
@RequestMapping("/funcionario")
class FuncionarioController {

    @Autowired
    private val funcionarioService: FuncionarioService? = null

    @GetMapping
    fun buscar(): ResponseEntity<*> {
        return try {
            ResponseEntity<Any?>(funcionarioService!!.buscarFuncionarios(), HttpStatus.OK)
        } catch (e: Exception) {
            ResponseEntity<Any?>(null, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @GetMapping("/{id}")
    fun buscarPorId(@PathVariable("id") id: Long?): ResponseEntity<*> {
        return try {
            ResponseEntity<Any?>(funcionarioService!!.buscarFuncionarioPorId(id!!), HttpStatus.OK)
        } catch (e: Exception) {
            ResponseEntity<Any?>(null, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @PostMapping
    fun cadastrar(@RequestBody funcionario: Funcionario): ResponseEntity<*> {
        return try {
            funcionarioService!!.salvarFuncionario(funcionario!!)
            ResponseEntity<Any?>(null, HttpStatus.CREATED)
        } catch (e: Exception) {
            ResponseEntity<Any?>(null, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @PutMapping("/{id}")
    fun atualizarDados(@PathVariable("id") id: Long?,@RequestBody  funcionario: Funcionario): ResponseEntity<*> {
        return try {
            funcionario.id = id
            funcionarioService!!.salvarFuncionario(funcionario)
            ResponseEntity<Any?>(null, HttpStatus.OK)
        } catch (e: Exception) {
            ResponseEntity<Any?>(null, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @DeleteMapping("/{id}")
    fun remover(@PathVariable("id") id: Long?): ResponseEntity<*> {
        return try {
            funcionarioService!!.deletarLocalPorId(id)
            ResponseEntity<Any?>(null, HttpStatus.OK)
        } catch (e: Exception) {
            ResponseEntity<Any?>(null, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}