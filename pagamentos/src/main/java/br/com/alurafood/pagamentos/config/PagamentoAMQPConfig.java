package br.com.alurafood.pagamentos.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PagamentoAMQPConfig {

    @Bean
    public Queue criaFila() {
        //return new Queue("pagamento.concluido", false);
        return QueueBuilder.nonDurable("pagamento.concluido").build();
    }

    @Bean
    public RabbitAdmin criaRabbitAdmin(final ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    // inicializa o rabbit ao iniciar a aplicação
    @Bean
    public ApplicationListener<ApplicationReadyEvent> inicializaAdmin(final RabbitAdmin rabbitAdmin) {
        return event -> rabbitAdmin.initialize();
    }

}
