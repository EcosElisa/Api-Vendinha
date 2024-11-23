package com.api_vendinha.api.domain.dtos.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO para representar os dados necessários para criar ou atualizar um usuário.
 */
@Data
@NoArgsConstructor
public class UserRequestDto {

    private String name;
    private String email;
    private String password;
    private String cpf;
    private String cnpj;
    private Boolean active;

    private List<ProdutoRequestDto> produtoRequestDtos;
    private List<VendaRequestDto> vendaRequestDtos;
}
