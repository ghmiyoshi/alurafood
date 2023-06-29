package br.com.alurafood.pagamentos.service;

import br.com.alurafood.pagamentos.dto.PagamentoDTO;
import br.com.alurafood.pagamentos.http.PedidoClient;
import br.com.alurafood.pagamentos.model.Pagamento;
import br.com.alurafood.pagamentos.model.PagamentoStatus;
import br.com.alurafood.pagamentos.repository.PagamentoRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@RequiredArgsConstructor
public class PagamentoService {

    private final PagamentoRepository pagamentoRepository;
    private final PedidoClient pedidoClient;

    public Page<PagamentoDTO> buscarTodos(final Pageable paginacao) {
        log.info("{}::obterTodos - Buscando todos os pagamentos", getClass().getSimpleName());
        return pagamentoRepository.findAll(paginacao)
                .map(PagamentoDTO::new);
    }

    public PagamentoDTO buscarPorId(final Long id) {
        log.info("{}::buscarPorId - Buscando pagamento pelo id: {}", getClass().getSimpleName(), id);
        return new PagamentoDTO(buscarPagamento(id));
    }

    public Pagamento buscarPagamento(final Long id) {
        return pagamentoRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Pagamento não encontrado"));
    }

    public PagamentoDTO criarPagamento(final PagamentoDTO pagamentoDTO) {
        log.info("{}::criarPagamento - Dados recebidos: {}", getClass().getSimpleName(), pagamentoDTO);
        var pagamento = pagamentoDTO.converterParaPagamento();
        pagamento = pagamentoRepository.save(pagamento);
        log.info("{}::criarPagamento - Dados salvos: {}", getClass().getSimpleName(), pagamento);
        return new PagamentoDTO(pagamento);
    }

    public PagamentoDTO atualizarPagamento(final Long id, final PagamentoDTO pagamentoDTO) {
        log.info("{}::atualizarPagamento - Dados recebidos: {}", getClass().getSimpleName(), pagamentoDTO);
        var pagamento = pagamentoRepository.getReferenceById(id);
        pagamento.atualizaStatus(pagamentoDTO.status());
        pagamento = pagamentoRepository.save(pagamento);
        log.info("{}::atualizarPagamento - Dados salvos: {}", getClass().getSimpleName(), pagamento);
        return new PagamentoDTO(pagamento);
    }

    @Transactional
    public void excluirPagamento(final Long id) {
        Pagamento pagamento = buscaPagamentoAtivo(id);
        pagamento.setAtivo(false);
        pagamento.atualizaStatus(PagamentoStatus.CANCELADO);
        log.info("{}::excluirPagamento - Pagamento excluído", getClass().getSimpleName());
    }

    private Pagamento buscaPagamentoAtivo(final Long id) {
        var pagamento = buscarPagamento(id);
        if (pagamento.isAtivo())
            return pagamento;
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pagamento está inativo");
    }

    @CircuitBreaker(name = "atualizaPedido", fallbackMethod = "pagamentoConfirmadoComIntegracaoPendente")
    @Transactional
    public void confirmarPagamento(final Long id) {
        var pagamento = buscaPagamentoAtivo(id);
        pagamento.atualizaStatus(PagamentoStatus.CONFIRMADO);
        pagamentoRepository.save(pagamento);
        pedidoClient.atualizarPedido(pagamento.getPedidoId());
        log.info("{}::confirmarPagamento - Pagamento confirmado", getClass().getSimpleName());
    }

    public void pagamentoConfirmadoComIntegracaoPendente(final Long id, final Exception exception) {
        var pagamento = buscaPagamentoAtivo(id);
        pagamento.atualizaStatus(PagamentoStatus.CONFIRMADO_SEM_INTEGRACAO);
        pagamentoRepository.save(pagamento);
        log.info("{}::confirmarPagamento - Pagamento confirmado sem integração", getClass().getSimpleName());
    }

}
