package com.vr.miniautorizador.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.vr.miniautorizador.dtos.CartaoDTO;
import com.vr.miniautorizador.exceptions.CartaoDuplicadoException;
import com.vr.miniautorizador.mappers.CartaoMapper;
import com.vr.miniautorizador.models.Cartao;
import com.vr.miniautorizador.repositories.CartaoRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CartaoServiceTest {

  @Mock private CartaoRepository cartaoRepository;

  @Mock private CartaoMapper cartaoMapper;

  @InjectMocks private CartaoService cartaoService;

  private static final String NUMERO_CARTAO = "1234567890123456";
  private static final String SENHA_CARTAO = "1234";

  @Test
  void cadastrar_deveLancarExcecao_quandoNumeroCartaoJaExiste() {
    final var request = new CartaoDTO(NUMERO_CARTAO, SENHA_CARTAO);
    final var cartaoExistente = new Cartao(NUMERO_CARTAO, SENHA_CARTAO);

    when(cartaoRepository.findByNumeroCartao(NUMERO_CARTAO))
        .thenReturn(Optional.of(cartaoExistente));

    assertThrows(CartaoDuplicadoException.class, () -> cartaoService.cadastrar(request));

    verify(cartaoRepository, never()).save(org.mockito.ArgumentMatchers.any(Cartao.class));
    verify(cartaoMapper, never()).map(org.mockito.ArgumentMatchers.any(Cartao.class));
  }

  @Test
  void cadastrar_deveSalvarERetornarDto_quandoNumeroCartaoNaoExiste() {
    final var request = new CartaoDTO(NUMERO_CARTAO, SENHA_CARTAO);
    final var cartaoSalvo = new Cartao(NUMERO_CARTAO, SENHA_CARTAO);
    final var dtoEsperado = new CartaoDTO(NUMERO_CARTAO, SENHA_CARTAO);

    when(cartaoRepository.findByNumeroCartao(NUMERO_CARTAO)).thenReturn(Optional.empty());
    when(cartaoRepository.save(org.mockito.ArgumentMatchers.any(Cartao.class)))
        .thenReturn(cartaoSalvo);
    when(cartaoMapper.map(cartaoSalvo)).thenReturn(dtoEsperado);

    final var resultado = cartaoService.cadastrar(request);

    assertEquals(dtoEsperado, resultado);
    verify(cartaoRepository).save(org.mockito.ArgumentMatchers.any(Cartao.class));
    verify(cartaoMapper).map(cartaoSalvo);
  }
}
