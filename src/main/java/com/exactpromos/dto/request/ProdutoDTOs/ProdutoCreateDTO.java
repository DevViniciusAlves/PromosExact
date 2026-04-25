package com.exactpromos.dto.request.ProdutoDTOs;

import com.exactpromos.Enum.PlataformaEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoCreateDTO {

    @NotBlank
    private String produtoId;

    @NotNull
    private PlataformaEnum plataforma;

    @NotBlank
    @Size(min = 3, max = 150)
    private String nome;

    @NotBlank
    @Size(min = 15, max = 150)
    private String descricao;

    private String urlImagem;

    @NotBlank
    private String categoria;

    @NotBlank
    private String marca;

    @NotNull
    @PositiveOrZero
    private BigDecimal precoAtual;

    @NotNull
    @PositiveOrZero
    private BigDecimal precoOriginal;

    private String linkAfiliado;

    @NotNull
    private Boolean emEstoque;
}
