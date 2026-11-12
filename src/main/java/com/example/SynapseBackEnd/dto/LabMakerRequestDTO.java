package com.example.SynapseBackEnd.dto;

import com.example.SynapseBackEnd.model.CategoriaLabMaker;
import com.example.SynapseBackEnd.model.StatusLabMaker;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class LabMakerRequestDTO {

    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    @NotNull(message = "A categoria é obrigatória")
    private CategoriaLabMaker categoria;

    private String descricao;

    private StatusLabMaker status;

    public LabMakerRequestDTO() {}

    public LabMakerRequestDTO(String nome, CategoriaLabMaker categoria, String descricao, StatusLabMaker status) {
        this.nome = nome;
        this.categoria = categoria;
        this.descricao = descricao;
        this.status = status;
    }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public CategoriaLabMaker getCategoria() { return categoria; }
    public void setCategoria(CategoriaLabMaker categoria) { this.categoria = categoria; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public StatusLabMaker getStatus() { return status; }
    public void setStatus(StatusLabMaker status) { this.status = status; }
}