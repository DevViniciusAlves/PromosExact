package com.exactpromos.dto.request.PromocaoDTOs;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PromocaoFilterDTO {

    @NotBlank
    private List<String> categorias;

    @NotNull
    private BigDecimal precoMinimo;

    @NotNull
    private BigDecimal precoMaximo;

    @NotBlank
    private List<PlataformaEnum> plataforma;

    private Boolean apenasRecomendadas;

    private Integer page;

    private Integer size;
}
