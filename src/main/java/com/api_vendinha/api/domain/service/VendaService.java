package com.api_vendinha.api.domain.service;

import java.util.List;

import com.api_vendinha.api.domain.dtos.request.VendaRequestDto;
import com.api_vendinha.api.domain.dtos.response.VendaResponseDto;
import com.api_vendinha.api.domain.entities.Venda;

public interface VendaService {
    VendaResponseDto save(VendaRequestDto vendaRequestDto);
    VendaResponseDto update(VendaRequestDto vendaRequestDto, Long id);
    List<Venda> findAll();
    Venda findById(Long id);
    void deletar(Long id);
}
