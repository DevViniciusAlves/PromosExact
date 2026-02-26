package com.exactpromos.dto.response.PromocaoDTOs;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PromocaoSimplificadaDTO {

    private Long id;

    private String nomeProduto;

    private BigDecimal preco;

    private Integer desconto;
}
