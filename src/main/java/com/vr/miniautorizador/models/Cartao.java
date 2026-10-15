package com.vr.miniautorizador.models;

import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "cartoes")
public class Cartao {
  @Id private UUID id;

  @Indexed(unique = true)
  private String numeroCartao;

  private String senha;
  private double saldo;

  public Cartao(final String numeroCartao, final String senha) {
    this.numeroCartao = numeroCartao;
    this.senha = senha;
    this.saldo = 500;
  }

  public Cartao() {}

  public UUID getId() {
    return id;
  }

  public void setId(final UUID id) {
    this.id = id;
  }

  public String getNumeroCartao() {
    return numeroCartao;
  }

  public void setNumeroCartao(final String numeroCartao) {
    this.numeroCartao = numeroCartao;
  }

  public String getSenha() {
    return senha;
  }

  public void setSenha(final String senha) {
    this.senha = senha;
  }

  public double getSaldo() {
    return saldo;
  }

  public void setSaldo(final double saldo) {
    this.saldo = saldo;
  }
}
