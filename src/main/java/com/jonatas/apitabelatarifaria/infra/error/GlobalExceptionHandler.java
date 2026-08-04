package com.jonatas.apitabelatarifaria.infra.error;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fasterxml.jackson.annotation.JsonInclude;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponse> validationHandler(MethodArgumentNotValidException e) {
        Map<String, List<String>> errorDetail = e.getBindingResult()
        .getAllErrors()
        .stream()
        .filter(error -> error instanceof FieldError)
        .map(error -> (FieldError) error)
        .collect(Collectors.groupingBy(
            FieldError::getField,
            Collectors.mapping(
                FieldError::getDefaultMessage,
                Collectors.toList()
            )
        ));

        var erro = new ErrorResponse(
            "FALHA_NA_VALIDACAO",
            "Requisição enviada está inválida",
            errorDetail
        );
        return ResponseEntity
        .status(HttpStatus.UNPROCESSABLE_CONTENT)
        .body(erro);
    } 

    public ResponseEntity<Void> fallbackHandler(Exception ex) {
        log.error("Erro inesperado.", ex);
        return ResponseEntity.internalServerError().build();
    }

    public record ErrorResponse(
        String codigo,
        String erro,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        Object detalhes
    ) {
    }

}
