package com.example.SynapseBackEnd.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "tab_agendamentos")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class AgendamentoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private UsuarioModel usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sala_id")
    private SalaModel sala;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lab_maker_id")
    private LabMakerModel labMaker;

    // Quando seu colega criar Equipamento, basta descomentar:
    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "equipamento_id")
    // private EquipamentoModel equipamento;

    @Column(nullable = false)
    private LocalDate dataReserva;

    @Column(nullable = false)
    private LocalTime horaInicio;

    @Column(nullable = false)
    private LocalTime horaFim;

    @Column(nullable = false, length = 500)
    private String motivo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusAgendamento status;

    @Column(length = 300)
    private String motivoRejeicao;

    @Column(nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @PrePersist
    public void prePersist() {
        this.criadoEm = LocalDateTime.now();
        if (this.status == null) {
            this.status = StatusAgendamento.PENDENTE;
        }
    }
}