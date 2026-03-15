package com.exactpromos.dto.response.StatusPromocaoDTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class StatusPromocaoDTO {

    private Double scoreQualidade;

    private Double reputacaoComunitaria;

    private Integer votosPositivos;

    private Integer votosNegativos;

    private Integer totalVotos;

    private Integer percentualPositivo;

    private String analiseIA;

    private Boolean recomendada;
}
