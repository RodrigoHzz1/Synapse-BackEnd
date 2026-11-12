package com.example.SynapseBackEnd.repository;

import com.example.SynapseBackEnd.model.CategoriaLabMaker;
import com.example.SynapseBackEnd.model.LabMakerModel;
import com.example.SynapseBackEnd.model.StatusLabMaker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LabMakerRepository extends JpaRepository<LabMakerModel, Long> {
    boolean existsByNome(String nome);
    List<LabMakerModel> findByCategoria(CategoriaLabMaker categoria);
    List<LabMakerModel> findByStatus(StatusLabMaker status);
}
