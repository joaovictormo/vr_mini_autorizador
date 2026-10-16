package com.vr.miniautorizador.services;

import com.vr.miniautorizador.dtos.TransacaoDTO;
import com.vr.miniautorizador.exceptions.CodigoTransacaoNaoAutorizada;
import com.vr.miniautorizador.exceptions.TransacaoNaoAutorizadaException;
import com.vr.miniautorizador.models.Cartao;
import com.vr.miniautorizador.repositories.CartaoRepository;
import com.vr.miniautorizador.utils.CartaoUtils;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import java.util.Optional;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

@Service
public class TransacaoService {

  private final CartaoRepository cartaoRepository;

  public TransacaoService(final CartaoRepository cartaoRepository) {
    this.cartaoRepository = cartaoRepository;
  }

  @Transactional
  public void realizaTransacao(@Valid final TransacaoDTO request) {
    final var cartao = getCartao(request);
    CartaoUtils.validarSenha(cartao.getSenha(), request.senhaCartao());
    debitarSaldo(request);
  }

  private void debitarSaldo(final TransacaoDTO request) {
    final var linhasAtualizadas =
        cartaoRepository.debitarSeSaldoSuficiente(request.numeroCartao(), request.valor());

    Optional.of(linhasAtualizadas)
        .filter(linhas -> linhas > 0)
        .orElseThrow(
            () ->
                new TransacaoNaoAutorizadaException(
                    CodigoTransacaoNaoAutorizada.SALDO_INSUFICIENTE));
  }

  private @NonNull Cartao getCartao(final TransacaoDTO request) {
    return cartaoRepository
        .findByNumeroCartao(request.numeroCartao())
        .orElseThrow(
            () ->
                new TransacaoNaoAutorizadaException(
                    CodigoTransacaoNaoAutorizada.CARTAO_INEXISTENTE));
  }
}
