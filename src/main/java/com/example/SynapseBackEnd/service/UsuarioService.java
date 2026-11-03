package com.example.SynapseBackEnd.service;

import com.example.SynapseBackEnd.dto.UsuarioRequestDTO;
import com.example.SynapseBackEnd.model.PerfilUsuario;
import com.example.SynapseBackEnd.model.UsuarioModel;
import com.example.SynapseBackEnd.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 1. Cadastrar usuário (Força o perfil inicial como ALUNO)
    public UsuarioModel cadastrarUsuario(UsuarioRequestDTO dto) {
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Este e-mail já está cadastrado");
        }

        UsuarioModel novoUsuario = new UsuarioModel();
        novoUsuario.setNome(dto.getNome());
        novoUsuario.setEmail(dto.getEmail());
        novoUsuario.setSenha(passwordEncoder.encode(dto.getSenha()));

        // Regra de negócio: Todos começam como ALUNO
        novoUsuario.setPerfil(PerfilUsuario.ALUNO);

        return usuarioRepository.save(novoUsuario);
    }

    // 2. Alterar o perfil do usuário (Usado pelo Coordenador)
    public UsuarioModel alterarPerfil(Long id, PerfilUsuario novoPerfil, String emailExecutor) {
        UsuarioModel usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com o id: " + id));

        //Impede que o próprio coordenador remova o seu acesso de coordenador
        if (usuario.getEmail().equals(emailExecutor) && novoPerfil != PerfilUsuario.COORDENADOR){
            throw new IllegalArgumentException("Você não pode remover o seu próprio perfil de Coordenador");
        }

        usuario.setPerfil(novoPerfil);
        return usuarioRepository.save(usuario);
    }

    // 3. Listar todos os usuários
    public List<UsuarioModel> listarTodos() {
        return usuarioRepository.findAll();
    }

    // 4. Buscar por e-mail
    public Optional<UsuarioModel> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }
}