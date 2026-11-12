package com.example.SynapseBackEnd.service;

import com.example.SynapseBackEnd.dto.SalaRequestDTO;
import com.example.SynapseBackEnd.dto.SalaResponseDTO;
import com.example.SynapseBackEnd.model.SalaModel;
import com.example.SynapseBackEnd.model.StatusSala;
import com.example.SynapseBackEnd.repository.SalaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SalaService {

    @Autowired
    private SalaRepository salaRepository;

    public SalaResponseDTO cadastrarSala(SalaRequestDTO dto) {
        if (salaRepository.existsByNome(dto.getNome())) {
            throw new IllegalArgumentException("Já existe uma sala cadastrada com o nome: " + dto.getNome());
        }

        SalaModel sala = new SalaModel();
        sala.setNome(dto.getNome());
        sala.setTipo(dto.getTipo());
        sala.setCapacidade(dto.getCapacidade());
        sala.setDescricao(dto.getDescricao());
        sala.setStatus(dto.getStatus() != null ? dto.getStatus() : StatusSala.DISPONIVEL);

        SalaModel salaSalva = salaRepository.save(sala);
        return new SalaResponseDTO(salaSalva);
    }

    public List<SalaResponseDTO> listarTodas() {
        return salaRepository.findAll()
                .stream()
                .map(SalaResponseDTO::new)
                .collect(Collectors.toList());
    }

    public SalaResponseDTO buscarPorId(Long id) {
        SalaModel sala = salaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sala não encontrada com o ID: " + id));
        return new SalaResponseDTO(sala);
    }

    public List<SalaResponseDTO> listarPorStatus(StatusSala status) {
        return salaRepository.findByStatus(status)
                .stream()
                .map(SalaResponseDTO::new)
                .collect(Collectors.toList());
    }

    public List<SalaResponseDTO> listarPorTipo(String tipo) {
        return salaRepository.findByTipoIgnoreCase(tipo)
                .stream()
                .map(SalaResponseDTO::new)
                .collect(Collectors.toList());
    }

    public SalaResponseDTO atualizarSala(Long id, SalaRequestDTO dto) {
        SalaModel salaExistente = salaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sala não encontrada com o ID: " + id));

        if (!salaExistente.getNome().equalsIgnoreCase(dto.getNome())
                && salaRepository.existsByNome(dto.getNome())) {
            throw new IllegalArgumentException("Já existe uma sala cadastrada com o nome: " + dto.getNome());
        }

        salaExistente.setNome(dto.getNome());
        salaExistente.setTipo(dto.getTipo());
        salaExistente.setCapacidade(dto.getCapacidade());
        salaExistente.setDescricao(dto.getDescricao());

        if (dto.getStatus() != null) {
            salaExistente.setStatus(dto.getStatus());
        }

        SalaModel salaAtualizada = salaRepository.save(salaExistente);
        return new SalaResponseDTO(salaAtualizada);
    }

    public void deletarSala(Long id) {
        SalaModel sala = salaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sala não encontrada com o ID: " + id));
        salaRepository.delete(sala);
    }

    public SalaResponseDTO inativarSala(Long id) {
        SalaModel sala = salaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sala não encontrada com o ID: " + id));
        sala.setStatus(StatusSala.INATIVO);
        return new SalaResponseDTO(salaRepository.save(sala));
    }
}