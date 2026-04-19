package com.exactpromos.dto.request.PromocaoDTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
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
public class PromocaoCreateDTO {

    @NotNull
    private Long produtoId;

    @NotNull
    @PositiveOrZero
    private BigDecimal precoPromocional;

    @NotNull
    @PositiveOrZero
    private Integer descontoPercentual;

    @NotNull
    @PositiveOrZero
    private BigDecimal cashback;

    @NotBlank
    private String linkAfiliado;

    private LocalDateTime dataInicio;

    private LocalDateTime dataFim;
}
