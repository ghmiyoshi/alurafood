package br.com.alurafood.pedidos.config.amqp;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PedidoAMQPConfiguration {

    @Bean
    public Jackson2JsonMessageConverter converteMensagem() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate criaRabbitTemplate(final ConnectionFactory connectionFactory,
                                             final Jackson2JsonMessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }

    @Bean
    public Queue criaFilaDetalhesPedido() {
        return QueueBuilder.nonDurable("pagamentos.detalhes-pedido").build();
    }

    @Bean
    public FanoutExchange criaFanoutExchange() {
        return ExchangeBuilder.fanoutExchange("pagamentos.ex").build();
    }

    @Bean
    public Binding bindingPagamentoPedido() {
        return BindingBuilder.bind(criaFilaDetalhesPedido()).to(criaFanoutExchange());
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
