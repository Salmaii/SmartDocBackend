# API de Marcação de Consultas Médicas

<p>
Com o intuito de facilitar a marcação de consultas, surge o SmartDoc. 
Diretamente pela API o usuário/paciente tem acesso a maiores informações sobre os profissionais, formações e especializações. Caso tenha urgência em agendar uma consulta, pode fazê-la no mesmo dia.
<p>

### Tecnologias utilizadas

<p>
- Kotlin <br>
- Ktor <br>
- 
<p>

## Endpoint

| Método | URL                                        | Descrição                |
| ------ | ------------------------------------------ | ------------------------ |
| POST   | http://localhost:9999/cadastro/paciente    | Cadastra um paciente.    |
| POST   | http://localhost:9999/cadastro/medico      | Cadastra um médico.      |
| POST   | http://localhost:9999/cadastro/funcionario | Cadastra um funcionario  |
| POST   | http://localhost:9999/consulta             | Registra uma consulta    |
| GET    | http://localhost:9999/pacientes            | Lista todos os pacientes |

## Integrantes do Projeto
