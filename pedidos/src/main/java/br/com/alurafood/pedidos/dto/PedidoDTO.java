package br.com.alurafood.pedidos.dto;

import br.com.alurafood.pedidos.model.Status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public record PedidoDTO(Long id,
                        LocalDateTime dataHora,
                        Status status,
                        List<ItemDoPedidoDTO> itens) {
    public PedidoDTO(Long id, LocalDateTime dataHora, Status status) {
        this(id, dataHora, status, new ArrayList<>());
    }
}
