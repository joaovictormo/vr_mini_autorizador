package com.vr.miniautorizador.exceptions;

public class TransacaoNaoAutorizadaException extends RuntimeException {
  private final CodigoTransacaoNaoAutorizada codigo;

  public TransacaoNaoAutorizadaException(final CodigoTransacaoNaoAutorizada codigo) {
    super(codigo.name());
    this.codigo = codigo;
  }

  public CodigoTransacaoNaoAutorizada getCodigo() {
    return codigo;
  }
}
