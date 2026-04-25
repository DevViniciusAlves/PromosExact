package com.exactpromos.dto.request.PromocaoDTOs;

import com.exactpromos.Enum.PlataformaEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PromocaoLoteItemDTO {

    @NotNull
    private PlataformaEnum plataforma;

    @NotBlank
    private String linkOrigem;

    @NotBlank
    private String produtoId;

    @NotBlank
    private String nome;

    @NotBlank
    private String descricao;

    @NotBlank
    private String categoria;

    @NotBlank
    private String marca;

    @NotNull
    @PositiveOrZero
    private BigDecimal precoAtual;

    @NotNull
    @PositiveOrZero
    private BigDecimal precoPromocional;

    private String urlImagem;
}
