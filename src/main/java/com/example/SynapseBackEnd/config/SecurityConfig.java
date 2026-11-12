package com.example.SynapseBackEnd.config;

import com.example.SynapseBackEnd.repository.UsuarioRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.nio.charset.StandardCharsets;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> usuarioRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // 1. ENDPOINTS PÚBLICOS (Cadastro, Login e Erros internos do Spring)
                        .requestMatchers(HttpMethod.POST, "/api/usuarios", "/api/usuarios/", "/api/usuarios/login").permitAll()
                        .requestMatchers("/error").permitAll()

                        // 2. REGRAS DE ALTERAÇÃO DE PERFIL
                        .requestMatchers(HttpMethod.PATCH, "/api/usuarios/*/perfil").hasRole("COORDENADOR")

                        // 3. REGRAS DE SALAS
                        .requestMatchers(HttpMethod.POST, "/api/salas/**").hasRole("COORDENADOR")
                        .requestMatchers(HttpMethod.PUT, "/api/salas/**").hasRole("COORDENADOR")
                        .requestMatchers(HttpMethod.PATCH, "/api/salas/**").hasRole("COORDENADOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/salas/**").hasRole("COORDENADOR")
                        .requestMatchers(HttpMethod.GET, "/api/salas/**").hasAnyRole("COORDENADOR", "PROFESSOR")

                        // 4. REGRAS DO LABMAKER
                        .requestMatchers(HttpMethod.POST, "/api/labmaker/**").hasRole("COORDENADOR")
                        .requestMatchers(HttpMethod.PUT, "/api/labmaker/**").hasRole("COORDENADOR")
                        .requestMatchers(HttpMethod.PATCH, "/api/labmaker/**").hasRole("COORDENADOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/labmaker/**").hasRole("COORDENADOR")
                        .requestMatchers(HttpMethod.GET, "/api/labmaker/**").authenticated()

                        // Regras de Agendamentos
                        .requestMatchers(HttpMethod.POST, "/api/agendamentos").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/agendamentos/meus").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/agendamentos/ocupados/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/agendamentos").hasRole("COORDENADOR")
                        .requestMatchers(HttpMethod.PATCH, "/api/agendamentos/*/avaliar").hasRole("COORDENADOR")

                        // 5. QUALQUER OUTRA REQUISIÇÃO
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exception -> exception
                        // Trata falta de autenticação (401)
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
                            response.getWriter().write("{\"mensagem\": \"Token ausente ou inválido. Acesso não autorizado.\"}");
                        })
                        // Trata falta de permissão/role (403)
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
                            response.getWriter().write("{\"mensagem\": \"Você não tem permissão para realizar esta ação.\"}");
                        })
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}