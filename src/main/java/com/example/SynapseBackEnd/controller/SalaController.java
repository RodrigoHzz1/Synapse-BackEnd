package com.example.SynapseBackEnd.controller;

import com.example.SynapseBackEnd.dto.SalaRequestDTO;
import com.example.SynapseBackEnd.dto.SalaResponseDTO;
import com.example.SynapseBackEnd.model.StatusSala;
import com.example.SynapseBackEnd.service.SalaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/salas")
public class SalaController {

    @Autowired
    private SalaService salaService;

    @PostMapping
    public ResponseEntity<SalaResponseDTO> cadastrar(@Valid @RequestBody SalaRequestDTO dto) {
        SalaResponseDTO novaSala = salaService.cadastrarSala(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaSala);
    }

    @GetMapping
    public ResponseEntity<List<SalaResponseDTO>> listarTodas() {
        return ResponseEntity.ok(salaService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalaResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(salaService.buscarPorId(id));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<SalaResponseDTO>> listarPorStatus(@PathVariable StatusSala status) {
        return ResponseEntity.ok(salaService.listarPorStatus(status));
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<SalaResponseDTO>> listarPorTipo(@PathVariable String tipo) {
        return ResponseEntity.ok(salaService.listarPorTipo(tipo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SalaResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody SalaRequestDTO dto) {
        return ResponseEntity.ok(salaService.atualizarSala(id, dto));
    }

    @PatchMapping("/{id}/inativar")
    public ResponseEntity<SalaResponseDTO> inativar(@PathVariable Long id) {
        return ResponseEntity.ok(salaService.inativarSala(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        salaService.deletarSala(id);
        return ResponseEntity.noContent().build();
    }
}