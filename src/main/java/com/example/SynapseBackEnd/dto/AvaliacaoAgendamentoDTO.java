package com.example.SynapseBackEnd.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AvaliacaoAgendamentoDTO {
    @NotNull(message = "Informe se o agendamento foi aprovado ou rejeitado")
    private Boolean aprovado;
    private String motivoRejeicao;
}