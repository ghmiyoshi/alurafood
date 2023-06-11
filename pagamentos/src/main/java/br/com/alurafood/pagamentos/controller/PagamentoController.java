package br.com.alurafood.pagamentos.controller;

import br.com.alurafood.pagamentos.dto.PagamentoDTO;
import br.com.alurafood.pagamentos.service.PagamentoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pagamentos")
@RequiredArgsConstructor
public class PagamentoController {

    private final PagamentoService pagamentoService;
    private final RabbitTemplate rabbitTemplate;

    @GetMapping
    public Page<PagamentoDTO> buscarTodos(@PageableDefault final Pageable paginacao) {
        return pagamentoService.buscarTodos(paginacao);
    }

    @GetMapping("/{id}")
    public PagamentoDTO buscarPorId(@PathVariable @NotNull final Long id) {
        return pagamentoService.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PagamentoDTO cadastrarPagamento(@RequestBody @Valid final PagamentoDTO pagamentoDTO) {
        PagamentoDTO pagamento = pagamentoService.criarPagamento(pagamentoDTO);
        rabbitTemplate.send("pagamento.concluido", new Message(
                "Criei um pagamento com o id %s".formatted(pagamento.id()).getBytes()));
        return pagamento;
    }

    @PutMapping("/{id}")
    public PagamentoDTO atualizarPagamento(@RequestBody @Valid final PagamentoDTO pagamentoDTO,
                                           @PathVariable final Long id) {
        return pagamentoService.atualizarPagamento(id, pagamentoDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluirPagamento(@PathVariable final Long id) {
        pagamentoService.excluirPagamento(id);
    }

    @PatchMapping("/{id}/confirmar")
    public void confirmarPagamento(@PathVariable @NotNull final Long id) {
        pagamentoService.confirmarPagamento(id);
    }

}
