package com.api_vendinha.api.domain.dtos.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProdutoRequestDto {
    private Integer id;
    private String name;
    private Integer quantidade;
    private Double preco;
    private Boolean active;
    private Long user_id;
}
