package com.example.SynapseBackEnd.controller;

import com.example.SynapseBackEnd.dto.EquipamentoRequestDTO;
import com.example.SynapseBackEnd.dto.EquipamentoResponseDTO;
import com.example.SynapseBackEnd.model.StatusEquipamento;
import com.example.SynapseBackEnd.service.EquipamentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipamentos")
@RequiredArgsConstructor
public class EquipamentoController {

    private final EquipamentoService equipamentoService;

    @PostMapping
    public ResponseEntity<EquipamentoResponseDTO> cadastrar(@Valid @RequestBody EquipamentoRequestDTO dto) {
        EquipamentoResponseDTO novoEquipamento = equipamentoService.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoEquipamento);
    }

    @GetMapping
    public ResponseEntity<List<EquipamentoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(equipamentoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EquipamentoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(equipamentoService.buscarPorId(id));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<EquipamentoResponseDTO>> listarPorStatus(@PathVariable StatusEquipamento status) {
        return ResponseEntity.ok(equipamentoService.listarPorStatus(status));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EquipamentoResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody EquipamentoRequestDTO dto) {
        return ResponseEntity.ok(equipamentoService.atualizar(id, dto));
    }

    @PatchMapping("/{id}/inativar")
    public ResponseEntity<EquipamentoResponseDTO> inativar(@PathVariable Long id) {
        return ResponseEntity.ok(equipamentoService.inativar(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        equipamentoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}