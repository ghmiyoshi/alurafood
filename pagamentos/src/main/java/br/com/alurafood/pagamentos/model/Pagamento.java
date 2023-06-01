package br.com.alurafood.pagamentos.model;

import br.com.alurafood.pagamentos.dto.PagamentoDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Entity
@Table(name = "pagamentos")
@Builder
@Getter
@AllArgsConstructor
public class Pagamento extends AbstractDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Positive
    private BigDecimal valor;

    @NotBlank
    @Size(max = 100)
    private String nome;

    @NotBlank
    @Size(max = 19)
    private String numero;

    @NotBlank
    @Size(max = 7)
    private String expiracao;

    @NotBlank
    @Size(min = 3, max = 3)
    private String codigo;

    @NotNull
    @Enumerated(EnumType.STRING)
    private PagamentoStatus status;

    @NotNull
    private Long pedidoId;

    @NotNull
    private Long formaDePagamentoId;

    public Pagamento() {
        this.status = PagamentoStatus.CRIADO;
    }

    public Pagamento(BigDecimal valor, String nome, String numero, String expiracao, String codigo,
                     Long formaDePagamentoId, Long pedidoId) {
        this();
        this.valor = valor;
        this.nome = nome;
        this.numero = numero;
        this.expiracao = expiracao;
        this.codigo = codigo;
        this.formaDePagamentoId = formaDePagamentoId;
        this.pedidoId = pedidoId;
    }

    public Pagamento atualizarPagamento(PagamentoDTO pagamentoDTO) {
        this.status = pagamentoDTO.status();
        return this;
    }

}
