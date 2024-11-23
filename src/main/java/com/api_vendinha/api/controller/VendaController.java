package com.api_vendinha.api.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api_vendinha.api.domain.dtos.request.VendaRequestDto;
import com.api_vendinha.api.domain.dtos.response.VendaResponseDto;
import com.api_vendinha.api.domain.entities.Venda;
import com.api_vendinha.api.domain.service.VendaService;

@RestController
@RequestMapping("/api/vendas")
public class VendaController {
    private final VendaService vendaService;

    public VendaController(VendaService vendaService) {
        this.vendaService = vendaService;
    }

    @PostMapping("/venda")
    public VendaResponseDto createVenda(@RequestBody VendaRequestDto vendaRequestDto) {
        return vendaService.save(vendaRequestDto);
    }

    @PutMapping("/update/{id}")
    public VendaResponseDto updateVenda(@RequestBody VendaRequestDto vendaRequestDto, @PathVariable Long id) {
        return vendaService.update(vendaRequestDto, id);
    }

    @DeleteMapping("/deletar/{id}")
    public void deletarVenda(@PathVariable Long id) {
        vendaService.deletar(id);
    }
        
    @GetMapping("/findAll")
    public List<Venda> getAllVendas() {
        return vendaService.findAll();
    }

    @GetMapping("/findById/{id}")
    public Venda findById(@PathVariable Long id) {
        return vendaService.findById(id);
    }
}

