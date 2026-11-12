package com.example.SynapseBackEnd.dto;

import com.example.SynapseBackEnd.model.SalaModel;
import com.example.SynapseBackEnd.model.StatusSala;

public class SalaResponseDTO {

    private Long id;
    private String nome;
    private String tipo;
    private Integer capacidade;
    private String descricao;
    private StatusSala status;

    public SalaResponseDTO() {}

    public SalaResponseDTO(SalaModel sala) {
        this.id = sala.getId();
        this.nome = sala.getNome();
        this.tipo = sala.getTipo();
        this.capacidade = sala.getCapacidade();
        this.descricao = sala.getDescricao();
        this.status = sala.getStatus();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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