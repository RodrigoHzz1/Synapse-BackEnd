package com.example.SynapseBackEnd.controller;

import com.example.SynapseBackEnd.dto.AgendamentoRequestDTO;
import com.example.SynapseBackEnd.dto.AgendamentoResponseDTO;
import com.example.SynapseBackEnd.dto.AvaliacaoAgendamentoDTO;
import com.example.SynapseBackEnd.model.StatusAgendamento;
import com.example.SynapseBackEnd.service.AgendamentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/agendamentos")
@RequiredArgsConstructor
public class AgendamentoController {

    private final AgendamentoService agendamentoService;

    // Criar reserva (Aluno -> LabMaker | Professor/Coordenador -> Sala ou LabMaker)
    @PostMapping
    public ResponseEntity<AgendamentoResponseDTO> criar(
            @RequestBody @Valid AgendamentoRequestDTO dto,
            Authentication authentication) {
        AgendamentoResponseDTO criado = agendamentoService.criar(dto, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }

    // Tela "Minhas reservas"
    @GetMapping("/meus")
    public ResponseEntity<List<AgendamentoResponseDTO>> meusAgendamentos(Authentication authentication) {
        return ResponseEntity.ok(agendamentoService.listarMeusAgendamentos(authentication.getName()));
    }

    // Listar todos ou filtrar por status (?status=PENDENTE) - Coordenador
    @GetMapping
    public ResponseEntity<List<AgendamentoResponseDTO>> listar(
            @RequestParam(required = false) StatusAgendamento status) {
        return ResponseEntity.ok(agendamentoService.listarTodosOuPorStatus(status));
    }

    // Aprovar ou Rejeitar reserva - Apenas Coordenador
    @PatchMapping("/{id}/avaliar")
    public ResponseEntity<AgendamentoResponseDTO> avaliar(
            @PathVariable Long id,
            @RequestBody @Valid AvaliacaoAgendamentoDTO dto) {
        return ResponseEntity.ok(agendamentoService.avaliar(id, dto));
    }

    // Consultar horários já ocupados numa data (para o calendário do Figma)
    @GetMapping("/ocupados/sala/{salaId}")
    public ResponseEntity<List<AgendamentoResponseDTO>> ocupadosSala(
            @PathVariable Long salaId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        return ResponseEntity.ok(agendamentoService.listarHorariosOcupadosSala(salaId, data));
    }

    @GetMapping("/ocupados/labmaker/{labMakerId}")
    public ResponseEntity<List<AgendamentoResponseDTO>> ocupadosLabMaker(
            @PathVariable Long labMakerId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        return ResponseEntity.ok(agendamentoService.listarHorariosOcupadosLabMaker(labMakerId, data));
    }
}