package com.vr.miniautorizador.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.vr.miniautorizador.dtos.TransacaoDTO;
import com.vr.miniautorizador.exceptions.CodigoTransacaoNaoAutorizada;
import com.vr.miniautorizador.exceptions.TransacaoNaoAutorizadaException;
import com.vr.miniautorizador.models.Cartao;
import com.vr.miniautorizador.repositories.CartaoRepository;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TransacaoServiceTest {

  @Mock private CartaoRepository cartaoRepository;

  @InjectMocks private TransacaoService transacaoService;

  private static final String NUMERO_CARTAO = "1234567890123456";
  private static final String SENHA_CARTAO = "1234";
  private static final BigDecimal VALOR = new BigDecimal("25.50");

  @Test
  void realizaTransacao_deveConcluir_quandoCartaoSenhaESaldoForemValidos() {
    final var request = new TransacaoDTO(NUMERO_CARTAO, SENHA_CARTAO, VALOR);
    final var cartao = mock(Cartao.class);

    when(cartaoRepository.findByNumeroCartao(NUMERO_CARTAO)).thenReturn(Optional.of(cartao));
    when(cartao.getSenha()).thenReturn(SENHA_CARTAO);
    when(cartaoRepository.debitarSeSaldoSuficiente(NUMERO_CARTAO, VALOR)).thenReturn(1);

    assertDoesNotThrow(() -> transacaoService.realizaTransacao(request));

    verify(cartaoRepository).findByNumeroCartao(NUMERO_CARTAO);
    verify(cartaoRepository).debitarSeSaldoSuficiente(NUMERO_CARTAO, VALOR);
  }

  @Test
  void realizaTransacao_deveLancarSaldoInsuficiente_quandoDebitoNaoAtualizarLinha() {
    final var request = new TransacaoDTO(NUMERO_CARTAO, SENHA_CARTAO, VALOR);
    final var cartao = mock(Cartao.class);

    when(cartaoRepository.findByNumeroCartao(NUMERO_CARTAO)).thenReturn(Optional.of(cartao));
    when(cartao.getSenha()).thenReturn(SENHA_CARTAO);
    when(cartaoRepository.debitarSeSaldoSuficiente(NUMERO_CARTAO, VALOR)).thenReturn(0);

    final var exception =
        assertThrows(
            TransacaoNaoAutorizadaException.class,
            () -> transacaoService.realizaTransacao(request));

    assertEquals(CodigoTransacaoNaoAutorizada.SALDO_INSUFICIENTE, exception.getCodigo());
  }

  @Test
  void realizaTransacao_deveLancarSenhaInvalida_quandoSenhaNaoCorrespondente() {
    final var request = new TransacaoDTO(NUMERO_CARTAO, "9999", VALOR);
    final var cartao = mock(Cartao.class);

    when(cartaoRepository.findByNumeroCartao(NUMERO_CARTAO)).thenReturn(Optional.of(cartao));
    when(cartao.getSenha()).thenReturn(SENHA_CARTAO);

    final var exception =
        assertThrows(
            TransacaoNaoAutorizadaException.class,
            () -> transacaoService.realizaTransacao(request));

    assertEquals(CodigoTransacaoNaoAutorizada.SENHA_INVALIDA, exception.getCodigo());

    verify(cartaoRepository, never()).debitarSeSaldoSuficiente(NUMERO_CARTAO, VALOR);
  }

  @Test
  void realizaTransacao_deveLancarCartaoInexistente_quandoCartaoNaoForEncontrado() {
    final var request = new TransacaoDTO(NUMERO_CARTAO, SENHA_CARTAO, VALOR);

    when(cartaoRepository.findByNumeroCartao(NUMERO_CARTAO)).thenReturn(Optional.empty());

    final var exception =
        assertThrows(
            TransacaoNaoAutorizadaException.class,
            () -> transacaoService.realizaTransacao(request));

    assertEquals(CodigoTransacaoNaoAutorizada.CARTAO_INEXISTENTE, exception.getCodigo());

    verify(cartaoRepository, never()).debitarSeSaldoSuficiente(NUMERO_CARTAO, VALOR);
  }
}
