package br.com.alurafood.pedidos.service;

import br.com.alurafood.pedidos.dto.PedidoDTO;
import br.com.alurafood.pedidos.dto.StatusDTO;
import br.com.alurafood.pedidos.model.Pedido;
import br.com.alurafood.pedidos.model.Status;
import br.com.alurafood.pedidos.repository.PedidoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.Objects.isNull;

@Slf4j
@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository repository;
    private final ModelMapper modelMapper;

    public List<PedidoDTO> obterTodos() {
        return repository.findAll().stream()
                .map(p -> modelMapper.map(p, PedidoDTO.class))
                .toList();
    }

    public PedidoDTO obterPorId(final Long id) {
        var pedido = repository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        return modelMapper.map(pedido, PedidoDTO.class);
    }

    public PedidoDTO criarPedido(final PedidoDTO dto) {
        var pedido = modelMapper.map(dto, Pedido.class);
        pedido.setDataHora(LocalDateTime.now());
        pedido.setStatus(Status.REALIZADO);
        pedido.getItens().forEach(item -> item.setPedido(pedido));

        repository.save(pedido);
        return modelMapper.map(pedido, PedidoDTO.class);
    }

    public PedidoDTO atualizaStatus(final Long id, final StatusDTO dto) {
        var pedido = repository.porIdComItens(id);

        if (isNull(pedido)) {
            throw new EntityNotFoundException();
        }

        pedido.setStatus(dto.status());
        repository.atualizaStatus(dto.status(), pedido);
        return modelMapper.map(pedido, PedidoDTO.class);
    }

    public void aprovaPagamentoPedido(final Long id) {
        var pedido = repository.porIdComItens(id);

        if (isNull(pedido)) {
            throw new EntityNotFoundException();
        }

        pedido.setStatus(Status.PAGO);
        repository.atualizaStatus(Status.PAGO, pedido);
    }

}
