package com.vr.miniautorizador.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.vr.miniautorizador.models.Cartao;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.mysql.MySQLContainer;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class CartaoRepositoryTest {

  @Container static final MySQLContainer MYSQL = new MySQLContainer("mysql:5.7");

  @DynamicPropertySource
  static void configureDatasource(final DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", MYSQL::getJdbcUrl);
    registry.add("spring.datasource.username", MYSQL::getUsername);
    registry.add("spring.datasource.password", MYSQL::getPassword);
    registry.add("spring.datasource.driver-class-name", MYSQL::getDriverClassName);
  }

  @Autowired private CartaoRepository cartaoRepository;

  @Autowired private TestEntityManager entityManager;

  private static final String NUMERO_CARTAO = "1234567890123456";
  private static final String SENHA_CARTAO = "1234";

  @BeforeEach
  void setUp() {
    cartaoRepository.save(new Cartao(NUMERO_CARTAO, SENHA_CARTAO));
    entityManager.flush();
  }

  @Test
  void debitarSeSaldoSuficiente_deveAtualizarSaldo_quandoHaSaldo() {
    final var rows =
        cartaoRepository.debitarSeSaldoSuficiente(NUMERO_CARTAO, new BigDecimal("30.00"));

    assertEquals(1, rows);

    final var cartao = cartaoRepository.findByNumeroCartao(NUMERO_CARTAO).orElseThrow();

    assertEquals(0, new BigDecimal("470.00").compareTo(cartao.getSaldo()));
  }

  @Test
  void debitarSeSaldoSuficiente_naoDeveAtualizar_quandoSaldoInsuficiente() {
    final var rows =
        cartaoRepository.debitarSeSaldoSuficiente(NUMERO_CARTAO, new BigDecimal("500.01"));

    assertEquals(0, rows);

    final var cartao = cartaoRepository.findByNumeroCartao(NUMERO_CARTAO).orElseThrow();

    assertEquals(0, new BigDecimal("500.00").compareTo(cartao.getSaldo()));
  }

  @Test
  void debitarSeSaldoSuficiente_deveRetornarZero_quandoCartaoNaoExiste() {
    final var rows =
        cartaoRepository.debitarSeSaldoSuficiente("0000000000000000", new BigDecimal("10.00"));

    assertEquals(0, rows);
  }
}
