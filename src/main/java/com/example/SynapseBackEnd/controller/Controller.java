package com.example.SynapseBackEnd.controller;

import com.example.SynapseBackEnd.config.JwtService;
import com.example.SynapseBackEnd.dto.LoginRequestDTO;
import com.example.SynapseBackEnd.dto.TokenResponseDTO;
import com.example.SynapseBackEnd.dto.UsuarioRequestDTO;
import com.example.SynapseBackEnd.dto.UsuarioResponseDTO;
import com.example.SynapseBackEnd.model.PerfilUsuario;
import com.example.SynapseBackEnd.model.UsuarioModel;
import com.example.SynapseBackEnd.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class Controller {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UsuarioService usuarioService;

    // 1. Endpoint de Cadastro
    @PostMapping
    public ResponseEntity<UsuarioModel> cadastrar(@Valid @RequestBody UsuarioRequestDTO requestDTO) {
        UsuarioModel usuarioSalvo = usuarioService.cadastrarUsuario(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioSalvo);
    }

    // 2. Endpoint de Login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getSenha())
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String tokenJwt = jwtService.generateToken(userDetails);

            return ResponseEntity.ok(new TokenResponseDTO(tokenJwt, "Bearer"));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("E-mail ou senha inválidos.");
        }
    }

    // 3. Listar Todos
    @GetMapping
    public ResponseEntity<List<UsuarioModel>> listarTodos() {
        List<UsuarioModel> usuarios = usuarioService.listarTodos();
        return ResponseEntity.ok(usuarios);
    }

    // 4. Buscar por E-mail
    @GetMapping("/buscar-por-email")
    public ResponseEntity<?> buscarPorEmail(@RequestParam String email) {
        return usuarioService.buscarPorEmail(email)
                .map(usuario -> ResponseEntity.ok().body(usuario))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PatchMapping("/{id}/perfil")
    public ResponseEntity<UsuarioModel> alterarPerfil(
            @PathVariable Long id,
            @RequestParam PerfilUsuario novoPerfil){
        UsuarioModel usuarioAtualizado = usuarioService.alterarPerfil(id, novoPerfil);
        return ResponseEntity.ok(usuarioAtualizado);
    }
}