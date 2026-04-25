package com.exactpromos.dto.response.PromocaoDTOs;

import com.exactpromos.Enum.PromocaoEnum;
import com.exactpromos.dto.response.ProdutoDTOs.ProdutoResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class PromocaoResponseDTO {

    private Long id;

    private ProdutoResponseDTO produto;

    private BigDecimal precoPromocional;

    private Integer descontoPercentual;

    private Double scoreQualidade;

    private Double reputacaoComunitaria;

    private String linkAfiliado;

    private LocalDateTime dataFim;

    private PromocaoEnum status;


}
