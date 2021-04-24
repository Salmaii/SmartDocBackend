# API de Marcação de Consultas Médicas

<p>
Com o intuito de facilitar a marcação de consultas, surge o SmartDoc. 
Diretamente pela API o usuário/paciente tem acesso a maiores informações sobre os profissionais, formações e especializações. Caso tenha urgência em agendar uma consulta, pode fazê-la no mesmo dia.
<p>

### Tecnologias utilizadas

<h4><b> 
- Kotlin <br>
- Ktor <br> <br>
</b></h4>

## Endpoints

| Método | URL                                                | Descrição                            |
| ------ | -------------------------------------------------- | ------------------------------------ |
| POST   | http://localhost:9999/cadastro/paciente            | Cadastra um paciente.                |
| POST   | http://localhost:9999/cadastro/medico              | Cadastra um médico.                  |
| POST   | http://localhost:9999/cadastro/funcionario         | Cadastra um funcionario              |
| POST   | http://localhost:9999/consulta                     | Registra uma consulta                |
| GET    | http://localhost:9999/pacientes                    | Lista todos os pacientes             |
| GET    | http://localhost:9999/paciente/{numCartaoConsulta} | Procura paciente por id              |
| GET    | http://localhost:9999/medico/{crm?}                | Procurar médicos por id (CRM)        |
| DELETE | http://localhost:9999/consultas                    | Deleta todas as consultas            |
| DELETE | http://localhost:9999/consultas/{codigo?}          | Deleta a consultas por id            |
| DELETE | http://localhost:9999/medicos                      | Deleta registros de todo os médicos. |
| DELETE | http://localhost:9999/medicos/{crm?}               | Deleta registro de médicos por id    |

## Integrantes do Projeto

| Nome                              | Matrícula  |
| --------------------------------- | ---------- |
| Erika Bianca Csan                 | 1612082057 |
| Gabriel Henrique Alberto de Jesus | 1812130083 |
| Gabriel Salmai Camargo Farias     | 1922130017 |
| Luís Eduardo dos Anjos            | 1912130019 |

## Próximos passos

<h4><b> 
- Pacientes poder avaliar médicos <br>
- Pacientes poderem avaliar atendimentos realizados pelos funcionário <br>
</b></h4>
