package com.exactpromos.dto.request.InteresseDTOs;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
public class InteresseCreateDTO {

    @NotBlank(message = "Categoria obrigatoria")
    private String categoria;

    @NotNull(message = "Preço minimo obrigatorio")
    @Positive
    private BigDecimal precoMinimo;

    @NotNull(message = "Preço maximo obrigatorio")
    @Positive
    private BigDecimal precoMaximo;

    private List<String> marcasFavoritas;

    private List<String> palavrasChaves;


}
