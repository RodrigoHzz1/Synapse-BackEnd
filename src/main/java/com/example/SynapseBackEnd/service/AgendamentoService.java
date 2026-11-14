package com.example.SynapseBackEnd.service;

import com.example.SynapseBackEnd.dto.AgendamentoRequestDTO;
import com.example.SynapseBackEnd.dto.AgendamentoResponseDTO;
import com.example.SynapseBackEnd.dto.AvaliacaoAgendamentoDTO;
import com.example.SynapseBackEnd.model.*;
import com.example.SynapseBackEnd.repository.AgendamentoRepository;
import com.example.SynapseBackEnd.repository.LabMakerRepository;
import com.example.SynapseBackEnd.repository.SalaRepository;
import com.example.SynapseBackEnd.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final UsuarioRepository usuarioRepository;
    private final SalaRepository salaRepository;
    private final LabMakerRepository labMakerRepository;

    @Transactional
    public AgendamentoResponseDTO criar(AgendamentoRequestDTO dto, String emailUsuario) {
        UsuarioModel usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        // 1. Valida horários
        if (!dto.getHoraFim().isAfter(dto.getHoraInicio())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A hora de término deve ser posterior à hora de início.");
        }

        // 2. Valida escolha de exatamente um recurso
        boolean temSala = dto.getSalaId() != null;
        boolean temLabMaker = dto.getLabMakerId() != null;

        if (temSala == temLabMaker) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe apenas uma Sala OU um recurso do LabMaker por agendamento.");
        }

        AgendamentoModel agendamento = AgendamentoModel.builder()
                .usuario(usuario)
                .dataReserva(dto.getDataReserva())
                .horaInicio(dto.getHoraInicio())
                .horaFim(dto.getHoraFim())
                .motivo(dto.getIntuito())
                .status(StatusAgendamento.PENDENTE)
                .build();

        if (temSala) {
            // Regra RBAC: Aluno não pode reservar sala
            if (usuario.getPerfil() == PerfilUsuario.ALUNO) {
                throw new AccessDeniedException("Alunos não têm permissão para agendar salas.");
            }

            SalaModel sala = salaRepository.findById(dto.getSalaId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sala não encontrada"));

            if (sala.getStatus() != StatusSala.DISPONIVEL) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Esta sala não está disponível no momento.");
            }

            // Bloqueio de duplicidade de dia/horário
            if (agendamentoRepository.existeConflitoSala(sala.getId(), dto.getDataReserva(), dto.getHoraInicio(), dto.getHoraFim())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe uma reserva pendente ou aprovada para esta sala neste dia e horário.");
            }

            agendamento.setSala(sala);
        } else {
            LabMakerModel labMaker = labMakerRepository.findById(dto.getLabMakerId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Recurso LabMaker não encontrado"));

            // Bloqueio de duplicidade de dia/horário
            if (agendamentoRepository.existeConflitoLabMaker(labMaker.getId(), dto.getDataReserva(), dto.getHoraInicio(), dto.getHoraFim())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe uma reserva pendente ou aprovada para este recurso neste dia e horário.");
            }

            agendamento.setLabMaker(labMaker);
        }

        return AgendamentoResponseDTO.fromEntity(agendamentoRepository.save(agendamento));
    }

    @Transactional
    public AgendamentoResponseDTO avaliar(Long id, AvaliacaoAgendamentoDTO dto) {
        AgendamentoModel agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Agendamento não encontrado"));

        if (agendamento.getStatus() != StatusAgendamento.PENDENTE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Apenas agendamentos PENDENTES podem ser avaliados.");
        }

        if (Boolean.TRUE.equals(dto.getAprovado())) {
            agendamento.setStatus(StatusAgendamento.APROVADO);
            agendamento.setMotivoRejeicao(null);
        } else {
            agendamento.setStatus(StatusAgendamento.REJEITADO);
            agendamento.setMotivoRejeicao(dto.getMotivoRejeicao());
        }

        return AgendamentoResponseDTO.fromEntity(agendamentoRepository.save(agendamento));
    }

    @Transactional
    public AgendamentoResponseDTO cancelar(Long id, String emailUsuario) {
        AgendamentoModel agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Agendamento não encontrado"));

        UsuarioModel usuarioLogado = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        boolean isDono = agendamento.getUsuario().getId().equals(usuarioLogado.getId());
        boolean isCoordenador = usuarioLogado.getPerfil() == PerfilUsuario.COORDENADOR;

        if (!isDono && !isCoordenador) {
            throw new AccessDeniedException("Você não tem permissão para cancelar este agendamento.");
        }

        if (agendamento.getStatus() == StatusAgendamento.REJEITADO ||
                agendamento.getStatus() == StatusAgendamento.CANCELADO) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Não é possível cancelar um agendamento com status " + agendamento.getStatus() + "."
            );
        }

        agendamento.setStatus(StatusAgendamento.CANCELADO);
        return AgendamentoResponseDTO.fromEntity(agendamentoRepository.save(agendamento));
    }

    public List<AgendamentoResponseDTO> listarMeusAgendamentos(String emailUsuario) {
        UsuarioModel usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        return agendamentoRepository.findByUsuarioIdOrderByDataReservaDescHoraInicioAsc(usuario.getId())
                .stream()
                .map(AgendamentoResponseDTO::fromEntity)
                .toList();
    }

    public List<AgendamentoResponseDTO> listarTodosOuPorStatus(StatusAgendamento status) {
        List<AgendamentoModel> lista = (status != null)
                ? agendamentoRepository.findByStatusOrderByDataReservaAscHoraInicioAsc(status)
                : agendamentoRepository.findAll();

        return lista.stream().map(AgendamentoResponseDTO::fromEntity).toList();
    }

    public List<AgendamentoResponseDTO> listarHorariosOcupadosSala(Long salaId, LocalDate data) {
        return agendamentoRepository.findOcupadosSalaPorData(salaId, data)
                .stream()
                .map(AgendamentoResponseDTO::fromEntity)
                .toList();
    }

    public List<AgendamentoResponseDTO> listarHorariosOcupadosLabMaker(Long labMakerId, LocalDate data) {
        return agendamentoRepository.findOcupadosLabMakerPorData(labMakerId, data)
                .stream()
                .map(AgendamentoResponseDTO::fromEntity)
                .toList();
    }
}