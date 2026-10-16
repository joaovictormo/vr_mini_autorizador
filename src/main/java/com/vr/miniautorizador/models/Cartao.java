package com.vr.miniautorizador.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "cartoes")
public class Cartao {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(nullable = false, unique = true)
  private String numeroCartao;

  @Column(nullable = false)
  private String senha;

  @Column(nullable = false)
  private double saldo;

  public Cartao(final String numeroCartao, final String senha) {
    this.numeroCartao = numeroCartao;
    this.senha = senha;
    this.saldo = 500;
  }

  public Cartao() {}

  public Long getId() {
    return id;
  }

  public void setId(final Long id) {
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
