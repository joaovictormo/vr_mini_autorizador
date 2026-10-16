package com.vr.miniautorizador.exceptions;

import com.vr.miniautorizador.dtos.CartaoDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CartaoExceptionHandler {

  @ExceptionHandler(CartaoDuplicadoException.class)
  public ResponseEntity<CartaoDTO> handleCartaoDuplicadoException(
      final CartaoDuplicadoException ex) {

    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT)
        .contentType(MediaType.APPLICATION_JSON)
        .body(ex.getCartao());
  }

  @ExceptionHandler(CartaoNaoEncontradoException.class)
  ResponseEntity<Void> handleNaoEncontrado(final CartaoNaoEncontradoException exception) {
    return ResponseEntity.notFound().build();
  }

  @ExceptionHandler(TransacaoNaoAutorizadaException.class)
  public ResponseEntity<String> handleTransacaoNaoAutorizadaException(
      final TransacaoNaoAutorizadaException ex) {

    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT)
        .contentType(MediaType.TEXT_PLAIN)
        .body(ex.getCodigo().name());
  }
}
