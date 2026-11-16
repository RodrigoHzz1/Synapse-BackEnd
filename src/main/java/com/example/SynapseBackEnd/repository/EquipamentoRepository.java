package com.example.SynapseBackEnd.repository;

import com.example.SynapseBackEnd.model.EquipamentoModel;
import com.example.SynapseBackEnd.model.StatusEquipamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipamentoRepository extends JpaRepository<EquipamentoModel, Long> {

    boolean existsByNome(String nome);

    List<EquipamentoModel> findByStatus(StatusEquipamento status);
}