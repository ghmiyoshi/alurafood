package br.com.alurafood.pedidos.controller;

import br.com.alurafood.pedidos.dto.PedidoDTO;
import br.com.alurafood.pedidos.dto.StatusDTO;
import br.com.alurafood.pedidos.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService service;

    @GetMapping()
    public List<PedidoDTO> listarTodos() {
        return service.obterTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoDTO> listarPorId(@PathVariable @NotNull final Long id) {
        var pedidoDto = service.obterPorId(id);
        return ResponseEntity.ok(pedidoDto);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<PedidoDTO> realizaPedido(@RequestBody @Valid final PedidoDTO dto,
                                                    final UriComponentsBuilder uriBuilder) {
        var pedidoDto = service.criarPedido(dto);
        URI endereco = uriBuilder.path("/pedidos/{id}").buildAndExpand(pedidoDto.id()).toUri();
        return ResponseEntity.created(endereco).body(pedidoDto);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<PedidoDTO> atualizaStatus(@PathVariable final Long id,
                                                    @RequestBody final StatusDTO status) {
        var pedidoDto = service.atualizaStatus(id, status);
        return ResponseEntity.ok(pedidoDto);
    }

    @PutMapping("/{id}/pago")
    public ResponseEntity<Void> aprovaPagamento(@PathVariable @NotNull final Long id) {
        service.aprovaPagamentoPedido(id);
        return ResponseEntity.ok().build();
    }

}
