package com.example.SynapseBackEnd.model;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum PerfilUsuario {
    ALUNO,
    PROFESSOR,
    COORDENADOR;

    PerfilUsuario(){}

    @JsonCreator
    public static PerfilUsuario fromString(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        for (PerfilUsuario perfil : PerfilUsuario.values()) {
            if (perfil.name().equalsIgnoreCase(value.trim())) {
                return perfil;
            }
        }
        throw new IllegalArgumentException("Perfil inválido: " + value);
    }

}
