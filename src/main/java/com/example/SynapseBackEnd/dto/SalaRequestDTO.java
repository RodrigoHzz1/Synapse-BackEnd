package com.example.SynapseBackEnd.dto;

import com.example.SynapseBackEnd.model.StatusSala;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class SalaRequestDTO {

    @NotBlank(message = "O nome da sala é obrigatório")
    private String nome;

    @NotBlank(message = "O tipo da sala é obrigatório")
    private String tipo;

    @NotNull(message = "A capacidade é obrigatória")
    @Positive(message = "A capacidade deve ser maior que zero")
    private Integer capacidade;

    private String descricao;

    private StatusSala status;

    public SalaRequestDTO() {}

    public SalaRequestDTO(String nome, String tipo, Integer capacidade, String descricao, StatusSala status) {
        this.nome = nome;
        this.tipo = tipo;
        this.capacidade = capacidade;
        this.descricao = descricao;
        this.status = status;
    }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public Integer getCapacidade() { return capacidade; }
    public void setCapacidade(Integer capacidade) { this.capacidade = capacidade; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public StatusSala getStatus() { return status; }
    public void setStatus(StatusSala status) { this.status = status; }
}