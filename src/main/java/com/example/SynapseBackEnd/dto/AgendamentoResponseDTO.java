package com.example.SynapseBackEnd.dto;

import com.example.SynapseBackEnd.model.AgendamentoModel;
import com.example.SynapseBackEnd.model.StatusAgendamento;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class AgendamentoResponseDTO {
    private Long id;
    private String nomeUsuario;
    private String perfilUsuario;
    private String tipoRecurso; // "SALA" ou "LAB_MAKER"
    private Long recursoId;
    private String nomeRecurso;
    private LocalDate dataReserva;
    private LocalTime horaInicio;
    private LocalTime horaFim;
    private String intuito;
    private StatusAgendamento status;
    private String motivoRejeicao;

    public static AgendamentoResponseDTO fromEntity(AgendamentoModel a) {
        String tipo = a.getSala() != null ? "SALA" : "LAB_MAKER";
        Long recId = a.getSala() != null ? a.getSala().getId() : a.getLabMaker().getId();
        String recNome = a.getSala() != null ? a.getSala().getNome() : a.getLabMaker().getNome();

        return AgendamentoResponseDTO.builder()
                .id(a.getId())
                .nomeUsuario(a.getUsuario().getNome())
                .perfilUsuario(a.getUsuario().getPerfil().name())
                .tipoRecurso(tipo)
                .recursoId(recId)
                .nomeRecurso(recNome)
                .dataReserva(a.getDataReserva())
                .horaInicio(a.getHoraInicio())
                .horaFim(a.getHoraFim())
                .intuito(a.getMotivo())
                .status(a.getStatus())
                .motivoRejeicao(a.getMotivoRejeicao())
                .build();
    }
}