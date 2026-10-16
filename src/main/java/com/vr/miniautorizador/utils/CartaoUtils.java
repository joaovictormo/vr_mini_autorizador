package com.vr.miniautorizador.utils;

import com.vr.miniautorizador.exceptions.CodigoTransacaoNaoAutorizada;
import com.vr.miniautorizador.exceptions.TransacaoNaoAutorizadaException;
import java.util.Objects;
import java.util.Optional;

public final class CartaoUtils {

  private CartaoUtils() {}

  public static void validarSenha(final String senhaCartao, final String senhaInformada) {
    Optional.ofNullable(senhaCartao)
        .filter(senha -> Objects.equals(senha, senhaInformada))
        .orElseThrow(
            () -> new TransacaoNaoAutorizadaException(CodigoTransacaoNaoAutorizada.SENHA_INVALIDA));
  }
}
