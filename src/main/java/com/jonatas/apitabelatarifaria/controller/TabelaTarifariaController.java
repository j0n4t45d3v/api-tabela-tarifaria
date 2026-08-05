package com.jonatas.apitabelatarifaria.controller;

import com.jonatas.apitabelatarifaria.dto.CriarTabelaTarifariaRequest;
import com.jonatas.apitabelatarifaria.entity.TabelaTarifaria;
import com.jonatas.apitabelatarifaria.service.CriarTabelaTarifariaFachadaService;
import com.jonatas.apitabelatarifaria.service.TabelaTarifariaService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Set;

@Tag(name = "Tabelas Tarifárias")
@RestController
@RequestMapping("/tabelas-tarifarias")
public class TabelaTarifariaController {

    private final CriarTabelaTarifariaFachadaService criarTabelaTarifariaFachadaService;
    private final TabelaTarifariaService tabelaTarifariaService;

    public TabelaTarifariaController(
        CriarTabelaTarifariaFachadaService criarTabelaTarifariaFachadaService,
        TabelaTarifariaService tabelaTarifariaService
    ) {
        this.criarTabelaTarifariaFachadaService = criarTabelaTarifariaFachadaService;
        this.tabelaTarifariaService = tabelaTarifariaService;
    }

    @PostMapping
    public ResponseEntity<Void> criar(@RequestBody @Valid CriarTabelaTarifariaRequest request) {
        var tabelaTarifaria = this.criarTabelaTarifariaFachadaService.executar(request);
        var localizacao = UriComponentsBuilder
                .fromPath("/tabelas-tarifarias/{id}")
                .buildAndExpand(tabelaTarifaria.getId())
                .toUri();
        return ResponseEntity.created(localizacao).build();
    }

    @PostMapping("/lote")
    public ResponseEntity<Void> criarEmLote(@RequestBody @Valid Set<CriarTabelaTarifariaRequest> request) {
        this.criarTabelaTarifariaFachadaService.executar(request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<TabelaTarifaria>> listarTudo() {
        return ResponseEntity.ok(this.tabelaTarifariaService.listarTabelasTarifarias());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        this.tabelaTarifariaService.deletarPeloId(id);
        return ResponseEntity.noContent().build();
    }

}
