package com.jonatas.apitabelatarifaria.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jonatas.apitabelatarifaria.dto.CalcularValorAPagarRequest;
import com.jonatas.apitabelatarifaria.dto.ValorAPagarResponse;
import com.jonatas.apitabelatarifaria.service.CalcularValorAPagarService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/calculos")
public class CalculosController {

    private final CalcularValorAPagarService calcularValorAPagarService;

    public CalculosController(CalcularValorAPagarService calcularValorAPagarService) {
        this.calcularValorAPagarService = calcularValorAPagarService;
    }

    @PostMapping
    public ResponseEntity<ValorAPagarResponse> calcularValorAPagar(
        @RequestBody @Valid CalcularValorAPagarRequest request
    ) {
        return ResponseEntity.ok(this.calcularValorAPagarService.executar(request));
    }
}
