package com.api_vendinha.api.controller;

import com.api_vendinha.api.domain.dtos.request.ProdutoRequestDto;
import com.api_vendinha.api.domain.dtos.response.ProdutoResponseDto;
import com.api_vendinha.api.domain.entities.Produto;
import com.api_vendinha.api.domain.service.ProdutoService;
import com.api_vendinha.api.domain.service.UserServiceInterface;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {
    private final ProdutoService produtorService;
    public ProdutoController(ProdutoService produtorService){
        this.produtorService = produtorService;
    }

    @PostMapping("/save")
    ProdutoResponseDto save(@RequestBody ProdutoRequestDto produtoRequestDto){
        return produtorService.save(produtoRequestDto);
    }

    @PutMapping("/update/{id}")
    ProdutoResponseDto update(@RequestBody ProdutoRequestDto produtoRequestDto, @PathVariable Long id){
        return produtorService.update(produtoRequestDto, id);
    }

    @GetMapping("/findAll")
    public List<Produto> findAll(){
        return produtorService.findAll();
    }

    @GetMapping("/findById/{id}")
    public Produto findById(@PathVariable Long id){
        return produtorService.findById(id);
    }

    @DeleteMapping("/deletar/{id}")
    public void deletar(@PathVariable  Long id) {
        produtorService.deletar(id);
    }

}
