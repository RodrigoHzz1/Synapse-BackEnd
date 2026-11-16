package com.example.SynapseBackEnd.service;

import com.example.SynapseBackEnd.dto.EquipamentoRequestDTO;
import com.example.SynapseBackEnd.dto.EquipamentoResponseDTO;
import com.example.SynapseBackEnd.model.EquipamentoModel;
import com.example.SynapseBackEnd.model.StatusEquipamento;
import com.example.SynapseBackEnd.repository.EquipamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EquipamentoService {

    private final EquipamentoRepository equipamentoRepository;

    public EquipamentoResponseDTO cadastrar(EquipamentoRequestDTO dto) {
        if (equipamentoRepository.existsByNome(dto.getNome())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um equipamento cadastrado com o nome: " + dto.getNome());
        }

        EquipamentoModel equipamento = new EquipamentoModel();
        equipamento.setNome(dto.getNome());
        equipamento.setDescricao(dto.getDescricao());
        equipamento.setStatus(dto.getStatus() != null ? dto.getStatus() : StatusEquipamento.DISPONIVEL);

        EquipamentoModel salvo = equipamentoRepository.save(equipamento);
        return new EquipamentoResponseDTO(salvo);
    }

    public List<EquipamentoResponseDTO> listarTodos() {
        return equipamentoRepository.findAll()
                .stream()
                .map(EquipamentoResponseDTO::new)
                .toList();
    }

    public EquipamentoResponseDTO buscarPorId(Long id) {
        EquipamentoModel equipamento = equipamentoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Equipamento não encontrado com o ID: " + id));
        return new EquipamentoResponseDTO(equipamento);
    }

    public List<EquipamentoResponseDTO> listarPorStatus(StatusEquipamento status) {
        return equipamentoRepository.findByStatus(status)
                .stream()
                .map(EquipamentoResponseDTO::new)
                .toList();
    }

    public EquipamentoResponseDTO atualizar(Long id, EquipamentoRequestDTO dto) {
        EquipamentoModel existente = equipamentoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Equipamento não encontrado com o ID: " + id));

        if (!existente.getNome().equalsIgnoreCase(dto.getNome())
                && equipamentoRepository.existsByNome(dto.getNome())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um equipamento cadastrado com o nome: " + dto.getNome());
        }

        existente.setNome(dto.getNome());
        existente.setDescricao(dto.getDescricao());

        if (dto.getStatus() != null) {
            existente.setStatus(dto.getStatus());
        }

        return new EquipamentoResponseDTO(equipamentoRepository.save(existente));
    }

    public EquipamentoResponseDTO inativar(Long id) {
        EquipamentoModel equipamento = equipamentoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Equipamento não encontrado com o ID: " + id));

        equipamento.setStatus(StatusEquipamento.INATIVO);
        return new EquipamentoResponseDTO(equipamentoRepository.save(equipamento));
    }

    public void deletar(Long id) {
        EquipamentoModel equipamento = equipamentoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Equipamento não encontrado com o ID: " + id));
        equipamentoRepository.delete(equipamento);
    }
}