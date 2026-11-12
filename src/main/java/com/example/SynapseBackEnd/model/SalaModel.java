package com.example.SynapseBackEnd.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "tab_salas")

public class SalaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome da sala é obrigatório")
    @Column(nullable = false, unique = true)
    private String nome;

    @NotBlank(message = "Informe o tipo da sala")
    @Column(nullable = false)
    private String tipo;

    @NotNull(message = "Informe a capacidade da sala")
    @Positive(message = "A capacidade deve ser maiorque zero")
    private Integer capacidade;

    @NotBlank(message = "Descreva a sala, Localização, capacidade...")
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusSala status = StatusSala.DISPONIVEL;

    public SalaModel() {}

    public SalaModel(Long id, String nome, String tipo, Integer capacidade, String descricao, StatusSala status) {
        this.id = id;
        this.nome = nome;
        this.tipo = tipo;
        this.capacidade = capacidade;
        this.descricao = descricao;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Integer getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(Integer capacidade) {
        this.capacidade = capacidade;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public StatusSala getStatus() {
        return status;
    }

    public void setStatus(StatusSala status) {
        this.status = status;
    }
}
