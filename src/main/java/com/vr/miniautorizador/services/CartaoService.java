package com.vr.miniautorizador.services;

import com.vr.miniautorizador.dtos.CartaoDTO;
import com.vr.miniautorizador.exceptions.CartaoDuplicadoException;
import com.vr.miniautorizador.mappers.CartaoMapper;
import com.vr.miniautorizador.models.Cartao;
import com.vr.miniautorizador.repositories.CartaoRepository;
import org.springframework.stereotype.Service;

@Service
public class CartaoService {
  private final CartaoRepository cartaoRepository;
  private final CartaoMapper cartaoMapper;

  public CartaoService(final CartaoRepository cartaoRepository, final CartaoMapper cartaoMapper) {
    this.cartaoRepository = cartaoRepository;
    this.cartaoMapper = cartaoMapper;
  }

  public CartaoDTO cadastrar(final CartaoDTO cartaoRequest) {
    final var novoCartao = new Cartao(cartaoRequest.numeroCartao(), cartaoRequest.senha());

    cartaoRepository
        .findByNumeroCartao(novoCartao.getNumeroCartao())
        .ifPresent(
            cartaoExistente -> {
              throw new CartaoDuplicadoException(cartaoRequest);
            });

    return cartaoMapper.map(cartaoRepository.save(novoCartao));
  }
}
