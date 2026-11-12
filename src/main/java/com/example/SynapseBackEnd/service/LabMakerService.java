package com.example.SynapseBackEnd.service;

import com.example.SynapseBackEnd.dto.LabMakerRequestDTO;
import com.example.SynapseBackEnd.dto.LabMakerResponseDTO;
import com.example.SynapseBackEnd.model.CategoriaLabMaker;
import com.example.SynapseBackEnd.model.LabMakerModel;
import com.example.SynapseBackEnd.model.StatusLabMaker;
import com.example.SynapseBackEnd.repository.LabMakerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LabMakerService {

    @Autowired
    private LabMakerRepository labMakerRepository;

    public LabMakerResponseDTO cadastrarRecurso(LabMakerRequestDTO dto) {
        if (labMakerRepository.existsByNome(dto.getNome())) {
            throw new IllegalArgumentException("Já existe um recurso do LabMaker cadastrado com o nome: " + dto.getNome());
        }

        LabMakerModel recurso = new LabMakerModel();
        recurso.setNome(dto.getNome());
        recurso.setCategoria(dto.getCategoria());
        recurso.setDescricao(dto.getDescricao());
        recurso.setStatus(dto.getStatus() != null ? dto.getStatus() : StatusLabMaker.DISPONIVEL);

        LabMakerModel recursoSalvo = labMakerRepository.save(recurso);
        return new LabMakerResponseDTO(recursoSalvo);
    }

    public List<LabMakerResponseDTO> listarTodos() {
        return labMakerRepository.findAll()
                .stream()
                .map(LabMakerResponseDTO::new)
                .collect(Collectors.toList());
    }

    public LabMakerResponseDTO buscarPorId(Long id) {
        LabMakerModel recurso = labMakerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recurso do LabMaker não encontrado com o ID: " + id));
        return new LabMakerResponseDTO(recurso);
    }

    public List<LabMakerResponseDTO> listarPorCategoria(CategoriaLabMaker categoria) {
        return labMakerRepository.findByCategoria(categoria)
                .stream()
                .map(LabMakerResponseDTO::new)
                .collect(Collectors.toList());
    }

    public List<LabMakerResponseDTO> listarPorStatus(StatusLabMaker status) {
        return labMakerRepository.findByStatus(status)
                .stream()
                .map(LabMakerResponseDTO::new)
                .collect(Collectors.toList());
    }

    public LabMakerResponseDTO atualizarRecurso(Long id, LabMakerRequestDTO dto) {
        LabMakerModel recursoExistente = labMakerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recurso do LabMaker não encontrado com o ID: " + id));

        if (!recursoExistente.getNome().equalsIgnoreCase(dto.getNome())
                && labMakerRepository.existsByNome(dto.getNome())) {
            throw new IllegalArgumentException("Já existe um recurso cadastrado com o nome: " + dto.getNome());
        }

        recursoExistente.setNome(dto.getNome());
        recursoExistente.setCategoria(dto.getCategoria());
        recursoExistente.setDescricao(dto.getDescricao());

        if (dto.getStatus() != null) {
            recursoExistente.setStatus(dto.getStatus());
        }

        LabMakerModel atualizado = labMakerRepository.save(recursoExistente);
        return new LabMakerResponseDTO(atualizado);
    }

    //Inativar
    public LabMakerResponseDTO inativarRecurso(Long id) {
        LabMakerModel recurso = labMakerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recurso do LabMaker não encontrado com o ID: " + id));
        recurso.setStatus(StatusLabMaker.INATIVO);
        return new LabMakerResponseDTO(labMakerRepository.save(recurso));
    }

    public void deletarRecurso(Long id) {
        LabMakerModel recurso = labMakerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recurso do LabMaker não encontrado com o ID: " + id));
        labMakerRepository.delete(recurso);
    }
}