package com.example.SynapseBackEnd.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "tab_equipamentos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EquipamentoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "É necessário informar o nome do aparelho")
    @Column(nullable = false, unique = true)
    private String nome;

    @NotBlank(message = "Informar descrição dos equipamentos desejados")
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusEquipamento status = StatusEquipamento.DISPONIVEL;
}