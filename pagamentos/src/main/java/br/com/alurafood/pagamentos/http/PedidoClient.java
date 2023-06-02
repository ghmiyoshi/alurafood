package br.com.alurafood.pagamentos.http;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient("${ms-pedidos-name}")
public interface PedidoClient {

    @PutMapping("/pedidos/{id}/pago")
    void atualizarPedido(@PathVariable Long id);

}
