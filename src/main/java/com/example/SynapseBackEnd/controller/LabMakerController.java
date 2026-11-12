package com.example.SynapseBackEnd.controller;

import com.example.SynapseBackEnd.dto.LabMakerRequestDTO;
import com.example.SynapseBackEnd.dto.LabMakerResponseDTO;
import com.example.SynapseBackEnd.model.CategoriaLabMaker;
import com.example.SynapseBackEnd.model.StatusLabMaker;
import com.example.SynapseBackEnd.service.LabMakerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/labmaker")
public class LabMakerController {

    @Autowired
    private LabMakerService labMakerService;

    @PostMapping
    public ResponseEntity<LabMakerResponseDTO> cadastrar(@Valid @RequestBody LabMakerRequestDTO dto) {
        LabMakerResponseDTO novoRecurso = labMakerService.cadastrarRecurso(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoRecurso);
    }

    @GetMapping
    public ResponseEntity<List<LabMakerResponseDTO>> listarTodos() {
        return ResponseEntity.ok(labMakerService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LabMakerResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(labMakerService.buscarPorId(id));
    }

    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<LabMakerResponseDTO>> listarPorCategoria(@PathVariable CategoriaLabMaker categoria) {
        return ResponseEntity.ok(labMakerService.listarPorCategoria(categoria));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<LabMakerResponseDTO>> listarPorStatus(@PathVariable StatusLabMaker status) {
        return ResponseEntity.ok(labMakerService.listarPorStatus(status));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LabMakerResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody LabMakerRequestDTO dto) {
        return ResponseEntity.ok(labMakerService.atualizarRecurso(id, dto));
    }

    @PatchMapping("/{id}/inativar")
    public ResponseEntity<LabMakerResponseDTO> inativar(@PathVariable Long id) {
        return ResponseEntity.ok(labMakerService.inativarRecurso(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        labMakerService.deletarRecurso(id);
        return ResponseEntity.noContent().build();
    }
}