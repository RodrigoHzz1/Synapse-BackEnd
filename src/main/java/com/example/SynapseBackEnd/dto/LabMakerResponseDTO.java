package com.example.SynapseBackEnd.dto;

import com.example.SynapseBackEnd.model.CategoriaLabMaker;
import com.example.SynapseBackEnd.model.LabMakerModel;
import com.example.SynapseBackEnd.model.StatusLabMaker;

public class LabMakerResponseDTO {

    private Long id;
    private String nome;
    private CategoriaLabMaker categoria; // Alterado de String para CategoriaLabMaker
    private String descricao;
    private StatusLabMaker status;

    public LabMakerResponseDTO() {}

    public LabMakerResponseDTO(LabMakerModel recurso) {
        this.id = recurso.getId();
        this.nome = recurso.getNome();
        this.categoria = recurso.getCategoria(); // Agora ambos são do tipo CategoriaLabMaker
        this.descricao = recurso.getDescricao();
        this.status = recurso.getStatus();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public CategoriaLabMaker getCategoria() { return categoria; }
    public void setCategoria(CategoriaLabMaker categoria) { this.categoria = categoria; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public StatusLabMaker getStatus() { return status; }
    public void setStatus(StatusLabMaker status) { this.status = status; }
}