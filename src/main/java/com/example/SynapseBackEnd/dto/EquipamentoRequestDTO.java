package com.example.SynapseBackEnd.dto;

import com.example.SynapseBackEnd.model.StatusEquipamento;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipamentoRequestDTO {

    @NotBlank(message = "Informe o nome do equipamento desejado")
    private String nome;

    @NotBlank(message = "Informe a descrição do equipamento desejado")
    private String descricao;

    private StatusEquipamento status;
}