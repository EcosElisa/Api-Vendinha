package com.api_vendinha.api.domain.dtos.response;

import com.api_vendinha.api.domain.entities.Produto;
import com.api_vendinha.api.domain.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VendaResponseDto {
    private Long id;
    private BigDecimal price;
    private Integer quantidade;
    private User user;
    private Produto produto;
}
