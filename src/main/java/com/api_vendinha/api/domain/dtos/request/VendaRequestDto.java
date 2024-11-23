package com.api_vendinha.api.domain.dtos.request;

import java.math.BigDecimal;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VendaRequestDto {
    private Long id;
    private Integer quantidade;
    private BigDecimal price;
    private Long user_id; 
    private Long produto_id;
}
