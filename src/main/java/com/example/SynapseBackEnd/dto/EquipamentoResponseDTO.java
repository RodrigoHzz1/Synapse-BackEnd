package com.example.SynapseBackEnd.dto;

import com.example.SynapseBackEnd.model.EquipamentoModel;
import com.example.SynapseBackEnd.model.StatusEquipamento;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipamentoResponseDTO {

    private Long id;
    private String nome;
    private String descricao;
    private StatusEquipamento status;

    public EquipamentoResponseDTO(EquipamentoModel equipamento) {
        this.id = equipamento.getId();
        this.nome = equipamento.getNome();
        this.descricao = equipamento.getDescricao();
        this.status = equipamento.getStatus();
    }
}