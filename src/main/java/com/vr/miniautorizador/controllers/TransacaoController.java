package com.vr.miniautorizador.controllers;

import com.vr.miniautorizador.dtos.TransacaoDTO;
import com.vr.miniautorizador.services.TransacaoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transacoes")
public class TransacaoController {

  private final TransacaoService transacaoService;

  public TransacaoController(final TransacaoService transacaoService) {
    this.transacaoService = transacaoService;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public String realizaTransacao(@Valid @RequestBody final TransacaoDTO request) {
    this.transacaoService.realizaTransacao(request);
    return "OK";
  }
}
