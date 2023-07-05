package br.com.alurafood.pedidos.config.amqp;

import br.com.alurafood.pedidos.dto.PagamentoDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PagamentoListener {

    @RabbitListener(queues = "pagamentos.detalhes-pedido")
    public void recebeMensagem(final PagamentoDTO pagamento) {
        String mensagem = """
                Dados do pagamento: %s
                Número do pedido: %s
                Valor: R$ %s
                Status: %s""".formatted(pagamento.id(), pagamento.pedidoId(), pagamento.valor(), pagamento.status());
        log.info(mensagem);
    }

}
