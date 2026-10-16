package com.vr.miniautorizador.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CartaoDTO(
    @NotBlank @Pattern(regexp = "\\d{16}") String numeroCartao,
    @NotBlank @Pattern(regexp = "\\d{4}") String senha) {}
