package br.com.alurafood.pedidos.dto;

import br.com.alurafood.pedidos.model.PagamentoStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;

public record PagamentoDTO(Long id,
                           BigDecimal valor,
                           String nome,
                           String numero,
                           String expiracao,
                           String codigo,
                           PagamentoStatus status,
                           Long formaDePagamentoId,
                           Long pedidoId) {
    @Override
    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
