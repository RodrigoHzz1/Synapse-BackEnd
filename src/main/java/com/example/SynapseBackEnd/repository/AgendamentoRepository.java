package com.example.SynapseBackEnd.repository;

import com.example.SynapseBackEnd.model.AgendamentoModel;
import com.example.SynapseBackEnd.model.StatusAgendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AgendamentoRepository extends JpaRepository<AgendamentoModel, Long> {

    // Verifica conflito de horário para SALA
    @Query("""
        SELECT COUNT(a) > 0 FROM AgendamentoModel a
        WHERE a.sala.id = :salaId
          AND a.dataReserva = :data
          AND a.status IN ('PENDENTE', 'APROVADO')
          AND (:horaInicio < a.horaFim AND :horaFim > a.horaInicio)
    """)
    boolean existeConflitoSala(
            @Param("salaId") Long salaId,
            @Param("data") LocalDate data,
            @Param("horaInicio") LocalTime horaInicio,
            @Param("horaFim") LocalTime horaFim
    );

    // Verifica conflito de horário para LAB MAKER
    @Query("""
        SELECT COUNT(a) > 0 FROM AgendamentoModel a
        WHERE a.labMaker.id = :labMakerId
          AND a.dataReserva = :data
          AND a.status IN ('PENDENTE', 'APROVADO')
          AND (:horaInicio < a.horaFim AND :horaFim > a.horaInicio)
    """)
    boolean existeConflitoLabMaker(
            @Param("labMakerId") Long labMakerId,
            @Param("data") LocalDate data,
            @Param("horaInicio") LocalTime horaInicio,
            @Param("horaFim") LocalTime horaFim
    );

    // Lista reservas do próprio usuário (Tela "Minhas reservas")
    List<AgendamentoModel> findByUsuarioIdOrderByDataReservaDescHoraInicioAsc(Long usuarioId);

    // Lista todas por status (ex: Coordenador listar apenas PENDENTE)
    List<AgendamentoModel> findByStatusOrderByDataReservaAscHoraInicioAsc(StatusAgendamento status);

    // Lista reservas ativas de uma sala numa data (para o front desabilitar horários ocupados no calendário)
    @Query("""
        SELECT a FROM AgendamentoModel a
        WHERE a.sala.id = :salaId
          AND a.dataReserva = :data
          AND a.status IN ('PENDENTE', 'APROVADO')
        ORDER BY a.horaInicio ASC
    """)
    List<AgendamentoModel> findOcupadosSalaPorData(@Param("salaId") Long salaId, @Param("data") LocalDate data);

    // Lista reservas ativas de um item LabMaker numa data
    @Query("""
        SELECT a FROM AgendamentoModel a
        WHERE a.labMaker.id = :labMakerId
          AND a.dataReserva = :data
          AND a.status IN ('PENDENTE', 'APROVADO')
        ORDER BY a.horaInicio ASC
    """)
    List<AgendamentoModel> findOcupadosLabMakerPorData(@Param("labMakerId") Long labMakerId, @Param("data") LocalDate data);
}