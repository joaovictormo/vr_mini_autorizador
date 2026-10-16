package com.vr.miniautorizador.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.vr.miniautorizador.configurations.CustomBasicAuthenticationEntryPoint;
import com.vr.miniautorizador.configurations.CustomSecurityConfig;
import com.vr.miniautorizador.dtos.CartaoDTO;
import com.vr.miniautorizador.exceptions.CartaoNaoEncontradoException;
import com.vr.miniautorizador.services.CartaoService;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(CartaoController.class)
@Import({CustomSecurityConfig.class, CustomBasicAuthenticationEntryPoint.class})
class CartaoControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private CartaoService cartaoService;

  private static final String NUMERO_CARTAO = "1234567890123456";
  private static final String SENHA_CARTAO = "1234";

  @Test
  void cadastrarCartao_deveRetornar201_quandoAutenticado() throws Exception {
    final var response = new CartaoDTO(NUMERO_CARTAO, SENHA_CARTAO);
    when(cartaoService.cadastrar(any(CartaoDTO.class))).thenReturn(response);

    mockMvc
        .perform(
            post("/cartoes")
                .with(httpBasic("username", "password"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                            {
                              "numeroCartao": "1234567890123456",
                              "senha": "1234"
                            }
                            """))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.numeroCartao").value(NUMERO_CARTAO))
        .andExpect(jsonPath("$.senha").value(SENHA_CARTAO));

    verify(cartaoService).cadastrar(any(CartaoDTO.class));
  }

  @Test
  void cadastrarCartao_deveRetornar401_quandoNaoAutenticado() throws Exception {
    mockMvc
        .perform(
            post("/cartoes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                            {
                              "numeroCartao": "1234567890123456",
                              "senha": "1234"
                            }
                            """))
        .andExpect(status().isUnauthorized());

    verify(cartaoService, never()).cadastrar(any(CartaoDTO.class));
  }

  @Test
  void cadastrarCartao_deveRetornar400_quandoDadosForemInvalidos() throws Exception {
    mockMvc
        .perform(
            post("/cartoes")
                .with(httpBasic("username", "password"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                                {
                                  "numeroCartao": "123",
                                  "senha": "12"
                                }
                                """))
        .andExpect(status().isBadRequest());

    verify(cartaoService, never()).cadastrar(any(CartaoDTO.class));
  }

  @Test
  void getSaldoCartao_deveRetornarSaldo_quandoAutenticado() throws Exception {
    when(cartaoService.getSaldoCartao(NUMERO_CARTAO)).thenReturn(new BigDecimal("125.50"));

    mockMvc
        .perform(
            get("/cartoes/{numeroCartao}", NUMERO_CARTAO).with(httpBasic("username", "password")))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(content().string("125.50"));

    verify(cartaoService).getSaldoCartao(NUMERO_CARTAO);
  }

  @Test
  void getSaldoCartao_deveRetornar401_quandoNaoAutenticado() throws Exception {
    mockMvc
        .perform(
            get("/cartoes/{numeroCartao}", NUMERO_CARTAO).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized());

    verify(cartaoService, never()).cadastrar(any(CartaoDTO.class));
  }

  @Test
  void getSaldoCartao_deveRetornar404ComBodyVazio_quandoCartaoNaoExiste() throws Exception {
    when(cartaoService.getSaldoCartao(NUMERO_CARTAO)).thenThrow(new CartaoNaoEncontradoException());

    mockMvc
        .perform(
            get("/cartoes/{numeroCartao}", NUMERO_CARTAO).with(httpBasic("username", "password")))
        .andExpect(status().isNotFound())
        .andExpect(content().string(""));

    verify(cartaoService).getSaldoCartao(NUMERO_CARTAO);
  }
}
