package com.vr.miniautorizador.controllers;

import com.vr.miniautorizador.dtos.CartaoDTO;
import com.vr.miniautorizador.services.CartaoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cartoes")
public class CartaoController {
  private final CartaoService cartaoService;

  public CartaoController(final CartaoService cartaoService) {
    this.cartaoService = cartaoService;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public CartaoDTO cadastrarCartao(@Valid @RequestBody final CartaoDTO cartaoRequest) {
    return cartaoService.cadastrar(cartaoRequest);
  }
}
