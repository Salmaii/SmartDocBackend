package com.csan.smartdoc.entity

import java.time.LocalDateTime
import javax.persistence.*
import com.csan.smartdoc.entity.Local;

@Entity
@Table(name = "TB_CONSULTA")
class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_CONSULTA")
    var id : Long? = null;

    @Column( name = "DTA_CONSULTA")
    var data: LocalDateTime? = null;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn( name = "ID_PACIENTE")
    var paciente: Paciente? = null;

    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn( name ="ID_MEDICO" )
    var medico: Medico? = null;

    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn( name = "ID_LOCAL")
    var local: Local? = null ;

}