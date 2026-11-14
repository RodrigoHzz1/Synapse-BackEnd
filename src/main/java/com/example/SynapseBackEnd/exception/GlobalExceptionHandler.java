package com.example.SynapseBackEnd.exception;

import com.example.SynapseBackEnd.dto.ErrosResponseDTO;
import com.example.SynapseBackEnd.dto.ErrosResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. Erros de regra de negócio lançados com ResponseStatusException (400, 404, 409, etc.)
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrosResponseDTO> handleResponseStatus(
            ResponseStatusException ex, HttpServletRequest request) {

        HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());

        ErrosResponseDTO body = ErrosResponseDTO.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .erro(status.getReasonPhrase())
                .mensagem(ex.getReason())
                .caminho(request.getRequestURI())
                .build();

        return ResponseEntity.status(status).body(body);
    }

    // 2. Erros de permissão RBAC (ex: Aluno tentando reservar Sala) -> 403 Forbidden
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrosResponseDTO> handleAccessDenied(
            AccessDeniedException ex, HttpServletRequest request) {

        ErrosResponseDTO body = ErrosResponseDTO.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.FORBIDDEN.value())
                .erro("Forbidden")
                .mensagem(ex.getMessage() != null ? ex.getMessage() : "Acesso negado para este perfil.")
                .caminho(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(body);
    }

    // 3. Erros de validação dos DTOs -> 400 Bad Request
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrosResponseDTO> handleValidationErrors(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        Map<String, String> errosCampos = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errosCampos.put(error.getField(), error.getDefaultMessage());
        }

        ErrosResponseDTO body = ErrosResponseDTO.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .erro("Erro de validação")
                .mensagem("Verifique os campos obrigatórios ou inválidos.")
                .caminho(request.getRequestURI())
                .campos(errosCampos)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    // 4. Erros de JSON malformado, data/hora inválida ou Enum inexistente -> 400 Bad Request
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrosResponseDTO> handleUnreadableMessage(
            HttpMessageNotReadableException ex, HttpServletRequest request) {

        ErrosResponseDTO body = ErrosResponseDTO.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .erro("Requisição inválida")
                .mensagem("Formato inválido, data/hora incorreta ou valo não reconhecido.")
                .caminho(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    //para qualquer erro inesperado -> 500 Internal Server Error
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrosResponseDTO> handleGenericException(
            Exception ex, HttpServletRequest request) {

        ErrosResponseDTO body = ErrosResponseDTO.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .erro("Erro interno do servidor")
                .mensagem("Ocorreu um erro inesperado. Tente novamente mais tarde.")
                .caminho(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}