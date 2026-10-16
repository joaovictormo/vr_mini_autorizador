package com.vr.miniautorizador.repositories;

import com.vr.miniautorizador.models.Cartao;
import java.math.BigDecimal;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CartaoRepository extends JpaRepository<Cartao, Long> {
  Optional<Cartao> findByNumeroCartao(String numeroCartao);

  @Modifying(flushAutomatically = true, clearAutomatically = true)
  @Query(
      value =
          """
            UPDATE cartoes
            SET saldo = saldo - :valor
            WHERE numero_cartao = :numeroCartao
              AND saldo >= :valor
            """,
      nativeQuery = true)
  int debitarSeSaldoSuficiente(
      @Param("numeroCartao") String numeroCartao, @Param("valor") BigDecimal valor);
}
