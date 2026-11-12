package com.example.SynapseBackEnd.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AgendamentoRequestDTO {

    private Long salaId;
    private Long labMakerId;
    // private Long equipamentoId;

    @NotNull(message = "A data da reserva é obrigatória")
    @FutureOrPresent(message = "Não é possível agendar para datas passadas")
    private LocalDate dataReserva;

    @NotNull(message = "A hora de início é obrigatória")
    private LocalTime horaInicio;

    @NotNull(message = "A hora de término é obrigatória")
    private LocalTime horaFim;

    @NotBlank(message = "Descreva o intuito da reserva")
    private String intuito;

    public AgendamentoRequestDTO() {}

    public Long getSalaId() {
        return salaId;
    }

    public void setSalaId(Long salaId) {
        this.salaId = salaId;
    }

    public Long getLabMakerId() {
        return labMakerId;
    }

    public void setLabMakerId(Long labMakerId) {
        this.labMakerId = labMakerId;
    }

    public LocalDate getDataReserva() {
        return dataReserva;
    }

    public void setDataReserva(LocalDate dataReserva) {
        this.dataReserva = dataReserva;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFim() {
        return horaFim;
    }

    public void setHoraFim(LocalTime horaFim) {
        this.horaFim = horaFim;
    }

    public String getIntuito() {
        return intuito;
    }

    public void setIntuito(String intuito) {
        this.intuito = intuito;
    }
}



