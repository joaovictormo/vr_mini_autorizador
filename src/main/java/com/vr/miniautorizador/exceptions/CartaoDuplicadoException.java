package com.vr.miniautorizador.exceptions;

import com.vr.miniautorizador.dtos.CartaoDTO;

public class CartaoDuplicadoException extends RuntimeException {

  private final CartaoDTO cartao;

  public CartaoDuplicadoException(final CartaoDTO cartao) {
    this.cartao = cartao;
  }

  public CartaoDTO getCartao() {
    return cartao;
  }
}
