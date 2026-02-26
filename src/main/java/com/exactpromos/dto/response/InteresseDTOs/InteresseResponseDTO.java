package com.exactpromos.dto.response.InteresseDTOs;

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
public class InteresseResponseDTO {

    private Long id;

    private String categoria;

    private BigDecimal precoMinimo;

    private BigDecimal precoMaximo;

    private List<String> marcasFavoritas;

    private Boolean ativo;
}
