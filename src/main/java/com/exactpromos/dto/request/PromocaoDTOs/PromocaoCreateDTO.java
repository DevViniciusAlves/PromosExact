package com.exactpromos.dto.request.PromocaoDTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    private BigDecimal precoPromocional;

    @NotNull
    private Integer descontoPercentual;

    private LocalDateTime dataInicio;

    private LocalDateTime dataFim;
}
