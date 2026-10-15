package com.vr.miniautorizador.controllers;

import com.vr.miniautorizador.services.CartaoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cartoes")
public class CartaoController {
  private final CartaoService cartaoService;

  public CartaoController(final CartaoService cartaoService) {
    this.cartaoService = cartaoService;
  }
}
