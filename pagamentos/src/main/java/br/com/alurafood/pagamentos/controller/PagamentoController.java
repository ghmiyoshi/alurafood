package br.com.alurafood.pagamentos.controller;

import br.com.alurafood.pagamentos.dto.PagamentoDTO;
import br.com.alurafood.pagamentos.service.PagamentoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
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

    @GetMapping
    public Page<PagamentoDTO> buscarTodos(@PageableDefault Pageable paginacao) {
        return pagamentoService.buscarTodos(paginacao);
    }

    @GetMapping("/{id}")
    public PagamentoDTO buscarPorId(@PathVariable @NotNull Long id) {
        return pagamentoService.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PagamentoDTO cadastrarPagamento(@RequestBody @Valid PagamentoDTO pagamentoDTO) {
        return pagamentoService.criarPagamento(pagamentoDTO);
    }

    @PutMapping("/{id}")
    public PagamentoDTO atualizarPagamento(@RequestBody @Valid PagamentoDTO pagamentoDTO, @PathVariable Long id) {
        return pagamentoService.atualizarPagamento(id, pagamentoDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluirPagamento(@PathVariable Long id) {
        pagamentoService.excluirPagamento(id);
    }

    @PatchMapping("/{id}/confirmar")
    public void confirmarPagamento(@PathVariable @NotNull Long id) {
        pagamentoService.confirmarPagamento(id);
    }

}
