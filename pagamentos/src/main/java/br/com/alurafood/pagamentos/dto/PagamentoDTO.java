package br.com.alurafood.pagamentos.dto;

import br.com.alurafood.pagamentos.model.Pagamento;
import br.com.alurafood.pagamentos.model.PagamentoStatus;
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

    public PagamentoDTO(Pagamento pagamento) {
        this(pagamento.getId(), pagamento.getValor(), pagamento.getNome(), pagamento.getNumero(),
             pagamento.getExpiracao(),
             pagamento.getCodigo(), pagamento.getStatus(), pagamento.getFormaDePagamentoId(),
             pagamento.getPedidoId());
    }

    public Pagamento converterParaPagamento() {
        return new Pagamento(valor, nome, numero, expiracao, codigo, formaDePagamentoId, pedidoId);
    }

    @Override
    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
