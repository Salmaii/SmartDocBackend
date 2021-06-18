# API de Marcação de Consultas Médicas

<p>
Com o intuito de facilitar a marcação de consultas, surge o SmartDoc.
Diretamente pela API o usuário paciente/médico/funcionário tem acesso
as consultas marcadas podendo ter um maior controle das mesmas.
O SmartDoc funciona como um intermedio do paciente e da clinica/médico.
<p>
  
  https://youtu.be/<https://youtu.be/ZYmp9GjQkII>

### Tecnologias utilizadas

<h4><b>
- Kotlin <br>
- Ktor <br> <br>
  </b></h4>

### IDs

<h4><b>
- Consulta     - Id{codigo?}<br>
- Funcionário  - Id{matricula?}<br>
- Médico       - Id{crm?}<br>
- Paciente     - Id{numCartaoConsulta?}<br>
  </b></h4>

## Endpoints

| Método | URL                                                                                | Descrição                                                       |
| ------ | ---------------------------------------------------------------------------------- | --------------------------------------------------------------- |
| Perfil Geral                                                                                                                                                  |
| GET    | http://localhost:9999/perfil                                                       | Verifica se tem perfil logado.                                  |
| GET    | http://localhost:9999/perfil/cadastro                                              | Mostra rotas de cadastro de perfil em geral.                    |
| GET    | http://localhost:9999/perfil/login                                                 | Mostra rotas de login de perfil em geral.                       |
| Perfil Funcionário                                                                                                                                            |
| POST   | http://localhost:9999/perfil/cadastro/funcionario                                  | Cadastro de funcionário                                         |
| POST   | http://localhost:9999/perfil/login/funcionario                                     | Login de funcionário                                            |
| POST   | http://localhost:9999/perfil/funcionario/{id}/cadastro/consulta                    | Cadastro de consulta                                            |
| GET    | http://localhost:9999/perfil/funcionario/{id?}/funcionarios                        | Mostra todos os funcionários                                    |
| GET    | http://localhost:9999/perfil/funcionario/{id?}/consultas                           | Mostra todas as consultas                                       |
| GET    | http://localhost:9999/perfil/funcionario/{id?}/medicos                             | Mostra todos os médicos                                         |
| GET    | http://localhost:9999/perfil/funcionario/{id?}/pacientes                           | Mostra todos os pacientes                                       |
| GET    | http://localhost:9999/perfil/funcionario/{id?}/funcionario/{matricula?}/dados      | Mostra funcionario por id{matricula?}                           |
| GET    | http://localhost:9999/perfil/funcionario/{id?}/consulta/{codigo?}/dados            | Mostra consulta por id{codigo?}                                 |
| GET    | http://localhost:9999/perfil/funcionario/{id?}/medico/{crm?}/dados                 | Mostra médico por id{crm?}                                      |
| GET    | http://localhost:9999/perfil/funcionario/{id?}/paciente/{numCartaoConsulta?}/dados | Mostra paciente por id{numCartaoConsulta?}                      |
| PUT    | http://localhost:9999/perfil/funcionario/{id?}/funcionario/{matricula?}            | Atualização geral do perfil de funcionário de id{matricula?}    |
| PUT    | http://localhost:9999/perfil/funcionario/{id?}/consulta/{codigo?}                  | Atualização geral da consulta de id{código?}                    |
| DELETE | http://localhost:9999/perfil/funcionario/{id?}/consultas                           | Deleta todas as consultas                                       |
| DELETE | http://localhost:9999/perfil/funcionario/{id?}/medicos                             | Deleta todos os medicos                                         |
| DELETE | http://localhost:9999/perfil/funcionario/{id?}/pacientes                           | Deleta todos os pacientes                                       |
| DELETE | http://localhost:9999/perfil/funcionario/{id?}/funcionarios                        | Deleta todos os funcionarios                                    |
| DELETE | http://localhost:9999/perfil/funcionario/{id?}/consulta/{codigo?}                  | Deleta uma consulta de {codigo?}                                |
| DELETE | http://localhost:9999/perfil/funcionario/{id?}/medico/{crm?}                       | Delete um médico de id{crm?}                                    |
| DELETE | http://localhost:9999/perfil/funcionario/{id?}/paciente/{numCartaoConsulta?}       | Deleta um paciente de id{numCartaoConsulta?}                    |
| DELETE | http://localhost:9999/perfil/funcionario/{id?}/funcionario/{matricula?}            | Deleta um funcionario de id{matricula?}                         |
| Perfil Médico                                                                                                                                                 |
| POST   | http://localhost:9999/perfil/cadastro/medico                                       | Cadastro de medico                                              |
| POST   | http://localhost:9999/perfil/login/medico                                          | Login de médico                                                 |
| GET    | http://localhost:9999/perfil/medico/{id?}/consultas                                | Mostra todas as consultas do médico de id{crm?}                 |
| GET    | http://localhost:9999/perfil/medico/{id?}/dados                                    | Mostra perfil do médico de id{crm?}                             |
| PUT    | http://localhost:9999/perfil/medico/{id?}/atualizar                                | Atualização geral do perfil do médico de id{crm?}               |
| DELETE | http://localhost:9999/perfil/medico/{id?}/delete                                   | Delete do perfil de médico de id{crm?}                          |
| Perfil Paciente                                                                                                                                               |
| POST   | http://localhost:9999/perfil/cadastro/paciente                                     | Cadastro de paciente                                            |
| POST   | http://localhost:9999/perfil/login/paciente                                        | Login de paciente                                               |
| GET    | http://localhost:9999/perfil/paciente/{id?}/buscarConsultas                        | Mostra todas as consultas do paciente de id{numCartaoConsulta?} |
| GET    | http://localhost:9999/perfil/paciente/{id?}/dados                                  | Mostra perfil do paciente de id{numCartaoConsulta?}             |
| PUT    | http://localhost:9999/perfil/paciente/{id?}                                        | Atualização geral do perfil do paciente de id{crm?}             |
| DELETE | http://localhost:9999/perfil/paciente/{id?}/delete                                 | Delete do perfil de paciente de id{numCartaoConsulta?}          |




## Integrantes do Projeto

| Nome                              | Matrícula  |
| --------------------------------- | ---------- |
| Gabriel Henrique Alberto de Jesus | 1812130083 |
| Gabriel Salmai Camargo Farias     | 1922130017 |

## Próximos passos

<h4><b>
- Pacientes poderem avaliar médicos <br>
- Pacientes poderem avaliar atendimentos realizados pelos funcionário <br>
- O médico passar comentários sobre o tratamento (Pdf de prescrição, recomendações e contraindicações) <br>
- Calendário do paciente e do medico <br>
- Notificações sobre os horários das consultas <br>
- Paciente indicar em um desenho do corpo humano aonde está afetado <br>
- Usuário/paciente tem acesso a maiores informações sobre os profissionais, formações e especializações <br>
- Mostrar os profissionais/clinicas que façam a consulta mais proxima, em tempo e/ou distancia <br>
- Mostrar os profissionais/clinicas atendam por especializações <br>
  </b></h4>
