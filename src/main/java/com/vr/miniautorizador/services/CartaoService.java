package com.vr.miniautorizador.services;

import com.vr.miniautorizador.repositories.CartaoRepository;
import org.springframework.stereotype.Service;

@Service
public class CartaoService {
  private final CartaoRepository cartaoRepository;

  public CartaoService(final CartaoRepository cartaoRepository) {
    this.cartaoRepository = cartaoRepository;
  }
}
