package com.vr.miniautorizador.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record TransacaoDTO(
    @NotBlank String numeroCartao,
    @NotBlank String senhaCartao,
    @NotNull @Positive BigDecimal valor) {}
