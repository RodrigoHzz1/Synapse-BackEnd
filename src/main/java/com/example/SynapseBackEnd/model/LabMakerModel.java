package com.example.SynapseBackEnd.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "tab_labMaker")
public class LabMakerModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome do recurso é obrigatório")
    @Column(nullable = false)
    private String nome;

    @NotNull(message = "A categoria é obrigatória")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoriaLabMaker categoria;

    @NotBlank(message = "Descreva o produto")
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusLabMaker status = StatusLabMaker.DISPONIVEL;

    public LabMakerModel() {}

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

    public CategoriaLabMaker getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaLabMaker categoria) {
        this.categoria = categoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public StatusLabMaker getStatus() {
        return status;
    }

    public void setStatus(StatusLabMaker status) {
        this.status = status;
    }
}