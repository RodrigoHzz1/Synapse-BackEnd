package com.example.SynapseBackEnd.repository;

import com.example.SynapseBackEnd.model.SalaModel;
import com.example.SynapseBackEnd.model.StatusSala;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalaRepository extends JpaRepository<SalaModel, Long> {
    boolean existsByNome(String nome);
    List<SalaModel> findByStatus(StatusSala status);
    List<SalaModel> findByTipoIgnoreCase(String tipo);
}
