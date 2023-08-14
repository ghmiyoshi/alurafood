## 1. Curso de Microsserviços na prática: implementando com Java e Spring
* Criação de microsserviço com Java e Spring, conectando a um banco de dados MySQL;
* Service discovery utilizando o Eureka;
* Centralização de requisições adicionando API Gateway ao projeto;
* Comunicação síncrona entre dois microsserviços com Open Feign;
* Circuit breaker e fallback.

## 2. Curso de Microsserviços na prática: mensageria com RabbitMQ
* Principais conceitos relacionados à mensageria;
* Comunicação assíncrona em microsserviços utilizando RabbitMQ;
* Tratamento de falhas no consumo de mensagem;
* Criação de cluster para garantir a alta disponibilidade da comunicação.
<br/>

## Conteúdos interessantes aprendidos neste curso:
* Configuração dead letter queue.
  https://github.com/ghmiyoshi/ms-alurafood/blob/main/avaliacao/src/main/java/br/com/alurafood/avaliacao/avaliacao/amqp/AvaliacaoAMQPConfiguration.java
<br/>

## Melhorias feitas:
* Sobre escrever rotas no gateway. Ex: /courses do microserviço -> /ms-ead/course/courses no gateway;
  https://github.com/ghmiyoshi/ms-alurafood/blob/main/gateway/src/main/resources/application.yml

* Criação de filter no gateway para exibir no console a request e response;
  https://github.com/ghmiyoshi/ms-alurafood/blob/main/gateway/src/main/java/br/com/alurafood/gateway/filter/LoggingFilter.java

* Uso do micrometer para rastreabilidade de logs de cada requisição nos microserviços e set tracer id no header request-id do response.
  https://github.com/ghmiyoshi/ms-alurafood/blob/main/pagamentos/src/main/java/br/com/alurafood/pagamentos/config/filter/TraceFilter.java
