package com.vr.miniautorizador.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.vr.miniautorizador.configurations.CustomBasicAuthenticationEntryPoint;
import com.vr.miniautorizador.configurations.CustomSecurityConfig;
import com.vr.miniautorizador.dtos.TransacaoDTO;
import com.vr.miniautorizador.services.TransacaoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TransacaoController.class)
@Import({CustomSecurityConfig.class, CustomBasicAuthenticationEntryPoint.class})
class TransacaoControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private TransacaoService transacaoService;

  @Test
  void realizaTransacao_deveRetornar201_quandoAutenticado() throws Exception {
    mockMvc
        .perform(
            post("/transacoes")
                .with(httpBasic("username", "password"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                      "numeroCartao": "1234567890123456",
                      "senhaCartao": "1234",
                      "valor": 25.50
                    }
                    """))
        .andExpect(status().isCreated())
        .andExpect(content().string("OK"));

    verify(transacaoService).realizaTransacao(any(TransacaoDTO.class));
  }

  @Test
  void realizaTransacao_deveRetornar401_quandoNaoAutenticado() throws Exception {
    mockMvc
        .perform(
            post("/transacoes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                      "numeroCartao": "1234567890123456",
                      "senhaCartao": "1234",
                      "valor": 25.50
                    }
                    """))
        .andExpect(status().isUnauthorized());

    verify(transacaoService, never()).realizaTransacao(any(TransacaoDTO.class));
  }

  @Test
  void realizaTransacao_deveRetornar400_quandoRequestForInvalido() throws Exception {
    mockMvc
        .perform(
            post("/transacoes")
                .with(httpBasic("username", "password"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                      "numeroCartao": "",
                      "senhaCartao": " ",
                      "valor": 0
                    }
                    """))
        .andExpect(status().isBadRequest());

    verify(transacaoService, never()).realizaTransacao(any(TransacaoDTO.class));
  }
}
