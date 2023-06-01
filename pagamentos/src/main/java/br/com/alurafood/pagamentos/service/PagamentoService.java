package br.com.alurafood.pagamentos.service;

import br.com.alurafood.pagamentos.dto.PagamentoDTO;
import br.com.alurafood.pagamentos.model.Pagamento;
import br.com.alurafood.pagamentos.repository.PagamentoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PagamentoService {

    private final PagamentoRepository pagamentoRepository;

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
                () -> new EntityNotFoundException("Pagamento não encontrado com esse ID"));
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
        pagamento = pagamento.atualizarPagamento(pagamentoDTO);
        pagamento = pagamentoRepository.save(pagamento);
        log.info("{}::atualizarPagamento - Dados salvos: {}", getClass().getSimpleName(), pagamento);
        return new PagamentoDTO(pagamento);
    }

    @Transactional
    public void excluirPagamento(final Long id) {
        var pagamento = buscarPagamento(id);
        if (!pagamento.isAtivo()) {
            throw new EntityNotFoundException("Pagamento já está inativo");
        }
        pagamento.setAtivo(false);
        log.info("{}::excluirPagamento - Pagamento excluído: {}", getClass().getSimpleName(), id);
    }

}
