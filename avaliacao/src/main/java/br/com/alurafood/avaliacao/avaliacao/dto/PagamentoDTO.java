package br.com.alurafood.avaliacao.avaliacao.dto;

import java.math.BigDecimal;

public record PagamentoDTO(Long id,
                           BigDecimal valor,
                           String nome,
                           String numero,
                           String expiracao,
                           String codigo,
                           StatusPagamento status,
                           Long formaDePagamentoId,
                           Long pedidoId) {
}
