package com.exactpromos.dto.response.ProdutoDTOs;

import com.exactpromos.Enum.PlataformaEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class ProdutoResponseDTO {

    private Long id;

    private String nome;

    private String categoria;

    private String marca;

    private BigDecimal precoAtual;

    private Integer descontoPercentual;

    private String urlImagem;

    private PlataformaEnum plataforma;
}
